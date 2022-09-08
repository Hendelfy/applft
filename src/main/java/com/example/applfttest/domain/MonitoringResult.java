package com.example.applfttest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MonitoringResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime dateOfCheck;
    Integer status;
    String payload;
    @ManyToOne(fetch = FetchType.LAZY)
    MonitoredEndpoint monitoredEndpoint;
}
