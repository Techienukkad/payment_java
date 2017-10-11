package com.interswitch.techquest.payment.java.sample;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.techquest.auth.Interswitch;
import com.interswitch.techquest.payment.java.Constants;
import com.interswitch.techquest.payment.java.PaymentGateway;

public class TestDriver {

    public static final String HTTP_CODE = "HTTP_CODE";
    public static final String RESPONSE_BODY = "RESPONSE_BODY";

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("To leave a field empty, please press enter");
            Scanner scanner = new Scanner(System.in);
            String quitFlag = "";

            PaymentGateway paymentGateway = new PaymentGateway(Constants.CLIENT_ID, Constants.CLIENT_SECRET, Interswitch.ENV_SANDBOX);

            while ((quitFlag != null) && (!quitFlag.equalsIgnoreCase("q"))) {
                System.out.println("");
                System.out.println("===================================");
                System.out.println("1. Validations");
                System.out.println("2. Purchases");
                System.out.println("3. Requery");
                String menuItem = "1";
                scanner = new Scanner(System.in);
                menuItem = scanner.nextLine();

                HashMap<String, String> interswitchResponse = new HashMap<String, String>();
                Map<String, String> map = new HashMap();
                TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
                };
                ObjectMapper mapper = new ObjectMapper();
                String responseCode, paymentId, transactionRef, httpResponseCode, response;

                if ("3".equals(menuItem)) {
                    System.out.println("==========================REQUERY==========================");
                    System.out.println("Enter amount in major denomination e.g. 100.00");
                    String amount = "2000.00";
                    amount = scanner.nextLine();
                    System.out.println("Enter transactionRef");
                    transactionRef = scanner.nextLine();
                    interswitchResponse = paymentGateway.doTransactionQuery(amount, transactionRef);
                    httpResponseCode = interswitchResponse.get(Interswitch.RESPONSE_CODE);
                    response = interswitchResponse.get(Interswitch.RESPONSE_MESSAGE);

                    System.out.println("HTTP Code: " + httpResponseCode);
                    System.out.println("Response: " + response);
                }

                System.out.println("");
                System.out.println("===================================");
                System.out.println("Enter your PAN: ");
                String pan = "5060990580000217499";
                pan = scanner.nextLine();
                System.out.println("Enter PAN Expiry Date (Format YYMM e.g. 5004 for Apr, 2050): ");
                String expiryDate = "2004";
                expiryDate = scanner.nextLine();
                System.out.println("Enter CVV. Press enter to ignore: ");
                String cvv = "111";
                cvv = scanner.nextLine();
                System.out.println("Enter PIN. Press enter to ignore: ");
                String pin = "1111";
                pin = scanner.nextLine();

                if ("1".equals(menuItem)) {
                    System.out.println("==========================VALIDATIONS==========================");
                    interswitchResponse = paymentGateway.doValidation(pan, pin, cvv, expiryDate);
                    httpResponseCode = interswitchResponse.get(Interswitch.RESPONSE_CODE);
                    response = interswitchResponse.get(Interswitch.RESPONSE_MESSAGE);

                    System.out.println("HTTP Code: " + httpResponseCode);
                    System.out.println("Response: " + response);

                    if (httpResponseCode.equalsIgnoreCase("202")) {
                        map = (Map) mapper.readValue(response, typeRef);
                        responseCode = (String) map.get("responseCode");
                        transactionRef = (String) map.get("transactionRef");
                        System.out.println("Enter your OTP e.g. 958274");
                        String otp = "958274";
                        otp = scanner.nextLine();
                        interswitchResponse = paymentGateway.doValidationOTP(transactionRef, paymentGateway.getAuthData(), otp);
                        httpResponseCode = interswitchResponse.get(Interswitch.RESPONSE_CODE);
                        response = interswitchResponse.get(Interswitch.RESPONSE_MESSAGE);

                        System.out.println("HTTP Code: " + httpResponseCode);
                        System.out.println("Response: " + response);
                    }

                } else if ("2".equals(menuItem)) {
                    System.out.println("==========================PURCHASES==========================");
                    System.out.println("Enter amount in major denomination e.g. 100.00");
                    String amount = "2000.00";
                    amount = scanner.nextLine();
                    interswitchResponse = paymentGateway.doPurchase(pan, pin, cvv, expiryDate, amount);
                    httpResponseCode = interswitchResponse.get(Interswitch.RESPONSE_CODE);
                    response = interswitchResponse.get(Interswitch.RESPONSE_MESSAGE);

                    System.out.println("HTTP Code: " + httpResponseCode);
                    System.out.println("Response: " + response);

                    if (httpResponseCode.equalsIgnoreCase("202")) {
                        map = (Map) mapper.readValue(response, typeRef);
                        responseCode = (String) map.get("responseCode");
                        if (responseCode.equalsIgnoreCase("T0")) {
                            paymentId = (String) map.get("paymentId");
                            System.out.println("Enter your OTP e.g. 958274");
                            String otp = "958274";
                            otp = scanner.nextLine();
                            interswitchResponse = paymentGateway.doPurchaseOTP(paymentId, paymentGateway.getAuthData(), otp);
                            httpResponseCode = interswitchResponse.get(Interswitch.RESPONSE_CODE);
                            response = interswitchResponse.get(Interswitch.RESPONSE_MESSAGE);

                            System.out.println("HTTP Code: " + httpResponseCode);
                            System.out.println("Response: " + response);
                        }
                    }
                }

                System.out.println();
                System.out.println("===================================");
                System.out.println("Press any key to contiue, Q to quit");
                quitFlag = scanner.nextLine();
            }
            scanner.close();
        } catch (Exception ex) {
            System.out.println("Exception occured: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
