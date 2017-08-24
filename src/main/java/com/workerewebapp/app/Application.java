package com.workerewebapp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.workerewebapp.*")
//@EnableAutoConfiguration
public class Application {

	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}

}
