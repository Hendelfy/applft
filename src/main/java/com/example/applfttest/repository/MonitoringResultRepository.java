package com.example.applfttest.repository;

import com.example.applfttest.domain.MonitoredEndpoint;
import com.example.applfttest.domain.MonitoringResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {

    @Query("select mr from MonitoringResult mr where mr.monitoredEndpoint = :monitoredEndpoint order by mr.dateOfCheck desc")
    List<MonitoringResult> findByEndpointWithLimitOrderedByChecked(MonitoredEndpoint monitoredEndpoint, Pageable pageable);
}
