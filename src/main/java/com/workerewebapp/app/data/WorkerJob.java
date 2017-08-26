package com.workerewebapp.app.data;

public class WorkerJob {

	public WorkerJob(String customerName, String rating, String capability, String location, String payOut,
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

	String customerName;
	String customerRating;

	String jobDescription;
	String workID;
	String payOut;
	String location;
	String capability;

	public String toString() {
		return workID + ", " + jobDescription + ", " + payOut + ", " + customerName + ", " + location + ", "
				+ capability + ", " + customerRating;
	}
}
