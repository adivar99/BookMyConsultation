package com.bmc.appointmentservice.model;

import com.bmc.appointmentservice.enums.AppointmentStatus;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Table;

@Table(name="appointment")
@Entity
public class Appointment {
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String appointment_id;
    private Date appointment_date;
    private Date created_date;
    private String doctor_id;
    private String prior_medical_history;
    private AppointmentStatus status;
    private String symptoms;
    private String time_slot;
    private String user_id;
    private String user_email_id;
    private String user_name;
    private String doctor_name;

    public Appointment(String appointment_id, Date appointment_date, Date created_date, String doctor_id, String prior_medical_history, AppointmentStatus status, String symptoms, String time_slot, String user_id, String user_email_id, String user_name, String doctor_name) {
        this.appointment_id = appointment_id;
        this.appointment_date = appointment_date;
        this.created_date = created_date;
        this.doctor_id = doctor_id;
        this.prior_medical_history = prior_medical_history;
        this.status = status;
        this.symptoms = symptoms;
        this.time_slot = time_slot;
        this.user_id = user_id;
        this.user_email_id = user_email_id;
        this.user_name = user_name;
        this.doctor_name = doctor_name;
    }

    public Appointment() {

    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Date getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(Date appointment_date) {
        this.appointment_date = appointment_date;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPrior_medical_history() {
        return prior_medical_history;
    }

    public void setPrior_medical_history(String prior_medical_history) {
        this.prior_medical_history = prior_medical_history;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public void setUser_email_id(String user_email_id) {
        this.user_email_id = user_email_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointment_id='" + appointment_id + '\'' +
                ", appointment_date=" + appointment_date +
                ", created_date=" + created_date +
                ", doctor_id='" + doctor_id + '\'' +
                ", prior_medical_history='" + prior_medical_history + '\'' +
                ", status='" + status + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", time_slot='" + time_slot + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_email_id='" + user_email_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", doctor_name='" + doctor_name + '\'' +
                '}';
    }
}
