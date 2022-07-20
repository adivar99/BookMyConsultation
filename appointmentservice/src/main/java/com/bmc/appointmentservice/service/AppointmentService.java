package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {
    Appointment saveAppointmentDetails(Appointment appointment);

    Appointment getAppointmentDetails(String id);

    Appointment updateAppointmentDetails(String id, Appointment appointment);

    boolean deleteAppointment(String id);

    List<Appointment> getAllAppointment();

    Page<Appointment> getPaginatedAppointment(Pageable pageRequest);

    public List<Appointment> getAppointmentByUser(String userId);
}
