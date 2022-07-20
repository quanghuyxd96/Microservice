package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes().route("auth", r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("http://localhost:8086/"))
				.route("manager", r -> r.path("/manager/**").filters(f -> f.filter(filter)).uri("http://localhost:8081/"))
				.route("order", r -> r.path("/order/**").filters(f -> f.filter(filter)).uri("http://localhost:8083/"))
				.route("demo", r -> r.path("/demo/**").filters(f -> f.filter(filter)).uri("http://localhost:8090/")).build();
//				.route("echo", r -> r.path("/echo/**").filters(f -> f.filter(filter)).uri("lb://echo"))
//				.route("hello", r -> r.path("/hello/**").filters(f -> f.filter(filter)).uri("lb://hello")).build();
	}

}
