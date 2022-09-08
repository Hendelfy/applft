package com.example.applfttest.client;

import com.example.applfttest.dto.MonitoringResultResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MonitoringClient {
    private final RestTemplate restTemplate;

    public MonitoringClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MonitoringResultResponseDto monitor(String url) {
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        return new MonitoringResultResponseDto(entity.getStatusCodeValue(), entity.getBody());
    }
}
