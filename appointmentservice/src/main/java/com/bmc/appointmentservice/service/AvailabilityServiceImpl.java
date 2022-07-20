package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.dao.AvailabilityDAO;
import com.bmc.appointmentservice.model.Availability;
import com.bmc.appointmentservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    AvailabilityDAO availabilityDAO;

    @Override
    public Availability acceptAvailabilityDetails(Availability availability) { return availabilityDAO.save(availability); }

    @Override
    public Availability getAvailabilityDetails(Long id) {
        Optional<Availability> savedAvailability = availabilityDAO.findById(id);
        if (savedAvailability.isPresent()) {
            return savedAvailability.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Availability updateAvailabilityDetails(Long id, Availability availability) {
        Availability savedAvailability = getAvailabilityDetails(id);

        savedAvailability.setDoctor_id(availability.getDoctor_id());
        savedAvailability.setTime_slot(availability.getTime_slot());
        savedAvailability.setIs_booked(availability.getIs_booked());
        savedAvailability.setAvailability_date(availability.getAvailability_date());
        return availabilityDAO.save(savedAvailability);
    }

    @Override
    public boolean deleteAvailability(Long id) {
        Availability savedAvailability = getAvailabilityDetails(id);
        if(savedAvailability == null) {
            return false;
        }
        availabilityDAO.delete(savedAvailability);
        return true;
    }

    @Override
    public List<Availability> getAllAvailability() {
        return availabilityDAO.findAll();
    }

    @Override
    public Page<Availability> getPaginatedAvailability(Pageable pageRequest) {
        return availabilityDAO.findAll(pageRequest);
    }

    @Override
    public List<Availability> getAvailabilityByDoctor_id(String doctorId) {
        List<Availability> Availabilities = getAllAvailability();
        List<Availability> res = new ArrayList<>();
        for (Availability avi :
                Availabilities) {
            if (Objects.equals(avi.getDoctor_id(), doctorId)) {
                res.add(avi);
            }
        }
        return res;
    }
}
