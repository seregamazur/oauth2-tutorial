package com.seregamazur.oauth2.tutorial.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
        MediaType.valueOf("text/*"),
        MediaType.APPLICATION_FORM_URLENCODED,
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML,
        MediaType.valueOf("application/*+json"),
        MediaType.valueOf("application/*+xml"),
        MediaType.MULTIPART_FORM_DATA
    );

    /**
     * List of HTTP headers whose values should not be logged.
     */
    private static final List<String> HEADERS_TO_LOG = List.of(
        "authorization",
        "content-type",
        "host"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        StringBuilder msg = new StringBuilder();

        try {
            filterChain.doFilter(request, response);
        } finally {
            /*
            We log request body after response because of ContentCachingWrappers
            They capture the request body by reading it as it passes through the filter chain.
             */
            logRequestHeaders(request, msg);
            logResponse(response, msg);
            log.info(msg.toString());
            response.copyBodyToResponse();
        }
    }

    private static void logRequestHeaders(ContentCachingRequestWrapper request, StringBuilder msg) {
        msg.append("\n-- REQUEST --\n");
        String queryString = request.getQueryString();
        if (queryString == null) {
            msg.append(String.format("%s %s", request.getMethod(), request.getRequestURI())).append("\n");
        } else {
            msg.append(String.format("%s %s?%s", request.getMethod(), request.getRequestURI(), queryString)).append("\n");
        }
        String requestHeaders = Collections.list(request.getHeaderNames())
            .stream().map(headerName -> Collections.list(request.getHeaders(headerName))
                .stream().filter(e -> isValidToLog(headerName))
                .map(headerValue -> String.format("%s: %s\n", headerName, headerValue)).collect(Collectors.joining()))
            .collect(Collectors.joining());
        msg.append(requestHeaders);
        logRequestBody(request, msg);
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, StringBuilder msg) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logPayload(content, request.getContentType(), request.getCharacterEncoding(), msg);
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, StringBuilder msg) {
        msg.append("\n-- RESPONSE --\n");
        int status = response.getStatus();
        msg.append(String.format("%s: %s %s", "status", status, HttpStatus.valueOf(status).getReasonPhrase())).append("\n");
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logPayload(content, response.getContentType(), response.getCharacterEncoding(), msg);
        }
    }

    private static void logPayload(byte[] content, String contentType, String contentEncoding, StringBuilder msg) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                String contentLine = Stream.of(contentString.split("\n")).map(line -> "body: " + line + "\n")
                    .collect(Collectors.joining());
                msg.append(contentLine);
            } catch (UnsupportedEncodingException e) {
                msg.append(String.format("[%d bytes content]", content.length)).append("\n");
            }
        } else {
            msg.append(String.format("[%d bytes content]", content.length)).append("\n");
        }
    }

    /**
     * Determine if a given header name should have its value logged.
     *
     * @param headerName HTTP header name.
     * @return True if the header is sensitive (i.e. its value should <b>not</b> be logged).
     */
    private static boolean isValidToLog(String headerName) {
        return HEADERS_TO_LOG.contains(headerName.toLowerCase());
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
