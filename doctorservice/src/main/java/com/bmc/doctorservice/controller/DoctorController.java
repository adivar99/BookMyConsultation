package com.bmc.doctorservice.controller;

import com.bmc.doctorservice.exceptions.ResourceNotFoundException;
import com.bmc.doctorservice.dto.DoctorDTO;
import com.bmc.doctorservice.enums.ErrorCodes;
import com.bmc.doctorservice.enums.Status;
import com.bmc.doctorservice.model.Doctor;
import com.bmc.doctorservice.exceptions.ErrorModel;
import com.bmc.doctorservice.enums.Specialty;
import com.bmc.doctorservice.producer.KafkaMessageProducer;
import com.bmc.doctorservice.service.DoctorServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/doctors")
public class DoctorController {

    @Autowired
    DoctorServiceImpl doctorService;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    /**
     * API #1
     * 
     * @param doctorDTO doctor details
     * @return saved doctor details
     */
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor newDoctor = modelMapper.map(doctorDTO, Doctor.class);

        boolean validated = true;
        ArrayList<String> errorFields = new ArrayList<>();

        if (newDoctor.getFirstName().length() > 20) {
            errorFields.add("firstName");
            validated = false;
        }
        if (newDoctor.getLastName().length() > 20) {
            errorFields.add("lastName");
            validated = false;
        }
        if (newDoctor.getMobile().length() != 10) {
            errorFields.add("Mobile");
            validated = false;
        }
        if (!newDoctor.getEmailId().matches("^(.+)@(.+)$")) {
            errorFields.add("Email Id");
            validated = false;
        }
        if (!validateDate(newDoctor.getDob())) {
            errorFields.add("Date of Birth");
            validated = false;
        }
        if (!newDoctor.getPan().matches("^[a-zA-Z0-9]*$")) {
            errorFields.add("PAN");
            validated = false;
        }
        if (newDoctor.getSpecialty() == null) {
            newDoctor.setSpecialty(Specialty.GENERAL_PHYSICIAN);
        }

