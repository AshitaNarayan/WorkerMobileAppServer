package com.workerewebapp.service;

import java.util.List;


import com.workerewebapp.app.data.NewCustomerWork;

public interface CustomerNewWorkServiceInterface {
	
	public List<NewCustomerWork> addNewWorkByCustomer(String data);

	List<NewCustomerWork> matchJobWithWorkerWorkRequest(String data);

}
