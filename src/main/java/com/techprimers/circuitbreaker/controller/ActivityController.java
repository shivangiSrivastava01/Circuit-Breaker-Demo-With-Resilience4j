package com.techprimers.circuitbreaker.controller;

import com.techprimers.circuitbreaker.model.Activity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequestMapping("/activity")
@RestController
public class ActivityController {

    private RestTemplate restTemplate;

    private final String BORED_API = "http://localhost:8000/home";

    public ActivityController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name = "randomActivity", fallbackMethod = "fallbackRandomActivity")
    public String getRandomActivity() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(BORED_API, String.class);
        log.info("Activity received: " + responseEntity.getBody());
        return responseEntity.getBody();
    }

    public String fallbackRandomActivity(Throwable throwable) {
        return "FallBack method called";
    }


}