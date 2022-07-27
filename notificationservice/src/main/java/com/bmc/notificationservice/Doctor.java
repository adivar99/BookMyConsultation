package com.bmc.notificationservice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Doctor {
    private String firstName;
    private String lastName;
    private String specialty;
    private String dob;
    private String mobile;
    private String emailId;
    private String pan;
    private String status;
    private String approvedBy;
    private String approverComments;
    private String registrationDate;
    private String verificationDate;
}