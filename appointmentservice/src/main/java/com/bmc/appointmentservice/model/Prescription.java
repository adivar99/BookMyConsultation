package com.bmc.appointmentservice.model;

import org.bson.Document;

import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@org.springframework.data.mongodb.core.mapping.Document(collection = "Prescription")
public class Prescription {
    @Id
    private String _id;
    private String userId;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private String diagnosis;
    private List<Medicine> medicineList;

    public Prescription(String _id, String userId, String doctorId, String doctorName, String appointmentId, String diagnosis, List<Medicine> medicineList) {
        this._id = _id;
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.medicineList = medicineList;
    }

    public Prescription() {

    }

    public Document createDBObject() {
        Document prescription = new Document();
        prescription.append("_id", UUID.randomUUID().toString());
        prescription.append("userId",this.get_id());
        prescription.append("doctorId",this.getDoctorId());
        prescription.append("doctorName", this.getDoctorName());
        prescription.append("appointmentId", this.getAppointmentId());
        prescription.append("diagnosis", this.getDiagnosis());
        prescription.append("medicineList", this.getMedicineList());

        return prescription;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "_id='" + _id + '\'' +
                ", userId='" + userId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", medicineList=" + medicineList +
                '}';
    }
}

class Medicine {
    private String name;
    private String dosage;
    private String frequency;
    private String remarks;

    
    /**
     * @param name
     * @param dosage
     * @param frequency
     * @param remarks
     */
    public Medicine(String name, String dosage, String frequency, String remarks) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.remarks = remarks;
    }

    public Medicine(){

    }
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
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return "Medicine [dosage=" + dosage + ", frequency=" + frequency + ", name=" + name + ", remarks=" + remarks
                + "]";
    }

    
}
