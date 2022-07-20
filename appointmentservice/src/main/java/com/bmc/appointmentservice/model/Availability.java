package com.bmc.appointmentservice.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "availability")
@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date availability_date;
    private String doctor_id;
    private Boolean is_booked;
    private String time_slot;

    public Availability(Long id, Date availability_data, String doctor_id, Boolean is_booked, String time_slot) {
        this.id = id;
        this.availability_date = availability_data;
        this.doctor_id = doctor_id;
        this.is_booked = is_booked;
        this.time_slot = time_slot;
    }

    public Availability() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAvailability_date() {
        return availability_date;
    }

    public void setAvailability_date(Date availability_date) {
        this.availability_date = availability_date;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Boolean getIs_booked() {
        return is_booked;
    }

    public void setIs_booked(Boolean is_booked) {
        this.is_booked = is_booked;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }
}
