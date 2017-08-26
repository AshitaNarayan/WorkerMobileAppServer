package com.workerewebapp.app.data;

public class NewCustomerWork {

	String customerName;
	// String customerRating;

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkRatedForWorker() {
		return workRatedForWorker;
	}

	public void setWorkRatedForWorker(String workRatedForWorker) {
		this.workRatedForWorker = workRatedForWorker;
	}

	public String getCustomerRatedforWork() {
		return customerRatedforWork;
	}

	public void setCustomerRatedforWork(String customerRatedforWork) {
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

	String workerName;
	// String workerRating;

	// For every customer he creates a list of work
	String jobDescription;
	String location;
	String payOut;
	String workId;
	String capability;

	String workRatedForWorker;
	String customerRatedforWork;

	int bidPlaced;
	String statusOfWork;

	public String getCapability() {
		return capability;
	}

	public void setCapability(String capability) {
		this.capability = capability;
	}

	// public String getCustomerRating() {
	// return customerRating;
	// }
	//
	// public void setCustomerRating(String customerRating) {
	// this.customerRating = customerRating;
	// }

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public NewCustomerWork(String workId, String customerName, String jobDescription, String location, String payOut,
			String capability) {
		this.workId = workId;
		this.customerName = customerName;
		this.jobDescription = jobDescription;
		this.location = location;
		this.payOut = payOut;
		this.capability = capability;
		// this.customerRating = customerRating;
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
		return workId + ", " + jobDescription + ", " + payOut + ", " + customerName + ", " + location + ", " + capability;
	}

}
