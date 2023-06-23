package com.seregamazur.oauth2.tutorial.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

public class JWTVerificationService {

    public static Pattern pattern = Pattern.compile("\"kid\":\"(.*?)\"");

    public static void verifyToken(String jwtToken) {
        String jwtHeader = jwtToken.split("\\.")[0];
        String jwtPayload = jwtToken.split("\\.")[1];
        String header = base64UrlDecode(jwtHeader);
        Matcher matcher = pattern.matcher(header);
        String keyId = null;
        if (matcher.find()) {
            keyId = matcher.group(1);
        }
        JwtParser parser = Jwts.parserBuilder().setSigningKey(keyId).build();
        Jwt<Header, Claims> claimsJwt = parser.parse(jwtToken);
        Claims body = claimsJwt.getBody();

//        jwtPayload = jwtPayload.replace('-', '+').replace('_', '/');
        // Step 2: Decode the payload
//        String decodedPayload = base64UrlDecode(jwtPayload.getBytes(StandardCharsets.UTF_8));

        // Step 3: Parse the JSON payload into JWT claims

        // Step 4: Verify token claims
        Claims claims = body;

        // Verify issuer (iss) claim
        String issuer = claims.getIssuer();
        if (!"https://www.facebook.com".equals(issuer)) {
            System.out.println("Invalid issuer");
            return;
        }

        // Verify audience (aud) claim
        String audience = claims.getAudience();
        if (!"YOUR_FACEBOOK_APP_ID".equals(audience)) {
            System.out.println("Invalid audience");
            return;
        }

        // Verify expiration (exp) claim
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            System.out.println("Token has expired");
            return;
        }

        // Step 5: Perform additional checks (optional)

        // Token is valid
        System.out.println("Token is valid");
    }


    private static String base64UrlDecode(String bytes) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(bytes);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}

