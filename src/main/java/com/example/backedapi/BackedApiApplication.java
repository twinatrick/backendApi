package com.example.backedapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import  static com.example.backedapi.Timer.CheckTime.start;
@SpringBootApplication
@EnableCaching
public class BackedApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackedApiApplication.class, args);
		start();
	}

}
