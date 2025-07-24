package com.skyeagle.redis_test.config;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String jwtSecret = generateKey();
    // private String jwtSecret = System.getenv("JWT_SECRET_KEY"); // Avoid
    // hardcoding!
    private long jwtExpirationDate = 3600000; // 1h = 3600s and 3600*1000 = 3600000 milliseconds

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token =
                Jwts.builder()
                        .subject(username)
                        .issuedAt(currentDate)
                        .expiration(expireDate)
                        .signWith(getDecodedKey())
                        .compact();

        return token;
    }

    private Key getDecodedKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // extract username from JWT token
    public String getUsername(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getDecodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        Jwts.parser().verifyWith((SecretKey) getDecodedKey()).build().parse(token);
        return true;
    }

    public boolean validateToken(String token, String username) {
        return username.equals(getUsername(token)) && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        if (isTokenExpired(token)) {
            throw new IllegalArgumentException("Token has expired");
        }

        String username = getUsername(token);
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(getDecodedKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date expiration =
                Jwts.parser()
                        .verifyWith((SecretKey) getDecodedKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getExpiration();
        return expiration.before(new Date());
    }

    // Remove while deploying in production
    public String generateKey() {
        Key key = Jwts.SIG.HS256.key().build();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        logger.info("Generated Key: {}", encodedKey);
        return encodedKey;
    }

    public void invalidateToken(String token) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invalidateToken'");
    }
}
