package com.seregamazur.oauth2.tutorial.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.adyen.Client;
import com.adyen.enums.Environment;
import com.adyen.model.checkout.Amount;
import com.adyen.model.checkout.LineItem;
import com.adyen.model.checkout.PaymentCompletionDetails;
import com.adyen.model.checkout.PaymentDetailsRequest;
import com.adyen.model.checkout.PaymentMethodsRequest;
import com.adyen.model.checkout.PaymentMethodsResponse;
import com.adyen.model.checkout.PaymentRequest;
import com.adyen.model.checkout.PaymentResponse;
import com.adyen.service.checkout.PaymentsApi;
import com.adyen.service.exception.ApiException;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdyenController {

    private String adyenApiKey;
    private String adyenSecretKey;
    private String merchantAccount;

    private final PaymentsApi paymentsApi;

    public AdyenController(@Value("${adyen.api-key}") String adyenApiKey,
        @Value("${adyen.secret-key}") String adyenSecretKey,
        @Value("${adyen.merchant-account}") String merchantAccount) {
        this.adyenApiKey = adyenApiKey;
        this.adyenSecretKey = adyenSecretKey;
        this.merchantAccount = merchantAccount;
        Client client = new Client(adyenApiKey, Environment.TEST);
        this.paymentsApi = new PaymentsApi(client);
    }

    @PostMapping("/api/v1/adyen/initiate-payment")
    public ResponseEntity<PaymentResponse> payments(@RequestHeader String host, @RequestBody PaymentRequest body, HttpServletRequest request) throws IOException, ApiException {
        var paymentRequest = new PaymentRequest();

        var orderRef = UUID.randomUUID().toString();
        var amount = new Amount()
            .currency("EUR")
            .value(10000L); // value is 10€ in minor units

        paymentRequest.setMerchantAccount(merchantAccount); // required
        paymentRequest.setChannel(PaymentRequest.ChannelEnum.WEB);
        paymentRequest.setReference(orderRef); // required
        paymentRequest.setReturnUrl(request.getScheme() + "://" + host + "/api/handleShopperRedirect?orderRef=" + orderRef);

        paymentRequest.setAmount(amount);
        // set lineItems required for some payment methods (ie Klarna)
        paymentRequest.setLineItems(Arrays.asList(
            new LineItem().quantity(1L).amountIncludingTax(5000L).description("Sunglasses"),
            new LineItem().quantity(1L).amountIncludingTax(5000L).description("Headphones"))
        );
        // required for 3ds2 native flow
        paymentRequest.setAdditionalData(Collections.singletonMap("allow3DS2", "true"));
        // required for 3ds2 native flow
        paymentRequest.setOrigin(request.getScheme() + "://" + host);
        // required for 3ds2
        paymentRequest.setBrowserInfo(body.getBrowserInfo());
        // required by some issuers for 3ds2
        paymentRequest.setShopperIP(request.getRemoteAddr());
        paymentRequest.setPaymentMethod(body.getPaymentMethod());

        log.info("REST request to make Adyen payment {}", paymentRequest);
        var response = paymentsApi.payments(paymentRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/v1/adyen/payment-methods")
    public ResponseEntity<PaymentMethodsResponse> paymentMethods() throws IOException, ApiException {
        var paymentMethodsRequest = new PaymentMethodsRequest();
        paymentMethodsRequest.setMerchantAccount(merchantAccount);
        paymentMethodsRequest.setChannel(PaymentMethodsRequest.ChannelEnum.WEB);

        log.info("REST request to get Adyen payment methods {}", paymentMethodsRequest);
        var response = paymentsApi.paymentMethods(paymentMethodsRequest);
        return ResponseEntity.ok().body(response);
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
