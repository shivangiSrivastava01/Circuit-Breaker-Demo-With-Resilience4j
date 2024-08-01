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

        // Create an API endpoint at http://localhost:8000/home in a separate application and run it on port 8000 to ensure it’s accessible.
        // Once the successful scenario is tested, shut down the http://localhost:8000/home service.
        // Test http://localhost:9090/activity API again; the fallbackMethod should be triggered as the service will be unavailable.

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