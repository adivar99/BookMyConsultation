package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.dao.AppointmentDAO;
import com.bmc.appointmentservice.exception.ResourceNotFoundException;
import com.bmc.appointmentservice.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentDAO appointmentDAO;

    @Override
    public Appointment saveAppointmentDetails(Appointment appointment) {
        System.out.print("IN SAVE APP: ");
        System.out.println(appointment);
        return appointmentDAO.save(appointment);
    }

    @Override
    public Appointment getAppointmentDetails(String id) {
        Optional<Appointment> appointment = appointmentDAO.findById(id);
        if (appointment.isPresent()){
            return appointment.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Appointment updateAppointmentDetails(String id, Appointment appointment) {
        Appointment savedAppointment = getAppointmentDetails(id);

        savedAppointment.setAppointment_date(appointment.getAppointment_date());
        savedAppointment.setCreated_date(appointment.getCreated_date());
        savedAppointment.setPrior_medical_history(appointment.getPrior_medical_history());
        savedAppointment.setStatus(appointment.getStatus());
        savedAppointment.setSymptoms(appointment.getSymptoms());
        savedAppointment.setTime_slot(appointment.getTime_slot());
        savedAppointment.setUser_id(appointment.getUser_id());
        savedAppointment.setUser_email_id(appointment.getUser_email_id());
        savedAppointment.setUser_name(appointment.getUser_name());
        savedAppointment.setDoctor_id(appointment.getDoctor_id());
        savedAppointment.setDoctor_name(appointment.getDoctor_name());
        return appointmentDAO.save(savedAppointment);
    }

    @Override
    public boolean deleteAppointment(String id) {
        Appointment savedAppointment = getAppointmentDetails(id);
        if (savedAppointment == null)
            return false;
        appointmentDAO.delete(savedAppointment);
        return true;
    }

    @Override
    public List<Appointment> getAllAppointment() {
        return appointmentDAO.findAll();
    }

    @Override
    public Page<Appointment> getPaginatedAppointment(Pageable pageRequest) {
        return appointmentDAO.findAll(pageRequest);
    }

    @Override
    public List<Appointment> getAppointmentByUser(String userId) {
        List<Appointment> appointments = getAllAppointment();
        List<Appointment> res = new ArrayList<>();
        for (Appointment app : appointments) {
            if (Objects.equals(app.getUser_id(), userId)) {
                res.add(app);
            }
        }
        return res;
    }
}
