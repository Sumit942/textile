package com.example.textile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TextileApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextileApplication.class, args);
	}

}
