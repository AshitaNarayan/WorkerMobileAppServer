package com.workerewebapp.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldService {

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:8102")
	public String getLocalCust() {
		return "I am from 'getLocalCust' method";
	}

}