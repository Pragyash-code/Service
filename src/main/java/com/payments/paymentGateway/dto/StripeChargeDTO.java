package com.payments.paymentGateway.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StripeChargeDTO {

    private String stripeToken;
    private String userName;
    private double amount;
    private Boolean success;
    private String message;
    private String chargeId;
    private Map<String,Object> additionInfo = new HashMap<>();

}
