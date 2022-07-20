package com.bmc.appointmentservice.dao;

import com.bmc.appointmentservice.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionDAO extends MongoRepository<Prescription, String> {
}
