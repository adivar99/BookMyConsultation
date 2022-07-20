package com.bmc.userservice.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@org.springframework.data.mongodb.core.mapping.Document(collection = "User")
public class User {
    @Id
    private String _id;

    private String firstName;
    private String lastName;
    private String dob;
    private String mobile;
    private String emailId;
    private String createdDate;

    public User(String _id, String firstName, String lastName, String dob, String mobile, String emailId, String createdDate) {
        this._id = _id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.mobile = mobile;
        this.emailId = emailId;
        this.createdDate = createdDate;
    }

    public User () {

    }

    public Document createDBObject(){
        Document user = new Document();
        user.append("_id", UUID.randomUUID().toString());
        user.append("firstName", this.getFirstName());
        user.append("lastName", this.getLastName());
        user.append("dob", this.getDob());
        user.append("mobile", this.getMobile());
        user.append("emailId", this.getEmailId());
        user.append("createdDate", this.getCreatedDate());

        return user;
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

    public void setlastName(String lastName) {
        this.lastName = lastName;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", mobile='" + mobile + '\'' +
                ", emailId='" + emailId + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
