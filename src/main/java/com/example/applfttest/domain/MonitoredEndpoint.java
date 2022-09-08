package com.example.applfttest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MonitoredEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String url;
    Integer monitoredInterval;
    LocalDateTime dateOfLastCheck;
    LocalDateTime dateOfCreation;
    String name;
    @ManyToOne(fetch = FetchType.LAZY)
    User owner;
}
