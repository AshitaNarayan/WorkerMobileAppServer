package com.workerewebapp.controllers;

import java.util.List;

import org.json.JSONObject;
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
public class WorkerRequestForMatchingJobs {

	@Autowired
	private CustomerNewWorkServiceInterface service;

	@RequestMapping(value = "/workerGetMatchJobs", method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:8102")
	public @ResponseBody List<NewCustomerWork> customerAddWork(@RequestBody String data) {

		System.out.println("Worker requesting for work from mobile");
		System.out.println(data);
		List<NewCustomerWork> matchingJobsForWorker = service.matchJobWithWorkerWorkRequest(data);

		return matchingJobsForWorker;

	}

}
