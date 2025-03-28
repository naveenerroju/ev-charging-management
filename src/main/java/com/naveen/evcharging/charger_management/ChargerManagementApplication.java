package com.naveen.evcharging.charger_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChargerManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChargerManagementApplication.class, args);
	}

}
