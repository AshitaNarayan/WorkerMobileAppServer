package com.workerewebapp.service;

import java.util.List;


import com.workerewebapp.app.data.NewCustomerWork;
import com.workerewebapp.app.data.WorkerJob;

public interface CustomerNewWorkServiceInterface {
	
	public List<NewCustomerWork> addNewWorkByCustomer(String data);

	List<WorkerJob> matchJobWithWorkerWorkRequest(String data);

	public void resetAllData();

	public void insertBulkData(String data);

	public List<NewCustomerWork> getWorkForCustomer(String data);

}
