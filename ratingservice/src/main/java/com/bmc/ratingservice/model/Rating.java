package com.bmc.ratingservice.model;

import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@org.springframework.data.mongodb.core.mapping.Document(collection = "Rating")
public class Rating {
    @Id
    private String _id;
    private String doctorId;
    private Integer rating;

    public Rating(String id, String doctorId, Integer rating) {
        this._id = id;
        this.doctorId = doctorId;
        this.rating = rating;
    }

    public Rating() {

    }

    public Document createDBObject() {
        Document rating = new Document();
        rating.append("_id", UUID.randomUUID().toString());
        rating.append("doctorId",this.getDoctorId());
        rating.append("rating", this.getRating());

        return rating;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "_id='" + _id + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
