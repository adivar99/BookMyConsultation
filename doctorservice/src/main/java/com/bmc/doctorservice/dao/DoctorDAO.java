package com.bmc.doctorservice.dao;

import com.bmc.doctorservice.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorDAO extends MongoRepository<Doctor, String> {
}
