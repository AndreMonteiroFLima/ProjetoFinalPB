package com.adobe.aem.guides.wknd.core.auth;

import com.adobe.aem.guides.wknd.core.exception.JwtInvalidException;

import java.security.SecureRandom;
import java.util.Base64;

import static org.apache.commons.codec.digest.HmacUtils.hmacSha256;


public class JwtEmulator {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private static final Base64.Decoder base64Decoder = Base64.getUrlDecoder(); //threadsafe


    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final String JWT_CLAIMS = "{\"iss\":\"%s\",\"sub\":\"%s\",\"aud\":\"%s\",\"exp\":%d,\"iat\":%d}";

    private static final String JWT_SECRET = "secret";
    private static final String JWT_ISSUER = "aem-wknd-auth";
    private static final String JWT_AUDIENCE = "aem-wknd-auth";

    public static String generateJwt(String username, String audience) {
        String header = base64Encoder.encodeToString(JWT_HEADER.getBytes());
        String claims = base64Encoder.encodeToString(String.format(JWT_CLAIMS, JWT_ISSUER, username, audience,
                (System.currentTimeMillis() / 1000) + 60, System.currentTimeMillis() / 1000).getBytes());
        String signature = base64Encoder.encodeToString(hmacSha256(JWT_SECRET, header + "." + claims));
        return header + "." + claims + "." + signature;
    }

    public static Boolean validateJwt(String jwt) throws JwtInvalidException {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new JwtInvalidException("Invalid JWT");
        }
        String header = new String(base64Decoder.decode(parts[0]));
        String claims = new String(base64Decoder.decode(parts[1]));
        String signature = parts[2];
        String expectedSignature = base64Encoder.encodeToString(hmacSha256(JWT_SECRET, header + "." + claims));
        if(signature.equals(expectedSignature)){
            return true;
        }else
            throw new JwtInvalidException("Invalid JWT");
    }


}
