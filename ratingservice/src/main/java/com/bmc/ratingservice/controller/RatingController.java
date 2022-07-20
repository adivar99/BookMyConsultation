package com.bmc.ratingservice.controller;

import com.bmc.ratingservice.dto.RatingDTO;
import com.bmc.ratingservice.model.Rating;
import com.bmc.ratingservice.service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ratings")
public class RatingController {
    @Autowired
    RatingService ratingService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createRating(@RequestBody RatingDTO rating) {
        Rating newRating = modelMapper.map(rating, Rating.class);

        Rating savedRating = ratingService.acceptRatingDetails(newRating);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
