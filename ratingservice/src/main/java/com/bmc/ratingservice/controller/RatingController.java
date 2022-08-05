package com.bmc.ratingservice.controller;

import com.bmc.ratingservice.dto.RatingDTO;
import com.bmc.ratingservice.model.Rating;
import com.bmc.ratingservice.producer.KafkaMessageProducer;
import com.bmc.ratingservice.service.RatingService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @PostMapping
    public ResponseEntity createRating(@RequestBody RatingDTO rating) throws IOException {
        Rating newRating = modelMapper.map(rating, Rating.class);

        Rating savedRating = ratingService.acceptRatingDetails(newRating);

        String message = "The rating for doctor id["+savedRating.getDoctorId()+"]. Please find the details below: \n"+savedRating.toString();
        kafkaMessageProducer.publish("message", "doctorCreate", message);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<List<RatingDTO>> getRatingByDoctor(@PathVariable(name = "doctorId") String doctorId) {
        List<Rating> ratings= ratingService.getRatingsByDoctor(doctorId);
        List<RatingDTO> ratingsDTO = ratings.stream().map(rating -> modelMapper.map(rating, RatingDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(ratingsDTO, HttpStatus.OK);
    }
}
