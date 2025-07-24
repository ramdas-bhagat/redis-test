package com.skyeagle.redis_test.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.skyeagle.redis_test.config.JwtTokenProvider;
import com.skyeagle.redis_test.model.AuthRequest;
import com.skyeagle.redis_test.model.SecurityUser;
import com.skyeagle.redis_test.model.TransactionStatus;
import com.skyeagle.redis_test.service.CustomUserDetailsService;

/** Controller for handling authentication-related operations. */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenProvider jwtUtil;

    @Autowired private CustomUserDetailsService userDetailsService;

    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param authRequest The authentication request containing username and password.
     * @return A ResponseEntity containing the JWT token if authentication is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<TransactionStatus> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(authRequest.getUsername());

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            passwordEncoder.encode(userDetails.getPassword()),
                            userDetails.getAuthorities());

            String token = jwtUtil.generateToken(authentication);
            TransactionStatus transactionStatus =
                    TransactionStatus.builder()
                            .status("success")
                            .message("User authenticated successfully")
                            .data(Map.of("token", token, "userDetails", userDetails))
                            .build();
            return ResponseEntity.status(HttpStatus.OK).body(transactionStatus);
        } catch (Exception e) {
            TransactionStatus transactionStatus =
                    TransactionStatus.builder()
                            .status("error")
                            .message("Authentication failed: " + e.getMessage())
                            .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(transactionStatus);
        }
    }

    /**
     * Refreshes an existing JWT token.
     *
     * @param token The existing JWT token to be refreshed.
     * @return A ResponseEntity containing the refreshed JWT token if valid.
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                return ResponseEntity.ok(jwtUtil.refreshToken(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Token refresh failed");
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param securityUser The user details to be registered.
     * @return A ResponseEntity containing the registration status and user details.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SecurityUser securityUser) {
        try {
            SecurityUser createdUser = userDetailsService.addSecurityUser(securityUser);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User successfully registered");
            response.put("user", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed: " + e.getMessage());
        }
    }

    /**
     * Logs out the user by invalidating the JWT token.
     *
     * @param token The JWT token to be invalidated.
     * @return A ResponseEntity indicating the logout status.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                jwtUtil.invalidateToken(token); // Invalidate the token
                // Invalidate the token (implementation depends on your token storage)
                return ResponseEntity.ok("User logged out successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed");
        }
    }
}
