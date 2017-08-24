package com.workerewebapp.app.data;

public class NewCustomerWork {

	String customerName;
	String customerRating;
	
	//For every customer he creates a list of work
	String jobDescription;
	String location;
	String payOut;
	String customerWorkSequence;
	String capability;

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

	

	public String getCustomerWorkSequence() {
		return customerWorkSequence;
	}

	public void setCustomerWorkSequence(String customerWorkSequence) {
		this.customerWorkSequence = customerWorkSequence;
	}

	public NewCustomerWork(String workId, String customerName, String jobDescription, String location, String payOut,
			String capability, String customerRating) {
		this.customerWorkSequence = customerWorkSequence;
		this.customerName = customerName;
		this.jobDescription = jobDescription;
		this.location = location;
		this.payOut = payOut;
		this.capability = capability;
		this.customerRating = customerRating;
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
		return customerWorkSequence + ", " + jobDescription + ", " + payOut + ", " + customerName + ", " + location;
	}

}
