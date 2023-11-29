package com.payments.paymentGateway.service.impl;

import com.payments.paymentGateway.dto.StripeChargeDTO;
import com.payments.paymentGateway.dto.StripeTokenDTO;
import com.payments.paymentGateway.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.param.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

//    @Value("$api.stripe-key")
    private String stripeKey ="sk_test_51OAlGCSBqoRk66YBVWSvxyv9F6aWdI18w5OnYvPcVZQTo6zbC8OqR6bo1VFvhD5k4C92CbHiAbJg15KqIvVraDvM00fXFp8hJb";


    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeKey;
    }



    @Override
    public PaymentIntent createCardToken(StripeTokenDTO model) throws StripeException {
        Stripe.apiKey = "sk_test_51OAlGCSBqoRk66YBVWSvxyv9F6aWdI18w5OnYvPcVZQTo6zbC8OqR6bo1VFvhD5k4C92CbHiAbJg15KqIvVraDvM00fXFp8hJb";

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(2000L)
                .setCurrency("usd")
                .setDescription("Charge for jenny.rosen@example.com")
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        String clientScret= paymentIntent.getClientSecret();

//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", 2000);
//        params.put("currency", "usd");
//
//        PaymentIntent paymentIntent =
//                PaymentIntent.create(params);
//
        return paymentIntent;
    }

    @Override
    public PaymentIntent paymentCompletion(String paymentIntentId) throws StripeException {

        PaymentIntent paymentIntent =
                PaymentIntent.retrieve(paymentIntentId);
        Stripe.apiKey = "sk_test_51OAlGCSBqoRk66YBVWSvxyv9F6aWdI18w5OnYvPcVZQTo6zbC8OqR6bo1VFvhD5k4C92CbHiAbJg15KqIvVraDvM00fXFp8hJb";

        PaymentIntent resource = PaymentIntent.retrieve(paymentIntentId);

        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod("pm_card_visa")
                        .setReturnUrl("https://www.example.com")
                        .build();
        PaymentIntent updatedPaymentIntent = paymentIntent.confirm(params);
        if ("requires_action".equals(updatedPaymentIntent.getStatus())) {
            // If status is "requires_action," redirect the customer
            String redirectUrl = updatedPaymentIntent.getNextAction().getRedirectToUrl().getUrl();
            // Implement the logic to redirect the customer to the provided URL
            // This could be done through a web framework or by sending a redirect response
            // to the client in a web application
            // For simplicity, you can print the URL for testing purposes
            System.out.println("Redirect to: " + redirectUrl);
        }

//        PaymentIntent paymentIntent = resource.confirm(params);

//        Map<String, Object> params = new HashMap<>();
//        params.put("payment_method", "pm_card_visa");
//        params.put("return_url", "https://your-website.com/checkout/success");


        return updatedPaymentIntent;
    }

    @Override
    public PaymentIntent capturePayment(String intent) throws StripeException {
        Stripe.apiKey = "sk_test_51OAlGCSBqoRk66YBVWSvxyv9F6aWdI18w5OnYvPcVZQTo6zbC8OqR6bo1VFvhD5k4C92CbHiAbJg15KqIvVraDvM00fXFp8hJb";

        PaymentIntent paymentCapture =
                PaymentIntent.retrieve(intent);

        PaymentIntentCancelParams params = PaymentIntentCancelParams.builder().build();
        PaymentIntent paymentIntent = paymentCapture.cancel(params);
        return paymentIntent;
    }

    @Override
    public PaymentIntent updatePI(String paymentId) throws StripeException {
        Stripe.apiKey = "sk_test_51OAlGCSBqoRk66YBVWSvxyv9F6aWdI18w5OnYvPcVZQTo6zbC8OqR6bo1VFvhD5k4C92CbHiAbJg15KqIvVraDvM00fXFp8hJb";

        PaymentIntent paymentIntent =
                PaymentIntent.retrieve(paymentId);

        PaymentIntentUpdateParams params =
                PaymentIntentUpdateParams.builder().putMetadata("order_id", "6735").build();
        PaymentIntent Intent = paymentIntent.update(params);
        return Intent;
    }

    @Override
    public StripeChargeDTO charge(StripeChargeDTO chargeRequest){
        try{
            chargeRequest.setSuccess(false);
            Map<String, Object> chargePrams = new HashMap<>();
            chargePrams.put("amount", (int)(chargeRequest.getAmount() * 1000));
            chargePrams.put("currency","USD");
            chargePrams.put("description", "Payment for id" + chargeRequest.getAdditionInfo().getOrDefault("ID_TAG",""));
            chargePrams.put("source",chargeRequest.getStripeToken());
            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id",chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionInfo());
            chargePrams.put("metaData", metaData);
            Charge charge = Charge.create(chargePrams);
            chargeRequest.setMessage(charge.getOutcome().getSellerMessage());

            if(charge.getPaid()){
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);
            }
            return chargeRequest;
        } catch (StripeException e) {
            log.error("StripeService (Charge)", e);
            throw new RuntimeException(e.getMessage());
        }
    }


}
