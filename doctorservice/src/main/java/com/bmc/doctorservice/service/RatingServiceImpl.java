package com.bmc.doctorservice.service;

import com.bmc.doctorservice.exceptions.ResourceNotFoundException;
import com.bmc.doctorservice.dao.RatingDAO;

import com.bmc.doctorservice.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Rating> getRatingsByDoctor(String doctorId) {
        List<Rating> allRatings = getAllRating();
        List<Rating> res = new ArrayList<>();
        System.out.println("ALL Ratings:"+allRatings.toString());
        for (Rating rating : allRatings) {
            if(rating.getDoctorId() == doctorId) {
                res.add(rating);
            }
        }
        return res;
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
