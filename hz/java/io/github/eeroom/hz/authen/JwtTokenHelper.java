package io.github.eeroom.hz.authen;

import com.auth0.jwt.algorithms.Algorithm;
import io.github.eeroom.hz.ApplicationConfig;
import io.github.eeroom.hz.MyObjectFacotry;

import java.util.Date;

public class JwtTokenHelper {
    static String claimuserAccount="claimuserAccount";
    public static String encode(String userAccount){
        var appconfig= MyObjectFacotry.getBean(ApplicationConfig.class);
        var token= com.auth0.jwt.JWT.create()
                .withIssuer(appconfig.authenJwtIssuer)
                .withIssuedAt(new Date())
                .withClaim(claimuserAccount,userAccount)
                .sign(Algorithm.HMAC256(appconfig.authenJwtSecret));
        return token;
    }

    public static String decode(String token){
        var appconfig= MyObjectFacotry.getBean(ApplicationConfig.class);
        var jwtVerifier= com.auth0.jwt.JWT.require(Algorithm.HMAC256(appconfig.authenJwtSecret))
                .withIssuer(appconfig.authenJwtIssuer)
                .build();
        var jwtDecode= jwtVerifier.verify(token);
        var userAccount= jwtDecode.getClaim(claimuserAccount).asString();
        return userAccount;
    }
}
