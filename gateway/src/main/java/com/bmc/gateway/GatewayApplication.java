package com.bmc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(d -> d
						.path("/doctors*")
						.uri("http://localhost:8081"))
				.route(d -> d
						.path("/user*")
						.uri("http://localhost:8082"))
				.route(d -> d
						.path("*/availability")
						.uri("http://localhost:8083"))
				.route(d -> d
						.path("*/appointments*")
						.uri("http://localhost:8081"))
				.route(d -> d
						.path("/payments")
						.uri("http://localhost:8084"))
				.route(d -> d
						.path("/ratings")
						.uri("http://localhost:8085"))
				.build();
	}
}
