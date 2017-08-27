package com.workerewebapp.app.data;

public class MyJobDetail {

	// Dynamically creating list of jobs for worker and customer when they fetch
	// from their "My Job" page.
	String customerName;
	String customerRating;

	String workerName;
	String workerRating;

	String jobDescription;
	String workID;

	String location;
	String capability;

	int payIn;
	String payOut;
	String status;
	
	int workerRated;
	int customerRated;

	public int getWorkerRated() {
		return workerRated;
	}

	public void setWorkerRated(int i) {
		this.workerRated = i;
	}

	public int getCustomerRated() {
		return customerRated;
	}

	public void setCustomerRated(int i) {
		this.customerRated = i;
	}

	public MyJobDetail(String customerName, String rating, String capability, String location, String payOut,
			String workId, String jobDescription) {
		this.customerName = customerName;
		this.customerRating = rating;
		this.capability = capability;
		this.location = location;
		this.payOut = payOut;
		this.workID = workId;
		this.jobDescription = jobDescription;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getWorkID() {
		return workID;
	}

	public void setWorkID(String workID) {
		this.workID = workID;
	}

	public String getPayOut() {
		return payOut;
	}

	public void setPayOut(String payOut) {
		this.payOut = payOut;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCapability() {
		return capability;
	}

	public void setCapability(String capability) {
		this.capability = capability;
	}

	public String getCustomerRating() {
		return customerRating;
	}

	public void setCustomerRating(String customerRating) {
		this.customerRating = customerRating;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerRating() {
		return workerRating;
	}

	public void setWorkerRating(String workerRating) {
		this.workerRating = workerRating;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPayIn() {
		return payIn;
	}

	public void setPayIn(int i) {
		this.payIn = i;
	}

	public String toString() {
		return workID + ", " + jobDescription + ", " + payOut + ", " + customerName + ", " + location + ", "
				+ capability + ", " + customerRating + ", " + payIn;
	}
}
