package com.bmc.ratingservice.dao;

import com.bmc.ratingservice.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingDAO extends MongoRepository<Rating, String> {
}
