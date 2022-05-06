package com.tdonuk.apigateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final String secret = "secret";

    public void validate(String tokenHeader) throws Exception{
        try {
            String token = tokenHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decoded = verifier.verify(token);

            String username = decoded.getSubject();
            String[] roles = decoded.getClaim("roles").asArray(String.class);

        } catch(Exception e) {
            if(e instanceof TokenExpiredException) throw new Exception("Oturum süreniz sonlanmıştır. Lütfen tekrar giriş yapınız.");
            else if(e instanceof JWTDecodeException) throw new Exception("Oturum açarken bir sorun oluştu. Lütfen tekrar giriş yapınız.");
            else throw new Exception("Bilinmeyen bir sorun oluştu. Lütfen tekrar giriş yapınız.");
        }

    }
}
