package com.example.applfttest.repository;


import com.example.applfttest.domain.MonitoredEndpoint;
import com.example.applfttest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
    List<MonitoredEndpoint> findByOwner(User owner);
    Optional<MonitoredEndpoint> findByIdAndOwner(long id, User owner);
}
