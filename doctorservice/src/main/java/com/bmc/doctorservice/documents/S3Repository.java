package com.bmc.doctorservice.documents;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class S3Repository {

    private AmazonS3 s3Client;
    private String BUCKET_NAME = "bmc-bucket-doctor";// This needs to be a unique bucket name across all the regions.

    @Autowired
    ObjectMetadata metadata;

    // TODO: Remove keys before submitting
    // access key: AKIAYCA356BJ73DJMZUM
    // secret key: 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

    @PostConstruct
    public void init(){
        String accessKey = "AKIAYCA356BJ73DJMZUM";
        String secretKey = "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";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        s3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_1)
            .build();
    }

    public void uploadFiles(String doctorId, MultipartFile file) throws IOException {
        String key = doctorId + "/"+ file.getOriginalFilename();
        if(!s3Client.doesBucketExistV2(BUCKET_NAME)){
            s3Client.createBucket(BUCKET_NAME);
        }
        s3Client.putObject(BUCKET_NAME,key,file.getInputStream(),metadata);
    }

    public List<String> getFileNames(String doctorId) {
        List<String> res = new ArrayList<>();
        S3Objects.inBucket(s3Client, BUCKET_NAME).forEach((S3ObjectSummary objectSummary) -> {
            String name = objectSummary.getKey();
            if (name.split("/",0)[0] == doctorId) {
                res.add(name.split("/",0)[1]);
            }
        });
        return res;
    }

    public S3Object getFile(String filename) {
        return s3Client.getObject(new GetObjectRequest(BUCKET_NAME, filename));
    }

}
