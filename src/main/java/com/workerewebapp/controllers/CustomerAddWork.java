package com.workerewebapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.workerewebapp.service.CustomerNewWorkServiceInterface;

@RestController
public class CustomerAddWork {

	@Autowired
	private CustomerNewWorkServiceInterface service;

	@RequestMapping(value = "/customerAddWork", method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:8102")
	public String customerAddWork(@RequestBody String data) {

		System.out.println("Customer added work from mobile");
		System.out.println(data);
		service.addNewWorkByCustomer(data);

		return "";

	}

}
