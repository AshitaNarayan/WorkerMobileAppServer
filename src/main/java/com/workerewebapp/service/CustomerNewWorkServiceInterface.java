package com.workerewebapp.service;

import java.util.List;

import org.json.JSONObject;

import com.workerewebapp.app.data.NewCustomerWork;

public interface CustomerNewWorkServiceInterface {
	
	public void addNewWorkByCustomer(String data);

	List<NewCustomerWork> matchJobWithWorkerWorkRequest(String data);

}
