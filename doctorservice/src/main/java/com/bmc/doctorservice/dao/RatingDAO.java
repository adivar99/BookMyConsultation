package com.bmc.doctorservice.dao;

import com.bmc.doctorservice.model.Rating;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingDAO extends MongoRepository<Rating, String> {
    public List<Rating> findByDoctorId(String doctorId);
}