        if (validated) {
            newDoctor.setStatus(Status.PENDING);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
            Date date = new Date();
            newDoctor.setRegistrationDate(formatter.format(date));

            Doctor savedDoctor = doctorService.acceptDoctorDetails(newDoctor);
            DoctorDTO savedDoctorDTO = modelMapper.map(savedDoctor, DoctorDTO.class);

            // TODO: Implement notification function here

            return new ResponseEntity<>(savedDoctorDTO, HttpStatus.CREATED);
        } else {
            ErrorModel error = new ErrorModel(ErrorCodes.ERR_INVALID_INPUT, "Invalid Input. Parameter name",
                    errorFields);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * TODO: API #2
     * Implement submitting documents with AWS S3 bucket
     * 
     * @param doctorId  id for doctor's details
     * @param doctorDTO Object containing the doctor's data
     * @return
     */
    @PostMapping(value = "/{doctorId}/document", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity submitDoctor(@PathVariable(name = "doctorId") String doctorId,
            @RequestBody DoctorDTO doctorDTO) {
        Doctor newDoctor = modelMapper.map(doctorDTO, Doctor.class);
        return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * API #3
     *
     * @param doctorId Id of doctor to approve
     * @param doctorDTO approver details
     * @return Doctor details
     */
    @PutMapping(value="/{doctorId}/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity approveDoctor(@PathVariable(name = "doctorId") String doctorId, @RequestBody DoctorDTO doctorDTO) {
        try {
            doctorService.getDoctorDetails(doctorId);
        } catch(ResourceNotFoundException e) {
            ErrorModel errorModel = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Requested resource is not available", null);
            return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
        }
        Doctor newDoctor = modelMapper.map(doctorDTO, Doctor.class);
        System.out.println("Input Details: "+newDoctor);

        Doctor updateDoctor = doctorService.getDoctorDetails(doctorId);
        updateDoctor.setApprovedBy(newDoctor.getApprovedBy());
        updateDoctor.setApproverComments(newDoctor.getApproverComments());
        updateDoctor.setStatus(Status.ACTIVE);
        updateDoctor.setVerificationDate(getCurrentDate());

        System.out.println("Updated Doctor: "+updateDoctor);

        Doctor savedDoctor = doctorService.updateDoctorDetails(doctorId, updateDoctor);

        System.out.println("Saved Doctor: "+savedDoctor);
        DoctorDTO savedDoctorDTO = modelMapper.map(savedDoctor, DoctorDTO.class);

        // TODO: implement notification functions here

        return new ResponseEntity<>(savedDoctorDTO, HttpStatus.OK);
    }

    /**
     * API #4
     */
    @PutMapping(value = "/{doctorId}/reject", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity rejectDoctor(@PathVariable(name = "doctorId") String doctorId,
            @RequestBody DoctorDTO doctorDTO) {
        if (doctorService.getDoctorDetails(doctorId) == null) {
            ErrorModel errorModel = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND,
                    "Requested resource is not available", null);
            return new ResponseEntity<>(errorModel, HttpStatus.NOT_FOUND);
        }
        Doctor newDoctor = modelMapper.map(doctorDTO, Doctor.class);

        Doctor updateDoctor = doctorService.getDoctorDetails(doctorId);
        updateDoctor.setApprovedBy(newDoctor.getApprovedBy());
        updateDoctor.setApproverComments(newDoctor.getApproverComments());
        updateDoctor.setStatus(Status.REJECTED);
        updateDoctor.setVerificationDate(getCurrentDate());

        Doctor savedDoctor = doctorService.updateDoctorDetails(doctorId, updateDoctor);
        DoctorDTO savedDoctorDTO = modelMapper.map(savedDoctor, DoctorDTO.class);

        // TODO: implement notification functions here

        return new ResponseEntity<>(savedDoctorDTO, HttpStatus.OK);
    }

    /**
     * API #5
     *
     * @param status    status of doctors
     * @param specialty specialty of doctors
     * @return list of doctors with the above parameters
     */
    @GetMapping
    public ResponseEntity getDoctorsDetails(@RequestParam("status") Optional<Status> status,
            @RequestParam("specialty") Optional<Specialty> specialty) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Doctor> res = new ArrayList<Doctor>();

        for (Doctor doctor : doctors) {
            boolean flag = true;
            if (status.isPresent()) {
                if (doctor.getStatus() != status.get()) {
                    flag=false;
                }
            }
            if (specialty.isPresent()) {
                if (doctor.getSpecialty() != specialty.get()) {
                    flag=false;
                }
            }
            if (flag){
                res.add(doctor);
            }
        }

        List<DoctorDTO> doctorDTOs = res.stream().map(doc -> modelMapper.map(doc, DoctorDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
    }

    /**
     * API #6
     * 
     * @param doctorId Doctor's Id
     * @return ResponseEntity<Doctor>
     */
    @GetMapping(value = "/{doctorId}")
    public ResponseEntity getDoctorDetails(@PathVariable(name = "doctorId") String doctorId) {
        // ADMIN, USER
        Doctor savedDoctor = doctorService.getDoctorDetails(doctorId);
        DoctorDTO savedDoctorDTO = modelMapper.map(savedDoctor, DoctorDTO.class);
        return new ResponseEntity<>(savedDoctorDTO, HttpStatus.OK);
    }

    /**
     * TODO: API #7
     * 
     * @param doctorId Doctor's Id
     * @return list of documents somehow
     */
    @GetMapping(value = "{doctorId}/documents/metadata")
    public ResponseEntity getDoctorDocuments(@PathVariable(name = "doctorId") String doctorId) {
        return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * TODO: API #8
     * 
     * @param doctorId     Doctor's Id
     * @param documentName name of document to download
     * @return downloadable file/blob
     */
    @GetMapping(value = "{doctorId}/documents/{documentName}")
    public ResponseEntity getDoctorDownload(@PathVariable(name = "doctorId") String doctorId,
            @PathVariable(name = "documentName") String documentName) {
        return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);
    }

    public boolean validateDate(String date) {
        return date.matches("^[0-3]?[0-9]-[0-3]?[0-9]-(?:[0-9]{2})?[0-9]{2}$");
    }

    public String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        return formatter.format(date);
    }

}
