package com.bmc.paymentservice.controller;

import com.bmc.paymentservice.dto.AppointmentDTO;
import com.bmc.paymentservice.enums.AppointmentStatus;
import com.bmc.paymentservice.exceptions.ResourceNotFoundException;
import com.bmc.paymentservice.producer.KafkaMessageProducer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @PostMapping()
    public ResponseEntity<Map<String, String>> confirmPayment(
            @RequestParam(name = "appointmentId") String appointmentId) throws IOException {
        AppointmentDTO savedAppointmentDTO = restTemplate
                .getForObject("http://localhost:8083/appointments/" + appointmentId, AppointmentDTO.class);

        if (savedAppointmentDTO == null) {
            throw new ResourceNotFoundException();
        }

        Date now = new Date();

        savedAppointmentDTO.setCreated_date(now);
        savedAppointmentDTO.setStatus(AppointmentStatus.CONFIRMED);

        Map<String, String> params = new HashMap<>();
        params.put("appointmentId", appointmentId);
        String UriForUpdatingAppointment = "http://localhost:8083/appointment/{appointmentId}";

        restTemplate.put(UriForUpdatingAppointment, savedAppointmentDTO, params);

        // TODO: Implement kafka here

        Map<String, String> ret = new HashMap<>();
        // ret.put("id", )
        ret.put("appointmentId", appointmentId);
        ret.put("createdDate", now.toString());

        String message = "Payment is complete for appointment ["+appointmentId+"]. Please find the details below: \n"+ret.toString();
        kafkaMessageProducer.publish("message", "paymentCreate", message);

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
