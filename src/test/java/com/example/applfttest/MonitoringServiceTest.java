package com.example.applfttest;


import com.example.applfttest.client.MonitoringClient;
import com.example.applfttest.domain.MonitoredEndpoint;
import com.example.applfttest.domain.MonitoringResult;
import com.example.applfttest.domain.User;
import com.example.applfttest.dto.CreateMonitoredEndpointDto;
import com.example.applfttest.dto.MonitoredEndpointDto;
import com.example.applfttest.repository.MonitoredEndpointRepository;
import com.example.applfttest.repository.MonitoringResultRepository;
import com.example.applfttest.service.MonitoringEventService;
import com.example.applfttest.service.MonitoringService;
import com.example.applfttest.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class MonitoringServiceTest {

    @Mock
    MonitoredEndpointRepository monitoredEndpointRepository;
    @Mock
    MonitoringResultRepository monitoringResultRepository;
    @Mock
    UserService userService;
    @Mock
    MonitoringEventService monitoringEventService;
    @Mock
    MonitoringClient client;
    MonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        monitoringService = new MonitoringService(monitoredEndpointRepository,
                monitoringResultRepository,
                userService,
                monitoringEventService,
                client,
                10);
    }

    @Test
    public void testCreateMonitoredEndpoint() {
        long userId = 1L;
        CreateMonitoredEndpointDto dto = createDto();
        Mockito.when(userService.getReferenceById(userId)).thenReturn(new User());
        Mockito.when(monitoredEndpointRepository.save(any()))
                .thenAnswer(i -> {
                    MonitoredEndpoint e = i.getArgument(0);
                    e.setId(2L);
                    return e;
                });
        monitoringService.createMonitoredEndpoint(userId, dto);

        ArgumentCaptor<MonitoredEndpoint> endpointCaputre = ArgumentCaptor.forClass(MonitoredEndpoint.class);
        Mockito.verify(monitoredEndpointRepository).save(endpointCaputre.capture());
        MonitoredEndpoint endpoint = endpointCaputre.getValue();
        Mockito.verify(monitoringEventService)
                .createMonitoringEvent(eq(endpoint.getId()), eq(endpoint.getMonitoredInterval()), any(Runnable.class));


        Assertions.assertEquals(dto.monitoredInterval(), endpoint.getMonitoredInterval());
        Assertions.assertEquals(dto.name(), endpoint.getName());
        Assertions.assertEquals(dto.url(), endpoint.getUrl());

    }

    @Test
    public void testGetMonitoredEndpoints() {
        long userId = 2L;
        MonitoredEndpoint endpoint = getEndpoint(1);
        Mockito.when(monitoredEndpointRepository.findByOwner(any())).thenReturn(List.of(endpoint));
        List<MonitoredEndpointDto> monitoredEndpoints = monitoringService.getMonitoredEndpoints(userId);

        Mockito.verify(userService).getReferenceById(userId);

        Assertions.assertEquals(endpoint.getName(), monitoredEndpoints.get(0).name());
        Assertions.assertEquals(endpoint.getDateOfCreation(), monitoredEndpoints.get(0).dateOfCreation());
        Assertions.assertEquals(endpoint.getUrl(), monitoredEndpoints.get(0).url());
        Assertions.assertEquals(endpoint.getMonitoredInterval(), monitoredEndpoints.get(0).monitoredInterval());
        Assertions.assertEquals(endpoint.getDateOfLastCheck(), monitoredEndpoints.get(0).dateOfLastCheck());
        Assertions.assertEquals(endpoint.getId(), monitoredEndpoints.get(0).id());
    }

    @Test
    public void testRemoveMonitoredEndpoint() {
        MonitoredEndpoint endpoint = getEndpoint(1);
        monitoringService.removeMonitoredEndpoint(endpoint);
        Mockito.verify(monitoredEndpointRepository).delete(endpoint);
        Mockito.verify(monitoringEventService).removeMonitoringEvent(endpoint.getId());
    }

    @Test
    public void testEditMonitoredEndpoint() {
        MonitoredEndpoint endpoint = getEndpoint(1);
        CreateMonitoredEndpointDto dto = createDto();
        monitoringService.editMonitoredEndpoint(endpoint, dto);
        Mockito.verify(monitoringEventService)
                .createMonitoringEvent(eq(endpoint.getId()), eq(dto.monitoredInterval()), any(Runnable.class));
    }

    @Test
    public void testGetMonitoringResults() {
        long userId = 2L;
        User user = new User();
        user.setId(userId);

        MonitoredEndpoint endpoint1 = getEndpoint(1);
        MonitoredEndpoint endpoint2 = getEndpoint(2);

        MonitoringResult result1 = getResult(1);
        MonitoringResult result2 = getResult(2);
        MonitoringResult result3 = getResult(3);
        MonitoringResult result4 = getResult(4);

        Mockito.when(userService.getReferenceById(userId)).thenReturn(user);
        Mockito.when(monitoredEndpointRepository.findByOwner(user)).thenReturn(List.of(endpoint1, endpoint2));

        Mockito.when(monitoringResultRepository.findByEndpointWithLimitOrderedByChecked(eq(endpoint1), any()))
                .thenReturn(List.of(result1, result2, result3));
        Mockito.when(monitoringResultRepository.findByEndpointWithLimitOrderedByChecked(eq(endpoint2), any()))
                .thenReturn(Collections.singletonList(result4));

        List<MonitoredEndpointDto> results = monitoringService.getMonitoringResults(userId);

        Mockito.verify(monitoringResultRepository).findByEndpointWithLimitOrderedByChecked(eq(endpoint1), any());
        Mockito.verify(monitoringResultRepository).findByEndpointWithLimitOrderedByChecked(eq(endpoint2), any());

        Assertions.assertEquals(endpoint1.getId(), results.get(0).id());
        Assertions.assertEquals(3, results.get(0).results().size());


        Assertions.assertEquals(endpoint2.getId(), results.get(1).id());
        Assertions.assertEquals(1, results.get(1).results().size());
    }

    private CreateMonitoredEndpointDto createDto() {
        String url = "https://random-data-api.com/api/v2/banks";
        int intervalSec = 40;
        String name = "Random api";
        return new CreateMonitoredEndpointDto(url, intervalSec, name);
    }

    private MonitoredEndpoint getEndpoint(long id) {
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setId(id);
        monitoredEndpoint.setName("Endpoint name");
        monitoredEndpoint.setMonitoredInterval(45);
        monitoredEndpoint.setDateOfCreation(LocalDateTime.now());
        return monitoredEndpoint;
    }

    private MonitoringResult getResult(long id) {
        MonitoringResult monitoringResult = new MonitoringResult();
        monitoringResult.setId(id);
        monitoringResult.setStatus(200);
        monitoringResult.setPayload("payload");
        monitoringResult.setDateOfCheck(LocalDateTime.now());
        return monitoringResult;
    }
}
