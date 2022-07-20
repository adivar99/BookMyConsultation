package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.model.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AvailabilityService {
    Availability acceptAvailabilityDetails(Availability availability);

    Availability getAvailabilityDetails(Long id);

    Availability updateAvailabilityDetails(Long id, Availability availability);

    boolean deleteAvailability(Long id);

    List<Availability> getAllAvailability();

    Page<Availability> getPaginatedAvailability(Pageable pageRequest);

    List<Availability> getAvailabilityByDoctor_id(String doctorId);
}
