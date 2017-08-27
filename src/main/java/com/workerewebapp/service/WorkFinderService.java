package com.workerewebapp.service;

import java.util.List;


import com.workerewebapp.app.data.WorkDetail;
import com.workerewebapp.app.data.MyJobDetail;

public interface WorkFinderService {
	
	public List<WorkDetail> addNewWorkByCustomer(String data);

	List<MyJobDetail> matchJobWithWorkerWorkRequest(String data);

	public void resetAllData();

	public void insertBulkData(String data);

	public List<WorkDetail> getWorkForCustomer(String data);
	
	public void workerInterestInJob(String data);
	
	public List<MyJobDetail> getInterestedAndAcceptedJobsListForWorker(String data);
	
	public List<MyJobDetail> getInterestedJobsListForCustomer(String data);
	
	public void customerAcceptsJob(String data);

	public void workerCompleteJob(String data);

	public void customerRatesWorker(String data);

}
