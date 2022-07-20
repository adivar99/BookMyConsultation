package com.bmc.doctorservice.exceptions;

import com.bmc.doctorservice.enums.ErrorCodes;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(ErrorCodes code, String message, ArrayList<String> fields) {
        HashMap<String,String> res = new HashMap<String, String>();
        res.put("errorCode",code.toString());
        res.put("errorMessage", message);
        res.put("errorFields",fields.toString());

//        super(String.valueOf(res));
    }
    public ResourceNotFoundException(ErrorCodes code, String message, ArrayList<String> errorFields, Throwable throwable) {
        super(message, throwable);
    }
}
