package com.example.applfttest.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MonitoringResultDto(
        LocalDateTime dateOfCheck,
        int status,
        String payload
) {
}
