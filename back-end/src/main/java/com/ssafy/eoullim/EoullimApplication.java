package com.ssafy.eoullim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class EoullimApplication {

	public static void main(String[] args) {
		SpringApplication.run(EoullimApplication.class, args);
	}

}
