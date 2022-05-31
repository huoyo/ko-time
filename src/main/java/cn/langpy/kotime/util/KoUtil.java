package cn.langpy.kotime.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

public class KoUtil {
    private final static String koTimeSecret = UUID.randomUUID().toString().replace("-","");

    public static String login(String userName) {
        Algorithm algorithm = Algorithm.HMAC256(koTimeSecret);
        String token = JWT.create()
                .withIssuer("kotime")
                .withSubject(userName)
                .withExpiresAt(new Date(System.currentTimeMillis() + (12*60*60*1000)))
                .withClaim("author", "KoTime")
                .sign(algorithm);
        return token;
    }


    public static boolean checkLogin(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(koTimeSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (JWTVerificationException verificationException){
            throw new KoTimeNotLoginException("can not find login information for kotime,please login first!");
        }
    }

    public static boolean isLogin(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(koTimeSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (JWTVerificationException verificationException){
            return false;
        }
    }

}
