package com.example.applfttest.controller;

import com.example.applfttest.domain.MonitoredEndpoint;
import com.example.applfttest.dto.CreateMonitoredEndpointDto;
import com.example.applfttest.dto.MonitoredEndpointDto;
import com.example.applfttest.service.MonitoringService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("monitored-endpoint")
public class MonitoredEndpointController {
    private final MonitoringService monitoringService;

    public MonitoredEndpointController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @PostMapping
    public MonitoredEndpointDto createMonitoredEndpoint(@Validated @RequestBody CreateMonitoredEndpointDto dto, Authentication authentication) {
        long userId = (long) authentication.getPrincipal();
        return monitoringService.createMonitoredEndpoint(userId, dto);
    }

    @GetMapping
    public List<MonitoredEndpointDto> getMonitoredEndpoints(Authentication authentication) {
        long userId = (long) authentication.getPrincipal();
        return monitoringService.getMonitoredEndpoints(userId);
    }

    @DeleteMapping("{id}")
    public void deleteMonitoredEndpoint(@PathVariable long id, Authentication authentication) {
        long userId = (long) authentication.getPrincipal();

        MonitoredEndpoint monitoredEndpoint = monitoringService.getMonitoredEndpoint(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        monitoringService.removeMonitoredEndpoint(monitoredEndpoint);
    }

    @PutMapping("{id}")
    public void editMonitoredEndpoint(@Validated @RequestBody CreateMonitoredEndpointDto dto,
                                      Authentication authentication,
                                      @PathVariable long id) {
        long userId = (long) authentication.getPrincipal();

        MonitoredEndpoint monitoredEndpoint = monitoringService.getMonitoredEndpoint(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        monitoringService.editMonitoredEndpoint(monitoredEndpoint, dto);
    }
}
