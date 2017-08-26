package com.workerewebapp.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workerewebapp.app.data.NewCustomerWork;
import com.workerewebapp.service.CustomerNewWorkServiceInterface;

@RestController
public class CustomerAddWork {

	@Autowired
	private CustomerNewWorkServiceInterface service;

	@RequestMapping(value = "/customerAddWork", method = RequestMethod.POST)
	//@CrossOrigin(origins = "http://localhost:8100")
	//@CrossOrigin(origins = "file://")
	@CrossOrigin
	public @ResponseBody List<NewCustomerWork> customerAddWork(@RequestBody String data, HttpServletResponse response) {

		System.out.println("Customer added work from mobile");
		System.out.println(data);
		List<NewCustomerWork> addedJobsByWorker = service.addNewWorkByCustomer(data);
		return addedJobsByWorker;

	}

}
