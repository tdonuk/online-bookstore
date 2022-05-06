package com.tdonuk.userservice.controller;

import com.tdonuk.userservice.model.entity.UserEntity;
import com.tdonuk.userservice.model.repository.UserRepository;
import com.tdonuk.userservice.service.UserService;
import com.tdonuk.userservice.util.JwtUtils;
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

    @GetMapping("/authorize")
    public ResponseEntity<?> checkUser(HttpServletRequest request) {
        try {
            JwtUtils.validate(request.getHeader("access_token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

}
