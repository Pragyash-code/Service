package com.payments.paymentGateway.controller;

import com.payments.paymentGateway.dto.StripeChargeDTO;
import com.payments.paymentGateway.dto.StripeTokenDTO;
import com.payments.paymentGateway.service.impl.StripeServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private StripeServiceImpl stripeService;

    //    @PostMapping("/create-payment-intent")
//    public CreatePaymentResponse createPaymentIntent(@RequestBody @Valid  CreatePayment createPayment)throws StripeException {
//        PaymentIntentCreateParams createParams = new
//                PaymentIntentCreateParams.Builder()
//                .setCurrency("usd")
//                .putMetadata("featureRequest", createPayment.getFeatureRequest())
//                .setAmount(createPayment.getAmount() * 100L)
//                .build();
//
//        PaymentIntent intent = PaymentIntent.create(createParams);
//        return new CreatePaymentResponse(intent.getClientSecret());
//    }

    @CrossOrigin
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("publishableKey", System.getenv("pk_test_51OAlGCSBqoRk66YBFJDYfsxMz284PgeIAl9WgNHMlEIChBZX7DoHVrrGwAEu3nqqql5vQfg7dSGkHosD0WqahxMR00kLy07i6z"));
        return config;
    }

    @PostMapping("/card/token")
    public PaymentIntent createCardToken(@RequestBody StripeTokenDTO model) throws StripeException {
        return stripeService.createCardToken(model);
    }

    @PostMapping("/setup")
    public PaymentIntent updatePI(@RequestParam String paymentId) throws StripeException {
        return stripeService.updatePI(paymentId);
    }

    @PostMapping("/comfirm")
    public PaymentIntent comfirmPayment(@RequestParam String paymentId) throws StripeException {
        return stripeService.paymentCompletion(paymentId);
    }

    @PostMapping("/capture")
    public PaymentIntent capturePayment(@RequestParam String paymentId) throws StripeException {
        return stripeService.capturePayment(paymentId);
    }

    @PostMapping("/card/charge")
    public StripeChargeDTO charge(@RequestBody StripeChargeDTO model){
        return stripeService.charge(model);
    }
}
