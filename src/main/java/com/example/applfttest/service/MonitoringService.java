package com.example.applfttest.service;

import com.example.applfttest.client.MonitoringClient;
import com.example.applfttest.domain.MonitoredEndpoint;
import com.example.applfttest.domain.MonitoringResult;
import com.example.applfttest.domain.User;
import com.example.applfttest.dto.CreateMonitoredEndpointDto;
import com.example.applfttest.dto.MonitoredEndpointDto;
import com.example.applfttest.dto.MonitoringResultDto;
import com.example.applfttest.dto.MonitoringResultResponseDto;
import com.example.applfttest.repository.MonitoredEndpointRepository;
import com.example.applfttest.repository.MonitoringResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringService {
    private final MonitoredEndpointRepository monitoredEndpointRepository;
    private final MonitoringResultRepository monitoringResultRepository;
    private final UserService userService;
    private final MonitoringEventService monitoringEventService;
    private final MonitoringClient client;

    private final int monitoringResultShowCount;


    public MonitoringService(MonitoredEndpointRepository monitoredEndpointRepository,
                             MonitoringResultRepository monitoringResultRepository,
                             UserService userService,
                             MonitoringEventService monitoringEventService,
                             MonitoringClient client,
                             @Value("${applifting.monitoring-result-show-count:10}") int monitoringResultShowCount) {
        this.monitoredEndpointRepository = monitoredEndpointRepository;
        this.monitoringResultRepository = monitoringResultRepository;
        this.userService = userService;
        this.monitoringEventService = monitoringEventService;
        this.client = client;
        this.monitoringResultShowCount = monitoringResultShowCount;

        fillScheduler();
    }

    public MonitoredEndpointDto createMonitoredEndpoint(long userId, CreateMonitoredEndpointDto dto) {
        User userReference = userService.getReferenceById(userId);

        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setUrl(dto.url());
        monitoredEndpoint.setOwner(userReference);
        monitoredEndpoint.setMonitoredInterval(dto.monitoredInterval());
        monitoredEndpoint.setName(dto.name());
        monitoredEndpoint.setDateOfCreation(LocalDateTime.now());

        monitoredEndpointRepository.save(monitoredEndpoint);

        createMonitoringEvent(monitoredEndpoint);
        return monitoredEndpointToDto(monitoredEndpoint);
    }


    public List<MonitoredEndpointDto> getMonitoredEndpoints(long userId) {
        List<MonitoredEndpoint> owner = monitoredEndpointRepository.findByOwner(userService.getReferenceById(userId));
        return owner.stream().map(this::monitoredEndpointToDto).toList();
    }

    public void removeMonitoredEndpoint(MonitoredEndpoint endpoint) {
        monitoredEndpointRepository.delete(endpoint);
        monitoringEventService.removeMonitoringEvent(endpoint.getId());
    }

    public Optional<MonitoredEndpoint> getMonitoredEndpoint(long id, long userId) {
        return monitoredEndpointRepository.findByIdAndOwner(id, userService.getReferenceById(userId));
    }

    public void editMonitoredEndpoint(MonitoredEndpoint monitoredEndpoint, CreateMonitoredEndpointDto dto) {
        monitoredEndpoint.setMonitoredInterval(dto.monitoredInterval());
        monitoredEndpoint.setUrl(dto.url());
        monitoredEndpoint.setName(dto.name());
        monitoredEndpointRepository.save(monitoredEndpoint);

        createMonitoringEvent(monitoredEndpoint);
    }

    public List<MonitoredEndpointDto> getMonitoringResults(long userId) {
        User owner = userService.getReferenceById(userId);
        List<MonitoredEndpoint> endpoints = monitoredEndpointRepository.findByOwner(owner);

        List<MonitoredEndpointDto> results = new ArrayList<>();

        for (MonitoredEndpoint endpoint : endpoints) {
            List<MonitoringResult> resultsForEndpoint =
                    monitoringResultRepository.findByEndpointWithLimitOrderedByChecked(endpoint, PageRequest.ofSize(monitoringResultShowCount));
            MonitoredEndpointDto monitoredEndpointDto = monitoredEndpointToDto(endpoint, resultsForEndpoint.stream()
                    .map(this::monitoringResultToDto).toList());
            results.add(monitoredEndpointDto);
        }

        return results;
    }

    private void monitor(MonitoredEndpoint endpoint) {
        LocalDateTime now = LocalDateTime.now();
        MonitoringResultResponseDto result = client.monitor(endpoint.getUrl());

        MonitoringResult monitoringResult = new MonitoringResult();
        monitoringResult.setMonitoredEndpoint(endpoint);
        monitoringResult.setPayload(result.getPayload());
        monitoringResult.setDateOfCheck(now);
        monitoringResult.setStatus(result.getCode());
        endpoint.setDateOfLastCheck(now);

        monitoredEndpointRepository.save(endpoint);
        monitoringResultRepository.save(monitoringResult);
    }

    private MonitoredEndpointDto monitoredEndpointToDto(MonitoredEndpoint source) {
        return monitoredEndpointToDto(source, null);
    }

    private MonitoredEndpointDto monitoredEndpointToDto(MonitoredEndpoint source, List<MonitoringResultDto> results) {
        return new MonitoredEndpointDto(source.getId(),
                source.getUrl(),
                source.getMonitoredInterval(),
                source.getDateOfLastCheck(),
                source.getDateOfCreation(),
                source.getName(),
                results);
    }

    private MonitoringResultDto monitoringResultToDto(MonitoringResult source) {
        return new MonitoringResultDto(source.getDateOfCheck(), source.getStatus(), source.getPayload());
    }


    private void fillScheduler() {
        monitoredEndpointRepository.findAll().forEach(this::createMonitoringEvent);
    }

    private void createMonitoringEvent(MonitoredEndpoint endpoint) {
        monitoringEventService.createMonitoringEvent(endpoint.getId(),
                endpoint.getMonitoredInterval(),
                () -> monitor(endpoint));
    }

}
