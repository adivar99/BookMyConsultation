package com.bmc.doctorservice.service;

import com.bmc.doctorservice.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    public Doctor acceptDoctorDetails(Doctor doctor);

    public Doctor getDoctorDetails(String id);

    public Doctor updateDoctorDetails(String id, Doctor doctor);

    public boolean deleteDoctor(String id);

    public List<Doctor> getAllDoctors();

    public Page<Doctor> getPaginatedDoctorDetails(Pageable pageRequest);
}
