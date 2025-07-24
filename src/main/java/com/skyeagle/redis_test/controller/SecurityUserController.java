package com.skyeagle.redis_test.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyeagle.redis_test.model.SecurityUser;
import com.skyeagle.redis_test.service.CustomUserDetailsService;

@RestController
@RequestMapping(value = "/api/v1/security/user")
public class SecurityUserController {

    private CustomUserDetailsService customUserDetailsService;

    private PasswordEncoder passwordEncoder;

    public SecurityUserController(
            CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/add")
    public ResponseEntity<SecurityUser> addSecurityUser(@RequestBody SecurityUser securityUser) {
        return new ResponseEntity<>(
                customUserDetailsService.addSecurityUser(securityUser), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SecurityUser securityUser) {
        try {
            customUserDetailsService.addSecurityUser(securityUser);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("User registration failed");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody SecurityUser securityUserRaw) {
        try {
            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(securityUserRaw.getUsername());
            if (userDetails != null) {
                SecurityUser securityUser =
                        SecurityUser.builder()
                                .username(securityUserRaw.getUsername())
                                .password(passwordEncoder.encode(securityUserRaw.getPassword()))
                                .roles(
                                        securityUserRaw.getRoles() != null
                                                ? securityUserRaw.getRoles()
                                                : Set.of("USER"))
                                .build();
                customUserDetailsService.updateSecurityUser(securityUser);
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Password update failed");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SecurityUser>> getAllSecurityUsers() {
        try {
            List<SecurityUser> securityUsers = customUserDetailsService.getAllSecurityUsers();
            return ResponseEntity.ok(securityUsers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }
}
