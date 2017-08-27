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
public class WorkerCompleteJob {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/workerCompleteJob", method = RequestMethod.POST)
	@CrossOrigin
	public String workerCompleteJob(@RequestBody String data) {

		System.out.println("Worker completed Job from mobile");
		System.out.println(data);
		service.workerCompleteJob(data);
		return "";

	}

}
