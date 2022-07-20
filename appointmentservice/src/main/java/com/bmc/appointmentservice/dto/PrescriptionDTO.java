package com.bmc.appointmentservice.dto;

import java.util.List;

public class PrescriptionDTO {
    private String _id;
    private String userId;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private String diagnosis;
    private List<Medicine> medicineList;
    /**
     * @return the _id
     */
    public String get_id() {
        return _id;
    }
    /**
     * @param _id the _id to set
     */
    public void set_id(String _id) {
        this._id = _id;
    }
    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * @return the doctorId
     */
    public String getDoctorId() {
        return doctorId;
    }
    /**
     * @param doctorId the doctorId to set
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }
    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    /**
     * @return the appointmentId
     */
    public String getAppointmentId() {
        return appointmentId;
    }
    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }
    /**
     * @param diagnosis the diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    /**
     * @return the medicineList
     */
    public List<Medicine> getMedicineList() {
        return medicineList;
    }
    /**
     * @param medicineList the medicineList to set
     */
    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    
}

class Medicine {
    private String name;
    private String dosage;
    private String frequency;
    private String remarks;
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the dosage
     */
    public String getDosage() {
        return dosage;
    }
    /**
     * @param dosage the dosage to set
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }
    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }
    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
}