package com.tdonuk.userservice.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tdonuk.userservice.model.entity.UserEntity;
import com.tdonuk.userservice.model.repository.UserRepository;
import com.tdonuk.userservice.service.UserService;
import com.tdonuk.userservice.util.JwtUtils;
import com.tdonuk.userservice.util.TimeConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Environment environment;

    @PutMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity entity) {
        UserEntity result = null;

        try {
            result = userService.save(entity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if(null != result) {
            return ResponseEntity.ok("Kullanıcı başarıyla oluşturuldu. ID: "+result.getUserId());
        }

        return ResponseEntity.badRequest().body("Bilinmeyen hata");
    }

    @GetMapping("/services")
    public ResponseEntity<?> getServices() {
        return ResponseEntity.ok(discoveryClient.getServices());
    }

    @GetMapping("/service/detail")
    public ResponseEntity<?> getInstances() {
        return ResponseEntity.ok(discoveryClient.getInstances("user-service"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        UserEntity entity = userService.findById(id);

        try {
            entity = userService.delete(entity);
        } catch (Exception e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }

        if(entity != null) {
            return ResponseEntity.ok("Kullanıcı hesabı başarıyla silindi. kullanıcı: " + entity.getUsername());
        }

        return ResponseEntity.ok("Bilinmeyen hata");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<?> getUser(@RequestParam(name = "username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkUser(HttpServletRequest request) {
        try {
            JwtUtils.validate(request.getHeader("Authorization"));
        } catch (Exception e) {
        	if(e instanceof TokenExpiredException) {
            	ResponseEntity<?> response = ResponseEntity.status(401).header("expired", "token has expired").build();
            	return response;
        	}

            return ResponseEntity.status(401).body(e.getMessage());
        }
        UserEntity entity = userService.findByEmail(JwtUtils.getUser(request.getHeader("Authorization")));
        entity.setPassword("[Protected]");
        return ResponseEntity.ok(entity);
    }
    
    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
    	String tokenHeader = request.getHeader("Authorization");
    	if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
    		try {
        		String refreshToken = tokenHeader.substring("Bearer ".length());
        		
        		Algorithm alg = Algorithm.HMAC256("secret");
        		JWTVerifier verifier = JWT.require(alg).build();
        		
        		DecodedJWT decoded = verifier.verify(refreshToken);
        		
        		String email = decoded.getSubject();
        		
        		UserEntity user = userService.findByEmail(email);
        		
        		String accessToken = JwtUtils.createDefault(email, request.getRequestURI().toString(), List.of(user.getRole().name()));
        		
        		response.setContentType("application/json");
        		
        		return ResponseEntity.ok(accessToken);
        		
    		} catch(TokenExpiredException e) {
    			response.setHeader("expired", e.getMessage());
    			return ResponseEntity.badRequest().body("Oturumunuz sonlanmıştır. Lütfen yeniden giriş yapınız.");
    		} catch(Exception e) {
    			return ResponseEntity.status(401).body("Lütfen giriş yapınız.");
    		}

    	}
    	
    	return ResponseEntity.badRequest().body("Bilinmeyen bir hata oluştu. Lütfen tekrar giriş yapınız.");
    }

}
