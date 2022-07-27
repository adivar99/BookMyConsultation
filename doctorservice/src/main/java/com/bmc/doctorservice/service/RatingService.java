package com.bmc.doctorservice.service;

import com.bmc.doctorservice.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RatingService {
    public Rating acceptRatingDetails(Rating rating);

    public Rating getRatingDetails(String id);

    public List<Rating> getRatingsByDoctor(String doctorId);

    public Rating updateRatingDetails(String id, Rating rating);

    public boolean deleteRating(String id);

    public List<Rating> getAllRating();

    public Page<Rating> getPageableRating(Pageable pageRequest);
}
