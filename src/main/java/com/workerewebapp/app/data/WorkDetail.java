package com.workerewebapp.app.data;

public class WorkDetail {

	String customerName;

	String workerName;

	// For every work entry made in the system
	String jobDescription;
	String location;
	String payOut;
	String workId;
	String capability;

	int workRatedForWorker;
	int customerRatedforWork;

	int bidPlaced;
	String statusOfWork;

	public WorkDetail(String workId, String customerName, String jobDescription, String location, String payOut,
			String capability) {
		// When customer creates work these details are added
		this.workId = workId;
		this.customerName = customerName;
		this.jobDescription = jobDescription;
		this.location = location;
		this.payOut = payOut;
		this.capability = capability;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public int getWorkRatedForWorker() {
		return workRatedForWorker;
	}

	public void setWorkRatedForWorker(int workRatedForWorker) {
		this.workRatedForWorker = workRatedForWorker;
	}

	public int getCustomerRatedforWork() {
		return customerRatedforWork;
	}

	public void setCustomerRatedforWork(int customerRatedforWork) {
		this.customerRatedforWork = customerRatedforWork;
	}

	public int getBidPlaced() {
		return bidPlaced;
	}

	public void setBidPlaced(int bidPlaced) {
		this.bidPlaced = bidPlaced;
	}

	public String getStatusOfWork() {
		return statusOfWork;
	}

	public void setStatusOfWork(String statusOfWork) {
		this.statusOfWork = statusOfWork;
	}

	public String getCapability() {
		return capability;
	}

	public void setCapability(String capability) {
		this.capability = capability;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPayOut() {
		return payOut;
	}

	public void setPayOut(String payOut) {
		this.payOut = payOut;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String toString() {
		if (workId != null && jobDescription != null && payOut != null && customerName != null && location != null
				&& capability != null)
			return workId + ", " + jobDescription + ", " + payOut + ", " + customerName + ", " + location + ", "
					+ capability;
		return "";
	}

}
