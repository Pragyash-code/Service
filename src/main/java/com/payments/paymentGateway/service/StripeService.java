package com.payments.paymentGateway.service;

import com.payments.paymentGateway.dto.StripeChargeDTO;
import com.payments.paymentGateway.dto.StripeTokenDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import org.springframework.stereotype.Service;

@Service
public interface StripeService {

    PaymentIntent createCardToken(StripeTokenDTO model) throws StripeException;

    PaymentIntent paymentCompletion(String paymentId) throws StripeException;
    StripeChargeDTO charge(StripeChargeDTO model);

    PaymentIntent capturePayment(String paymentId) throws StripeException;

    PaymentIntent updatePI(String paymentId) throws StripeException;
}
