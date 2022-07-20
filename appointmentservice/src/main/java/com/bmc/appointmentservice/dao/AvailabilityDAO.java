package com.bmc.appointmentservice.dao;

import com.bmc.appointmentservice.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityDAO extends JpaRepository<Availability, Long> {
}
