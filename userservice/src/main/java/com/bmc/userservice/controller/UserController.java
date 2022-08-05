package com.bmc.userservice.controller;

import com.bmc.userservice.dto.UserDTO;
import com.bmc.userservice.enums.ErrorCodes;
import com.bmc.userservice.exceptions.ErrorModel;
import com.bmc.userservice.model.User;
import com.bmc.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    /**
     * API #1
     * @param userDTO User details to save
     * @return saved User details
     */
    @PostMapping()
    public ResponseEntity createUser(@RequestBody UserDTO userDTO) {
        User newUser = modelMapper.map(userDTO, User.class);

        boolean validated = true;
        ArrayList<String> errorFields = new ArrayList<>();

        if(newUser.getFirstName().length() > 20){
            errorFields.add("firstName");
            validated = false;
        }
        if (newUser.getLastName().length() > 20) {
            errorFields.add("lastName");
            validated = false;
        }
        if (newUser.getMobile().length() != 10) {
            errorFields.add("Mobile");
            validated = false;
        }
        if (!newUser.getEmailId().matches("^(.+)@(.+)$")) {
            errorFields.add("Email Id");
            validated = false;
        }
        String dateRegex = "^(?:[0-9]{2})?[0-9]{2}-[0-3]?[0-9]-[0-3]?[0-9]$";
        String dateRegex1 = "^[0-3]?[0-9]-[0-3]?[0-9]-(?:[0-9]{2})?[0-9]{2}$";
        if (!newUser.getDob().matches(dateRegex)) {
            errorFields.add("Date of Birth");
            validated = false;
        }

        if (validated) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd=MM-yy");
            Date date = new Date();
            newUser.setCreatedDate(formatter.format(date));

            User savedUser = userService.acceptUserDetails(newUser);
            UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);

            // TODO: Implement notification function here

            return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
        } else {
            ErrorModel error = new ErrorModel(ErrorCodes.ERR_INVALID_INPUT, "Invalid Input. Parameter Name", errorFields);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * API #2
     * @param userId Id of user
     * @return User details
     */
    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable(name = "userId") String userId) {
        User savedUser = userService.getUserDetails(userId);
        if (savedUser == null){
            ErrorModel error = new ErrorModel(ErrorCodes.ERR_RESOURCE_NOT_FOUND, "Request Resource is not available", null);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.OK);
    }

    /**
     * API #3
     * @param userId Id of user
     * @param documents documents in some form to save
     * @return String of confirmation
     *
     * TODO: Implement the document saving part with AWS S3
     */
    @PostMapping("/{userId}/documents")
    public ResponseEntity saveUserDocuments(@PathVariable(name = "userId") String userId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * API #4
     * @param userId Id of user
     * @param documentName documents name to download
     * @return file/blob to download
     *
     * TODO: Implement the document loading part with AWS S3
     */
    @GetMapping("/{userId}/documents/{documentName}")
    public ResponseEntity downloadUserDocument(@PathVariable(name = "userId") String userId, @PathVariable(name = "documentName") String documentName) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }


    /**
     * 
     * @param userId
     * @return a list of document names uploaded
     */
    @GetMapping("/{userId}/documents/metadata")
    public ResponseEntity getUserDocuments(@PathVariable(name = "userId") String userId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }
}
