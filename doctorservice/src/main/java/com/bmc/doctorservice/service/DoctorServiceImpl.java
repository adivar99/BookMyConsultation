package com.bmc.doctorservice.service;

import com.bmc.doctorservice.exceptions.ResourceNotFoundException;
import com.bmc.doctorservice.dao.DoctorDAO;
import com.bmc.doctorservice.enums.ErrorCodes;
import com.bmc.doctorservice.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    DoctorDAO doctorDao;

    @Override
    public Doctor acceptDoctorDetails(Doctor newDoctor) { return doctorDao.save(newDoctor); }

    @Override
    public Doctor getDoctorDetails(String id) throws ResourceNotFoundException{
        Optional<Doctor> doc =  doctorDao.findById(id);
        if (doc.isPresent()) {
            return doc.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Doctor updateDoctorDetails(String id, Doctor doctor) {
        Doctor savedDoctor = getDoctorDetails(id);

        savedDoctor.setFirstName(doctor.getFirstName());
        savedDoctor.setLastName(doctor.getLastName());
        savedDoctor.setSpecialty(doctor.getSpecialty());
        savedDoctor.setMobile(doctor.getMobile());
        savedDoctor.setEmailId(doctor.getEmailId());
        savedDoctor.setPan(doctor.getPan());
        savedDoctor.setApprovedBy(doctor.getApprovedBy());
        savedDoctor.setApproverComments(doctor.getApproverComments());
        savedDoctor.setDob(doctor.getDob());
        savedDoctor.setStatus(doctor.getStatus());
        savedDoctor.setRegistrationDate(doctor.getRegistrationDate());
        savedDoctor.setVerificationDate(doctor.getVerificationDate());

        return doctorDao.save(savedDoctor);

    }

    @Override
    public boolean deleteDoctor(String id) {
        Doctor savedDoctor = getDoctorDetails(id);
        if(savedDoctor == null) {
            return false;
        }
        doctorDao.delete(savedDoctor);
        return true;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

    @Override
    public Page<Doctor> getPaginatedDoctorDetails(Pageable pageRequest) {
        return doctorDao.findAll(pageRequest);
    }
}
