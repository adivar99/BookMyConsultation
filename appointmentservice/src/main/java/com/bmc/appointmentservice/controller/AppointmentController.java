package com.bmc.appointmentservice.controller;

import com.bmc.appointmentservice.dto.AppointmentDTO;
import com.bmc.appointmentservice.dto.AvailabilityDTO;
import com.bmc.appointmentservice.dto.PrescriptionDTO;
import com.bmc.appointmentservice.dto.DoctorDTO;
import com.bmc.appointmentservice.dto.UserDTO;
import com.bmc.appointmentservice.enums.AppointmentStatus;
import com.bmc.appointmentservice.enums.ErrorCodes;
import com.bmc.appointmentservice.exception.ErrorModel;
import com.bmc.appointmentservice.exception.ResourceNotFoundException;
import com.bmc.appointmentservice.model.Appointment;
import com.bmc.appointmentservice.model.Availability;
import com.bmc.appointmentservice.model.Prescription;
import com.bmc.appointmentservice.service.AppointmentService;
import com.bmc.appointmentservice.service.AvailabilityService;
import com.bmc.appointmentservice.service.PrescriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    @Value("${url.service.doctor}")
    private String doctorServiceUrl;

    @Value("${url.service.doctor1}")
    private String doctorServiceUrl1;

    @PostMapping("/doctor/{doctorId}/availability")
    public ResponseEntity setAvailability(@PathVariable(value = "doctorId") String doctorId, @RequestBody Map<String, Map<String, List<String>>> availabilityMapDTO) throws ParseException {
        // Check and get doctor details
        DoctorDTO savedDoctor = restTemplate.getForObject(doctorServiceUrl+"/doctors/"+doctorId, DoctorDTO.class);

        if (savedDoctor == null) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Doctor with id ["+doctorId+"] not found", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
        // If availability already exists, delete it
         List<Availability> savedAvailabilities = availabilityService.getAvailabilityByDoctor_id(doctorId);
         for (Availability savedAvailability : savedAvailabilities) {
             availabilityService.deleteAvailability(savedAvailability.getId());
         }

        Map<String, List<String>> availabilities = availabilityMapDTO.get("availabilityMap");

        List<AvailabilityDTO> newAvailabilities = convertMapToAvailabilityDTO(availabilities, doctorId);

        for (AvailabilityDTO newAvailabilityDTO : newAvailabilities) {
            Availability newAvailability = modelMapper.map(newAvailabilityDTO, Availability.class);
            Availability savedAvailability = availabilityService.acceptAvailabilityDetails(newAvailability);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/doctor/{doctorId}/availability")
    public ResponseEntity getAvailability(@PathVariable(value = "doctorId") String doctorId) throws ParseException {
        // Check and get doctor details
        DoctorDTO savedDoctor = restTemplate.getForObject(doctorServiceUrl1+"/doctors/"+doctorId, DoctorDTO.class);

        if (savedDoctor == null) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Doctor with id ["+doctorId+"] not found", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        List<Availability> availabilities = availabilityService.getAvailabilityByDoctor_id(doctorId);

        Map ret = convertAvailabilitiesToMap(availabilities, doctorId);

        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @PostMapping("/appointments")
    public ResponseEntity createAppointment(@RequestBody AppointmentDTO appointmentDTO) throws ParseException {
        boolean found = false;
        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(appointmentDTO.getAppointment_date());
        Appointment newAppointment = modelMapper.map(appointmentDTO, Appointment.class);
        newAppointment.setAppointment_date(newDate);

        // Check and get doctor details
        DoctorDTO savedDoctor = restTemplate.getForObject("http://localhost:8081/doctors/"+newAppointment.getDoctor_id(), DoctorDTO.class);

        if (savedDoctor == null) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Doctor with id ["+newAppointment.getDoctor_id()+"] not found", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        // Check and get User details
        UserDTO savedUser = restTemplate.getForObject("http://localhost:8082/user/"+newAppointment.getUser_id(), UserDTO.class);

        if (savedUser == null) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "User with id ["+newAppointment.getUser_id()+"] not found", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        List<Availability> availabilities = availabilityService.getAvailabilityByDoctor_id(newAppointment.getDoctor_id());
        for(Availability avail: availabilities) {
            if ((compareDate(avail.getAvailability_date(), newAppointment.getAppointment_date())) && (avail.getTime_slot().equals(newAppointment.getTime_slot()))) {
                found = true;
            }
        }

        if (!found) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Availability with Date: "+newAppointment.getAppointment_date()+" and time: "+newAppointment.getTime_slot()+" not found", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
        
        Date now = new Date();
        newAppointment.setStatus(AppointmentStatus.PENDING_PAYMENT);
        newAppointment.setCreated_date(now);
        String DoctorName = savedDoctor.getFirstName() + " " + savedDoctor.getLastName();
        newAppointment.setDoctor_name(DoctorName);
        String UserName = savedUser.getFirstName() + " " + savedUser.getLastName();
        newAppointment.setUser_name(UserName);
        newAppointment.setUser_email_id(savedUser.getEmailId());

        Appointment savedAppointment = appointmentService.saveAppointmentDetails(newAppointment);

        // TODO: Implement kafka here

        return new ResponseEntity<String>(savedAppointment.getAppointment_id(), HttpStatus.OK);
    }

    @GetMapping(value = "/appointments/{appointmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable String appointmentId) {
        Appointment savedAppointment = appointmentService.getAppointmentDetails(appointmentId);

        AppointmentDTO savedAppointmentDTO = modelMapper.map(savedAppointment, AppointmentDTO.class);

        return new ResponseEntity<>(savedAppointmentDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByUser(@PathVariable String userId) {
        List<Appointment> appointments = appointmentService.getAppointmentByUser(userId);

        List<AppointmentDTO> res = new ArrayList<>();
        for (Appointment appointment: appointments) {
            res.add(modelMapper.map(appointment, AppointmentDTO.class));
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/prescriptions")
    public ResponseEntity createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {


        Prescription newPrescription = modelMapper.map(prescriptionDTO, Prescription.class);
        try {
            appointmentService.getAppointmentDetails(newPrescription.getAppointmentId());
        } catch (ResourceNotFoundException e) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Appointment with id ["+newPrescription.getAppointmentId()+"] not found.", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
        Appointment savedAppointment = appointmentService.getAppointmentDetails(newPrescription.getAppointmentId());
        if (savedAppointment.getStatus() == AppointmentStatus.PENDING_PAYMENT) {
            ErrorModel err = new ErrorModel(ErrorCodes.ERR_PAYMENT_PENDING, "Prescription cannot be issued since the payment status is pending.", null);
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }
        if (savedAppointment.getStatus() == AppointmentStatus.CONFIRMED) {
            prescriptionService.acceptPrescriptionDetails(newPrescription);

            // TODO: Implement kafka here
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping(value = "/appointment/{appointmentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable(name = "appointmentId") String appointmentId, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment newAppointment = modelMapper.map(appointmentDTO, Appointment.class);

        Appointment savedAppointment = appointmentService.getAppointmentDetails(appointmentId);
        if (savedAppointment == null) {
            throw new ResourceNotFoundException();
        }
        Appointment updatedAppointment = appointmentService.updateAppointmentDetails(appointmentId, newAppointment);

        AppointmentDTO updatedAppointmentDTO = modelMapper.map(updatedAppointment, AppointmentDTO.class);
        return new ResponseEntity<>(updatedAppointmentDTO, HttpStatus.OK);
    }


    public List<AvailabilityDTO> convertMapToAvailabilityDTO(Map<String, List<String>> availabilityMap, String doctorId) throws ParseException {
        List<AvailabilityDTO> res = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : availabilityMap.entrySet()) {
            for (String slot : entry.getValue()) {
                AvailabilityDTO availabilityEntry = new AvailabilityDTO();
                availabilityEntry.setDoctor_id(doctorId);
                Date aviDate = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getKey());
                availabilityEntry.setAvailability_date(aviDate);
                availabilityEntry.setTime_slot(slot);
                availabilityEntry.setIs_booked(false);
                res.add(availabilityEntry);
            }
        }
        return res;
    }

    public Map convertAvailabilitiesToMap(List<Availability> availabilities, String doctorId) {
        Map<String, List<String>> availabilityMap = new HashMap<>();
        for (Availability avail : availabilities) {
            String strDate = new SimpleDateFormat("yyyy-MM-dd").format(avail.getAvailability_date());

            List<String> newSlots = new ArrayList<>();
            if (availabilityMap.containsKey(strDate)) {
                List<String> slots = availabilityMap.remove(strDate);
                newSlots.addAll(slots);
            }
            newSlots.add(avail.getTime_slot());
            availabilityMap.put(strDate,newSlots);
        }
        Map ret = new HashMap();
        ret.put("doctorId", doctorId);
        ret.put("availabilityMap", availabilityMap);
        return ret;
    }

    public boolean compareDate(Date date1, Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate1 = formatter.format(date1);
        String strDate2 = formatter.format(date2);
        return strDate1.equals(strDate2);
    }

}
