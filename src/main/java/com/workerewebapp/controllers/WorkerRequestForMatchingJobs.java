package com.workerewebapp.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
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
public class WorkerRequestForMatchingJobs {

	@Autowired
	private WorkFinderService service;

	@RequestMapping(value = "/workerGetMatchJobs", method = RequestMethod.POST)
	@CrossOrigin
	public @ResponseBody List<MyJobDetail> matchJobsWorkerRequest(@RequestBody String data) {

		System.out.println("Worker requesting for work from mobile");
		System.out.println(data);

		List<MyJobDetail> matchingJobsForWorker = service.matchJobWithWorkerWorkRequest(data);
		return matchingJobsForWorker;

	}

}
