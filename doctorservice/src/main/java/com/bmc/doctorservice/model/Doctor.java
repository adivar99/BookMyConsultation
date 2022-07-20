package com.bmc.doctorservice.model;

import com.bmc.doctorservice.enums.Specialty;
import com.bmc.doctorservice.enums.Status;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@org.springframework.data.mongodb.core.mapping.Document(collection = "Doctor")
public class Doctor {
    @Id
    private String _id;

    private String firstName;
    private String lastName;
    private Specialty specialty;
    private String dob;
    private String mobile;
    private String emailId;
    private String pan;
    private Status status;
    private String approvedBy;
    private String approverComments;
    private String registrationDate;
    private String verificationDate;

    public Doctor(String id, String firstName, String lastName, Specialty specialty, String dob, String mobile, String emailId, String pan, Status status, String approvedBy, String approverComments, String registrationDate, String verificationDate) {
        this._id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.dob = dob;
        this.mobile = mobile;
        this.emailId = emailId;
        this.pan = pan;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approverComments = approverComments;
        this.registrationDate = registrationDate;
        this.verificationDate = verificationDate;
    }

    public Doctor() {

    }

    public Document createDBObject(){
        Document doctor = new Document();
        doctor.append("_id", UUID.randomUUID().toString());
        doctor.append("firstName", this.getFirstName());
        doctor.append("lastName", this.getLastName());
        doctor.append("specialty", this.getSpecialty());
        doctor.append("dob", this.getDob());
        doctor.append("mobile", this.getMobile());
        doctor.append("emailId", this.getEmailId());
        doctor.append("pan", this.getPan());
        doctor.append("status", this.getStatus());
        doctor.append("approvedBy", this.getApprovedBy());
        doctor.append("approverComments", this.getApproverComments());
        doctor.append("registrationDate", this.getRegistrationDate());
        doctor.append("verificationDate", this.getVerificationDate());

        return doctor;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApproverComments() {
        return approverComments;
    }

    public void setApproverComments(String approverComments) {
        this.approverComments = approverComments;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "_id='" + _id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", dob='" + dob + '\'' +
                ", mobile='" + mobile + '\'' +
                ", emailId='" + emailId + '\'' +
                ", pan='" + pan + '\'' +
                ", status='" + status + '\'' +
                ", approvedBy='" + approvedBy + '\'' +
                ", approverComments='" + approverComments + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", verificationDate='" + verificationDate + '\'' +
                '}';
    }
}
