package com.bmc.paymentservice.controller;

import com.bmc.paymentservice.dto.AppointmentDTO;
import com.bmc.paymentservice.enums.AppointmentStatus;
import com.bmc.paymentservice.exceptions.ResourceNotFoundException;
import com.bmc.paymentservice.producer.KafkaMessageProducer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    @Value("${url.service.instance}")
    private String instanceIp;

    @PostMapping()
    public ResponseEntity<Map<String, String>> confirmPayment(
            @RequestHeader HttpHeaders headers,
            @RequestParam(name = "appointmentId") String appointmentId) throws IOException, URISyntaxException {
        String accessToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        HttpHeaders send_headers = new HttpHeaders();
        send_headers.setContentType(MediaType.APPLICATION_JSON);
        send_headers.set("Authorization", "Bearer " + accessToken);
        String getUrl = "http://" + instanceIp + ":8083/appointments/" + appointmentId;
        // AppointmentDTO savedAppointmentDTO = restTemplate.getForObject("http://" + instanceIp + ":8083/appointments/" + appointmentId, AppointmentDTO.class);
        AppointmentDTO savedAppointmentDTO = restTemplate.exchange(RequestEntity.get(new URI(getUrl)).headers(send_headers).build(), AppointmentDTO.class).getBody();

        if (savedAppointmentDTO == null) {
            throw new ResourceNotFoundException();
        }

        Date now = new Date();

        savedAppointmentDTO.setCreated_date(now);
        savedAppointmentDTO.setStatus(AppointmentStatus.CONFIRMED);

        Map<String, String> params = new HashMap<>();
        params.put("appointmentId", appointmentId);
        String UriForUpdatingAppointment = "http://" + instanceIp + ":8083/appointment/"+appointmentId;

        // restTemplate.put(UriForUpdatingAppointment, savedAppointmentDTO, params);
        AppointmentDTO savedAppointment = restTemplate.exchange(RequestEntity.put(new URI(UriForUpdatingAppointment)).headers(send_headers).build(), AppointmentDTO.class).getBody();

        HttpEntity<String> entity = new HttpEntity(savedAppointmentDTO, send_headers);
        restTemplate.exchange(UriForUpdatingAppointment, HttpMethod.PUT, entity, AppointmentDTO.class);

        Map<String, String> ret = new HashMap<>();
        // ret.put("id", )
        ret.put("appointmentId", appointmentId);
        ret.put("createdDate", now.toString());

        String message = "Payment is complete for appointment [" + appointmentId
                + "]. Please find the details below: \n" + ret.toString();
        kafkaMessageProducer.publish("message", "paymentCreate", message);

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
