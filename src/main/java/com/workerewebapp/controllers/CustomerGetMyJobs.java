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
import com.workerewebapp.app.data.MyJobDetail;
import com.workerewebapp.service.WorkFinderService;

@RestController
public class CustomerGetMyJobs {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/customerGetJobsIntAndAccept", method = RequestMethod.POST)
	@CrossOrigin
	public @ResponseBody List<MyJobDetail> workerGetInterestedAndAcceptedJobs(@RequestBody String data) {

		System.out.println("Customer gets interested and Accepted jobs from mobile");
		System.out.println(data);
		List<MyJobDetail> interestedAndAcceptedJobsList = service.getInterestedJobsListForCustomer(data);
		return interestedAndAcceptedJobsList;

	}

}
