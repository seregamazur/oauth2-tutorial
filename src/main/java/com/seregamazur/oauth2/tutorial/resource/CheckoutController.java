package com.seregamazur.oauth2.tutorial.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.adyen.Client;
import com.adyen.enums.Environment;
import com.adyen.model.checkout.Amount;
import com.adyen.model.checkout.LineItem;
import com.adyen.model.checkout.PaymentCompletionDetails;
import com.adyen.model.checkout.PaymentDetailsRequest;
import com.adyen.model.checkout.PaymentDetailsResponse;
import com.adyen.model.checkout.PaymentMethodsRequest;
import com.adyen.model.checkout.PaymentMethodsResponse;
import com.adyen.model.checkout.PaymentRequest;
import com.adyen.model.checkout.PaymentResponse;
import com.adyen.service.checkout.PaymentsApi;
import com.adyen.service.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CheckoutController {

    private String adyenApiKey;
    private String adyenSecretKey;
    private String merchantAccount;

    private final PaymentsApi paymentsApi;

    public CheckoutController(@Value("${adyen.api-key}") String adyenApiKey,
        @Value("${adyen.secret-key}") String adyenSecretKey,
        @Value("${adyen.merchant-account}") String merchantAccount) {
        this.adyenApiKey = adyenApiKey;
        this.adyenSecretKey = adyenSecretKey;
        this.merchantAccount = merchantAccount;
        Client client = new Client(adyenApiKey, Environment.TEST);
        this.paymentsApi = new PaymentsApi(client);
    }

    @GetMapping("/api/v1/payment-methods")
    public ResponseEntity<PaymentMethodsResponse> paymentMethods(@RequestParam("country_code") String code) throws IOException, ApiException {
        PaymentMethodsRequest request = new PaymentMethodsRequest();
        request.setMerchantAccount(merchantAccount);
        request.setCountryCode(code);
        Amount amount = new Amount();
        amount.setCurrency("PLN");
        amount.setValue(100L);
        request.setAmount(amount);
        request.setShopperLocale(Locale.US.toString());
        request.setChannel(PaymentMethodsRequest.ChannelEnum.WEB);

        log.info("REST request to get Adyen payment methods {}", request);
        var response = paymentsApi.paymentMethods(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/v1/adyen/initiate-payment")
    public ResponseEntity<PaymentResponse> payments(@RequestBody PaymentRequest body, HttpServletRequest request) throws IOException, ApiException {
        var paymentRequest = new PaymentRequest();

        var orderRef = UUID.randomUUID().toString();
        var amount = new Amount()
            .currency("PLN")
            .value(10000L); // value is 10€ in minor units

        paymentRequest.setMerchantAccount(merchantAccount); // required
        paymentRequest.setChannel(PaymentRequest.ChannelEnum.WEB);
        paymentRequest.setReference(orderRef); // required
        paymentRequest.setReturnUrl(request.getScheme() + "://localhost:8080/api/handleShopperRedirect?orderRef=" + orderRef);

        paymentRequest.setAmount(amount);
        // set lineItems required for some payment methods (ie Klarna)
        paymentRequest.setLineItems(Arrays.asList(
            new LineItem().quantity(1L).amountIncludingTax(5000L).description("Sunglasses"),
            new LineItem().quantity(1L).amountIncludingTax(5000L).description("Headphones"))
        );
        // required for 3ds2 native flow
        paymentRequest.setAdditionalData(Collections.singletonMap("allow3DS2", "true"));
        // required for 3ds2 native flow
        paymentRequest.setOrigin(request.getScheme() + "://localhost:8080");
        // required for 3ds2
        paymentRequest.setBrowserInfo(body.getBrowserInfo());
        // required by some issuers for 3ds2
        paymentRequest.setShopperIP(request.getRemoteAddr());
        paymentRequest.setPaymentMethod(body.getPaymentMethod());

        log.info("REST request to make Adyen payment {}", paymentRequest);
        var response = paymentsApi.payments(paymentRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/submitAdditionalDetails")
    public ResponseEntity<PaymentDetailsResponse> payments(@RequestBody PaymentDetailsRequest detailsRequest) throws IOException, ApiException {
        log.info("REST request to make Adyen payment details {}", detailsRequest);
        var response = paymentsApi.paymentsDetails(detailsRequest);
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/handleShopperRedirect")
    public ResponseEntity<String> redirect(@RequestParam(required = false) String payload, @RequestParam(required = false) String redirectResult, @RequestParam String orderRef) throws IOException, ApiException {
        var detailsRequest = new PaymentDetailsRequest();

        PaymentCompletionDetails details = new PaymentCompletionDetails();
        if (redirectResult != null && !redirectResult.isEmpty()) {
            details.redirectResult(redirectResult);
        } else if (payload != null && !payload.isEmpty()) {
            details.payload(payload);
        }

        detailsRequest.setDetails(details);
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/api/v1/adyen/callback")
    public ResponseEntity<String> callbackFromPayment() throws IOException, ApiException {
        log.info("Got callback from adyen");
        return ResponseEntity.ok("hello");
    }


}
