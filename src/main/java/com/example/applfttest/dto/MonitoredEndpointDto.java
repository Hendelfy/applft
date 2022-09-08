package com.example.applfttest.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MonitoredEndpointDto(
        long id,
        String url,
        int monitoredInterval,
        LocalDateTime dateOfLastCheck,
        LocalDateTime dateOfCreation,
        String name,

        List<MonitoringResultDto> results
) {
}
