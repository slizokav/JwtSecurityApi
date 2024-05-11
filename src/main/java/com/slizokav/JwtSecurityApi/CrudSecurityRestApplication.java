package com.slizokav.JwtSecurityApi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrudSecurityRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSecurityRestApplication.class, args);
	}
	@Bean
	public ModelMapper ModelMapper() {
		return new ModelMapper();
	}

}
