package com.github.npawlenko.job.jobscraperservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.npawlenko.job.jobscraperservice.config.properties")
public class JobScraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobScraperApplication.class, args);
	}

}
