package com.bmc.appointmentservice.service;

import com.bmc.appointmentservice.exception.ResourceNotFoundException;
import com.bmc.appointmentservice.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PrescriptionService {
    Prescription acceptPrescriptionDetails(Prescription prescription);

    Prescription getPrescriptionDetails(String prescriptionId) throws ResourceNotFoundException;

    Prescription updatePrescriptionDetails(String id, Prescription prescription);

    boolean deletePrescription(String prescriptionId);

    List<Prescription> getAllPrescriptions();

    Page<Prescription> getPaginatedPrescriptions(Pageable pageRequest);
}
