package com.tdonuk.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JwtUtils {

    public static void validate(String tokenHeader) throws Exception{
        try {
            String token = tokenHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decoded = verifier.verify(token);

            String username = decoded.getSubject();
            String[] roles = decoded.getClaim("roles").asArray(String.class);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

            log.info("Incoming request from: {}.", username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch(Exception e) {
            log.info("An error has occurred while authenticating the user.");

            if(e instanceof TokenExpiredException) throw new Exception("Oturum süreniz sonlanmıştır. Lütfen tekrar giriş yapınız.");
            else if(e instanceof JWTDecodeException) throw new Exception("Oturum açarken bir sorun oluştu. Lütfen tekrar giriş yapınız.");
            else throw new Exception("Bilinmeyen bir sorun oluştu. tekrar giriş yapınız.");
        }

    }
}
