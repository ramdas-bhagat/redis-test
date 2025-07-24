package com.skyeagle.redis_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PublicController handles public API endpoints. It provides a welcome message for the Redis Test
 * Application.
 */
@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @GetMapping("/getWelcomeMessage")
    public String getWelcomeMessage() {
        return "Welcome to the Redis Test Application!";
    }
}
