package com.bmc.ratingservice.service;

import com.bmc.ratingservice.exception.ResourceNotFoundException;
import com.bmc.ratingservice.dao.RatingDAO;

import com.bmc.ratingservice.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingDAO ratingDao;

    @Override
    public Rating acceptRatingDetails(Rating rating){ return ratingDao.save(rating); }

    @Override
    public Rating getRatingDetails(String id) {
        Optional<Rating> rating = ratingDao.findById(id);
        if (rating.isPresent())
            return rating.get();
        else
            throw new ResourceNotFoundException();
    }

    @Override
    public Rating updateRatingDetails(String id, Rating rating) {
        Rating savedRating = getRatingDetails(id);

        savedRating.setDoctorId(rating.getDoctorId());
        savedRating.setRating(rating.getRating());

        Rating updatedRating = ratingDao.save(savedRating);

        return updatedRating;
    }

    @Override
    public boolean deleteRating(String id) {
        Rating savedRating = getRatingDetails(id);
        if (savedRating == null)
            return false;
        ratingDao.delete(savedRating);
        return true;
    }

    @Override
    public List<Rating> getAllRating() { return ratingDao.findAll(); }

    @Override
    public Page<Rating> getPageableRating(Pageable pageRequest) { return ratingDao.findAll(pageRequest); }
}
