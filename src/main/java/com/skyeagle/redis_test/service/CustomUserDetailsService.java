package com.skyeagle.redis_test.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skyeagle.redis_test.model.SecurityUser;
import com.skyeagle.redis_test.repository.SecurityUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(CustomUserDetailsService.class);

    private PasswordEncoder passwordEncoder;

    private SecurityUserRepository securityUserRepository;

    public CustomUserDetailsService(
            PasswordEncoder passwordEncoder, SecurityUserRepository securityUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.securityUserRepository = securityUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser =
                securityUserRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                String.format(
                                                        "Username: %s not found.", username)));
        return User.builder()
                .username(securityUser.getUsername())
                .password(securityUser.getPassword())
                .authorities(
                        securityUser.getRoles().stream().map(SimpleGrantedAuthority::new).toList())
                .disabled(!securityUser.isActive())
                .build();
    }

    public SecurityUser addSecurityUser(SecurityUser securityUser) {
        logger.info("Adding new user: {}", securityUser);
        if (securityUserRepository.findByUsername(securityUser.getUsername()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Username: %s already exists.", securityUser.getUsername()));
        }
        securityUser.setPassword(passwordEncoder.encode(securityUser.getPassword()));
        securityUser.setActive(true);
        securityUser.setRoles(
                securityUser.getRoles() != null ? securityUser.getRoles() : Set.of("USER"));
        logger.info("Saving new user: {}", securityUser);
        return securityUserRepository.save(securityUser);
    }

    public SecurityUser updateSecurityUser(SecurityUser securityUser) {
        SecurityUser existingUser =
                securityUserRepository
                        .findByUsername(securityUser.getUsername())
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                String.format(
                                                        "Username: %s not found.",
                                                        securityUser.getUsername())));
        existingUser.setPassword(passwordEncoder.encode(securityUser.getPassword()));
        existingUser.setRoles(securityUser.getRoles());
        existingUser.setActive(securityUser.isActive());
        return securityUserRepository.save(existingUser);
    }

    public List<SecurityUser> getAllSecurityUsers() {
        logger.info("Fetching all security users");
        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        if (securityUsers.isEmpty()) {
            logger.warn("No security users found");
        } else {
            logger.info("Found {} security users", securityUsers.size());
        }
        return securityUsers;
    }
}
