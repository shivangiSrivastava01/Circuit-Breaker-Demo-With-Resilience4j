package com.demo.circuitbreaker.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequestMapping("/activity")
@RestController
public class ActivityController {

    private final RestTemplate restTemplate;

    public ActivityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    public String getRandomActivity() {
        String backToHomeAPI = "http://localhost:8000/home";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(backToHomeAPI, String.class);
        log.info("Activity received: {}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    public String fallbackRandomActivity(Throwable throwable) {
        log.error("Failed to fetch random activity: {}", throwable.getMessage());
        return "We’re having trouble finding an activity right now. Please try again later or check out other fun options!";
    }


}