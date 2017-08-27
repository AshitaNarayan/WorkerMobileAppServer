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
public class CustomerGetWorkCreated {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/customerGetWork", method = RequestMethod.POST)
	@CrossOrigin
	public @ResponseBody List<WorkDetail> customerGetWork(@RequestBody String data, HttpServletResponse response) {

		System.out.println("Customer requested all work from mobile");
		System.out.println(data);
		List<WorkDetail> workForCustomer = service.getWorkForCustomer(data);
		return workForCustomer;

	}

}
