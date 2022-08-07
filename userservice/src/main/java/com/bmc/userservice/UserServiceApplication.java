package com.bmc.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.s3.model.ObjectMetadata;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() { return new ModelMapper(); }

	@Bean
	public RestTemplate getRestTemplate() { return new RestTemplate(); }

	@Bean
    ObjectMetadata objectMetadata() {
        return new ObjectMetadata();
    }

}
