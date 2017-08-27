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
public class WorkerInterestUpdateJob {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/workerAddInterestInJob", method = RequestMethod.POST)
	@CrossOrigin
	public String workerInterestInJob(@RequestBody String data) {

		System.out.println("Worker shows interest in job from mobile");
		System.out.println(data);
		service.workerInterestInJob(data);
		return "";

	}

}
