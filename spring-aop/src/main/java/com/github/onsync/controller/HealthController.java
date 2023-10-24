package com.github.onsync.controller;

import com.github.onsync.aspect.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @LogExecutionTime
    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {

        return ResponseEntity.ok("ok");
    }
}
