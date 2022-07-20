package com.bmc.doctorservice.exceptions;

import com.bmc.doctorservice.enums.ErrorCodes;

import java.util.ArrayList;

public class ErrorModel {
    private ErrorCodes errorCode;
    private String errorMessage;
    private ArrayList<String> errorFields;

    public ErrorModel(ErrorCodes errorCode, String errorMessage, ArrayList<String> errorFields) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorFields = errorFields;
    }

    public ArrayList<String> getErrorFields() {
        return errorFields;
    }

    public void setErrorFields(ArrayList<String> errorFields) {
        this.errorFields = errorFields;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
