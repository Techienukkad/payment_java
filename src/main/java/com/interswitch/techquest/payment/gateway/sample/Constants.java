/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interswitch.techquest.payment.gateway.sample;

/**
 *
 * @author Abiola.Adebanjo
 */
public class Constants {

    final static String CLIENT_ID = "IKIA9614B82064D632E9B6418DF358A6A4AEA84D7218";
    final static String CLIENT_SECRET = "XCTiBtLy1G9chAnyg0z3BcaFK4cVpwDg/GTw2EmjTZ8=";

    public static final String PURCHASE_URL = "api/v3/purchases";
    public static String PURCHASE_OTP_URL = "api/v3/purchases/otps/auths";
    public static String VALIDATION_URL = "api/v3/purchases/validations";
    public static String VALIDATION_OTP_URL = "api/v3/purchases/validations/otps/auths";

    public static final String SIGNATURE_METHOD = "SHA-256";
    public static final String HTTP_CODE = "HTTP_CODE";
    public static final String RESPONSE_BODY = "RESPONSE_BODY";

    public static String CERTIFICATE_FILE_PATH = "C:\\Users\\abiola.adebanjo\\Documents\\isw-api-jam\\paymentgateway.crt";
    static String POST = "POST";
    static String GET = "GET";
}
