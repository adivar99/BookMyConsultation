package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.dao.PrescriptionDAO;
import com.bmc.appointmentservice.exception.ResourceNotFoundException;
import com.bmc.appointmentservice.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    PrescriptionDAO prescriptionDAO;

    @Override
    public Prescription acceptPrescriptionDetails(Prescription prescription) { return prescriptionDAO.save(prescription); }

    @Override
    public Prescription getPrescriptionDetails(String prescriptionId) throws ResourceNotFoundException {
        Optional<Prescription> pres = prescriptionDAO.findById(prescriptionId);
        if(pres.isPresent()) {
            return pres.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Prescription updatePrescriptionDetails(String id, Prescription prescription) {
        Prescription savedPrescription = getPrescriptionDetails(id);

        savedPrescription.setAppointmentId(prescription.getAppointmentId());
        savedPrescription.setDoctorId(prescription.getDoctorId());
        savedPrescription.setUserId(prescription.getUserId());
        savedPrescription.setDoctorName(prescription.getDoctorName());
        savedPrescription.setMedicineList(prescription.getMedicineList());

        return prescriptionDAO.save(savedPrescription);
    }

    @Override
    public boolean deletePrescription(String prescriptionId) {
        Prescription savedPrescription = getPrescriptionDetails(prescriptionId);
        if(savedPrescription == null){
            return false;
        }
        prescriptionDAO.delete(savedPrescription);
        return true;
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionDAO.findAll();
    }

    @Override
    public Page<Prescription> getPaginatedPrescriptions(Pageable pageRequest) {
        return prescriptionDAO.findAll(pageRequest);
    }
}
