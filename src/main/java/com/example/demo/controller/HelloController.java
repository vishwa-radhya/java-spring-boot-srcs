package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // @Value("${app.name}")
    private String appName;

    // @Value("${app.environment}")
    private String environment;

    @GetMapping("/hello")
    public String hello() {
        return "Application: " + appName +
            " | Environment: "+environment;
    }
}