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

import com.workerewebapp.app.data.WorkDetail;
import com.workerewebapp.service.WorkFinderService;

@RestController
public class CustomerRatesWorker {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/customerRateWorker", method = RequestMethod.POST)
	@CrossOrigin
	public String customerRatesWorker(@RequestBody String data) {

		System.out.println("Customer send rate worker from mobile");
		System.out.println(data);
		service.customerRatesWorker(data);
		return "";

	}

}
