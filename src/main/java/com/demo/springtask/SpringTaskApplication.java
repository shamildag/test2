package com.demo.springtask;

import com.demo.springtask.service.UserService;
import com.demo.springtask.storage.StorageProperties;
import com.demo.springtask.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTaskApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService, UserService userService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
			userService.init();

		};
	}
}
