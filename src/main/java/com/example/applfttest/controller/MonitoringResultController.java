package com.example.applfttest.controller;

import com.example.applfttest.dto.MonitoredEndpointDto;
import com.example.applfttest.service.MonitoringService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("monitoring-result")
public class MonitoringResultController {
    private final MonitoringService service;


    public MonitoringResultController(MonitoringService service) {
        this.service = service;
    }

    @GetMapping
    public List<MonitoredEndpointDto> getResults(Authentication authentication){
        long userId = (long) authentication.getPrincipal();

        return service.getMonitoringResults(userId);
    }
}
