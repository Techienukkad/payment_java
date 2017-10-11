package com.interswitch.techquest.payment.gateway.sample;

import com.interswitch.techquest.auth.Interswitch;
import com.interswitch.techquest.auth.helpers.RequestHeaders;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;

/**
 *
 * @author Abiola.Adebanjo
 */
public class PaymentGateway {

    private Interswitch interswitchBase;

    public PaymentGateway(String clientId, String clientSecret) {
        this(clientId, clientSecret, Interswitch.ENV_SANDBOX);
    }

    public PaymentGateway(String clientId, String clientSecret, String env) {

        if (clientId == null) {
            throw new IllegalArgumentException("CliendId is empty");
        }
        if (clientSecret == null) {
            throw new IllegalArgumentException("CliendSecret is empty");
        }

        if (env == null) {
            env = Interswitch.SANDBOX_BASE_URL;
        }

        interswitchBase = new Interswitch(clientId, clientSecret, env);
    }

    static String authData = "";

    public HashMap<String, String> doPurchase(String pan, String pin, String cvv2, String expiryDate, String amount) throws Exception {
        HashMap<String, String> interswitchResponse;

        authData = interswitchBase.getAuthData(Constants.CERTIFICATE_FILE_PATH, pan, expiryDate, cvv2, pin);

        String customerId = "paymenttestdriver@interswitchgroup.com";
        String currency = "NGN";

        JSONObject json = new JSONObject();
        json.put("customerId", customerId);
        json.put("amount", amount);
        json.put("currency", currency);
        json.put("transactionRef", generateRef());
        json.put("authData", authData);
        String jsonData = json.toString();

        String signatureParameters = amount + "&" + authData;

        interswitchResponse = interswitchBase.send(Constants.PURCHASE_URL, Constants.POST, jsonData, signatureParameters);
        return interswitchResponse;
    }

    public HashMap<String, String> doPurchaseOTP(String paymentId, String authData, String otp) throws Exception {
        HashMap<String, String> interswitchResponse;

        JSONObject json = new JSONObject();
        json.put("paymentId", paymentId);
        json.put("authData", authData);
        json.put("otp", otp);
        String jsonData = json.toString();

        String signatureParameters = otp + "&" + authData;

        interswitchResponse = interswitchBase.send(Constants.PURCHASE_OTP_URL, Constants.POST, jsonData, signatureParameters);
        return interswitchResponse;
    }

    public HashMap<String, String> doValidation(String pan, String pin, String cvv2, String expiryDate) throws Exception {
        HashMap<String, String> interswitchResponse;

        authData = interswitchBase.getAuthData(Constants.CERTIFICATE_FILE_PATH, pan, expiryDate, cvv2, pin);

        JSONObject json = new JSONObject();
        json.put("transactionRef", generateRef());
        json.put("authData", authData);
        String jsonData = json.toString();

        interswitchResponse = interswitchBase.send(Constants.VALIDATION_URL, Constants.POST, jsonData);
        return interswitchResponse;
    }

    public HashMap<String, String> doValidationOTP(String transRef, String authData, String otp) throws Exception {
        HashMap<String, String> interswitchResponse;

        JSONObject json = new JSONObject();
        json.put("transactionRef", transRef);
        json.put("authData", authData);
        json.put("otp", otp);
        String jsonData = json.toString();

        interswitchResponse = interswitchBase.send(Constants.VALIDATION_OTP_URL, Constants.POST, jsonData);
        return interswitchResponse;
    }

    public HashMap<String, String> doTransactionQuery(String amount, String transactionRef) throws Exception {
        HashMap<String, String> interswitchResponse;
        HashMap<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("amount", amount);
        extraHeaders.put("transactionRef", transactionRef);

        interswitchResponse = interswitchBase.send(Constants.PURCHASE_URL, Constants.GET, null, extraHeaders);
        return interswitchResponse;
    }

    public String getAuthData() {
        return authData;
    }

    public static String generateRef() {
        UUID nonce = UUID.randomUUID();
        return "TEST|DRIVER|" + nonce.toString().replaceAll("-", "");
    }

}
