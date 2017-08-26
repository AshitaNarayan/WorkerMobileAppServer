package com.workerewebapp.service.impl;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.workerewebapp.app.data.NewCustomerWork;
import com.workerewebapp.app.data.WorkerJob;
import com.workerewebapp.service.CustomerNewWorkServiceInterface;

@Service
public class CustomerNewWorkService implements CustomerNewWorkServiceInterface {

	ConcurrentHashMap<String, NewCustomerWork> workDetailedList = new ConcurrentHashMap<>();

	int workIdCounter = 0;
	// ConcurrentHashMap<String, Integer> countOfWorkPerCustomer = new
	// ConcurrentHashMap<>();

	// Group the work also by capability to increase the performance of matching
	// algorithm.
	ConcurrentHashMap<String, ArrayList<String>> workGroupedByCapabiilty = new ConcurrentHashMap<>();
	ArrayList<String> workIDCapabilityList = null;

	ConcurrentHashMap<String, ArrayList<String>> customerActiveWorkMap = new ConcurrentHashMap<>();
	ArrayList<String> customerActiveWorkIDList = null;

	ConcurrentHashMap<String, ArrayList<String>> customerCompletedWorkMap = new ConcurrentHashMap<>();
	ArrayList<String> customerCompletedWorkIDList = null;

	ConcurrentHashMap<String, ArrayList<String>> workerActiveWorkMap = new ConcurrentHashMap<>();
	ArrayList<String> workerActiveWorkIDList = null;

	ConcurrentHashMap<String, ArrayList<String>> workerCompletedWorkMap = new ConcurrentHashMap<>();
	ArrayList<String> workerCompletedWorkIDList = null;

	ConcurrentHashMap<String, String> customerRatingMap = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, String> workerRatingMap = new ConcurrentHashMap<>();

	int PAYMENT_DIFF_THRESHOLD = 1000;

	String GENERAL_LABOR_SKILL = "GeneralLabor";

	ArrayList<WorkerJob> matchingJobsForWorker = null;

	@Override
	public List<NewCustomerWork> addNewWorkByCustomer(String data) {
		JSONObject newWorkAdded = new JSONObject(data);
		addWorkInDetailedList(newWorkAdded);
		printAllWorkDetail();
		return getAllJobsForCustomer(newWorkAdded.getString("customer_Name"));

	}

	public List<NewCustomerWork> getWorkForCustomer(String data) {
		JSONObject newWorkAdded = new JSONObject(data);
		return getAllJobsForCustomer(newWorkAdded.getString("customer_Name"));

	}

	/*
	 * private Integer fetchCountForGivenCustomer(String customerName) { Integer
	 * countForCustomer = countOfWorkPerCustomer.get(customerName); if
	 * (countForCustomer != null) { countForCustomer++;
	 * countOfWorkPerCustomer.put(customerName, countForCustomer); } else {
	 * countForCustomer = Integer.parseInt("1");
	 * countOfWorkPerCustomer.put(customerName, countForCustomer); } return
	 * countForCustomer; }
	 */

	private void printAllWorkDetail() {
		System.out.println();
		Iterator it = workDetailedList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}

	@Override
	public List<WorkerJob> matchJobWithWorkerWorkRequest(String data) {
		JSONObject newRequestByWorkerForMatchingJob = new JSONObject(data);

		Integer locationRange = 1;
		Integer payInRange = 1;
		
		if(newRequestByWorkerForMatchingJob.get("location_Range") instanceof String)
		{
			locationRange = Integer.parseInt((String) newRequestByWorkerForMatchingJob.get("location_Range"));
		}
		else
		{
			locationRange = (Integer) newRequestByWorkerForMatchingJob.get("location_Range");
		}
		
		if(newRequestByWorkerForMatchingJob.get("pay_In_Range") instanceof String)
		{
			payInRange = Integer.parseInt((String) newRequestByWorkerForMatchingJob.get("pay_In_Range"));
		}
		else
		{
			payInRange = (Integer) newRequestByWorkerForMatchingJob.get("pay_In_Range");
		}

		matchingJobsForWorker = new ArrayList<>();

		String workerLocation = (String) newRequestByWorkerForMatchingJob.get("worker_Location");
		String workerSkill = (String) newRequestByWorkerForMatchingJob.get("worker_Skill");

		Iterator it = workDetailedList.entrySet().iterator();
		System.out.println("No work found. Skipping and adding no jobs.");

		// TODO reverse the order of thw work match check, based on performance
		// do it by skil match first
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			NewCustomerWork newWork = (NewCustomerWork) pair.getValue();
			// Match location
			if (matchLocation(newWork.getLocation(), workerLocation, locationRange)) {
				// Match Payment Range
				if (matchPayment(newWork.getPayOut(), payInRange)) {

					if (matchSkill(newWork.getCapability(), workerSkill)) {
						// add to the list of returned jobs, need to sort by
						// customer rating to show on worker list
						NewCustomerWork workDetail = (NewCustomerWork) pair.getValue();
						WorkerJob workerJob = new WorkerJob(workDetail.getCustomerName(),
								customerRatingMap.get(workDetail.getCustomerName()), workDetail.getCapability(),
								workDetail.getLocation(), workDetail.getPayOut(), workDetail.getWorkId(),
								workDetail.getJobDescription());
						matchingJobsForWorker.add(workerJob);
						System.out.println("FINAL MATCH FOUND" + pair.getKey() + " = " + pair.getValue());
					}

				}
			}

		}

		return sortMatchedJobsByCustomerRating(matchingJobsForWorker);
		// List<NewCustomerWork> matchJobsForWorkerSortedByCustomerRating =
		// sortByCustomerRating();
		// List<NewCustomerWork> matchJobsForWorkerSortedByPayment =
		// sortByPayment();
		// sortByLocation();

		// return
		// createJSONListFromResults(matchJobsForWorkerSortedByCustomerRating,
		// matchJobsForWorkerSortedByPayment);

	}

	private ArrayList<WorkerJob> sortMatchedJobsByCustomerRating(ArrayList<WorkerJob> matchingJobsForWorker) {

		Collections.sort(matchingJobsForWorker, new Comparator<WorkerJob>() {

			public int compare(WorkerJob o1, WorkerJob o2) {
				double x = Double.parseDouble(o2.getCustomerRating());
				double y = Double.parseDouble(o1.getCustomerRating());
				if (x < y)
					return -1;
				if (x > y)
					return 1;
				return 0;
			}
		});
		printListDetailsOfWorker(matchingJobsForWorker);
		return matchingJobsForWorker;
	}

	/*
	 * private JSONObject createJSONListFromResults(List<NewCustomerWork>
	 * matchJobsForWorkerSortedByCustomerRating, List<NewCustomerWork>
	 * matchJobsForWorkerSortedByPayment) { JSONObject
	 * mergedMatchedWorkForCustomer = new JSONObject();
	 * 
	 * JSONArray values = new JSONArray(); for (NewCustomerWork matchedWorkerJob
	 * : matchJobsForWorkerSortedByCustomerRating) { JSONObject matchJob = new
	 * JSONObject(); matchJob.put("customer_Name",
	 * matchedWorkerJob.getCustomerName()); matchJob.put("customer_Rating",
	 * matchedWorkerJob.getCustomerRating());
	 * matchJob.put("customer_WorkSequence",
	 * matchedWorkerJob.getCustomerWorkSequence()); matchJob.put("location",
	 * matchedWorkerJob.getLocation()); matchJob.put("pay_Out",
	 * matchedWorkerJob.getPayOut()); matchJob.put("job_Description",
	 * matchedWorkerJob.getJobDescription()); values.put(matchJob); }
	 * 
	 * mergedMatchedWorkForCustomer.put("sortedByCustomerRating", values);
	 * 
	 * JSONArray values2 = new JSONArray(); for (NewCustomerWork
	 * matchedWorkerJob : matchJobsForWorkerSortedByPayment) { JSONObject
	 * matchJob = new JSONObject(); matchJob.put("customer_Name",
	 * matchedWorkerJob.getCustomerName()); matchJob.put("customer_Rating",
	 * matchedWorkerJob.getCustomerRating());
	 * matchJob.put("customer_WorkSequence",
	 * matchedWorkerJob.getCustomerWorkSequence()); matchJob.put("location",
	 * matchedWorkerJob.getLocation()); matchJob.put("pay_Out",
	 * matchedWorkerJob.getPayOut()); matchJob.put("job_Description",
	 * matchedWorkerJob.getJobDescription()); values2.put(matchJob); }
	 * 
	 * mergedMatchedWorkForCustomer.put("sortedByCustomerRating", values2);
	 * 
	 * return mergedMatchedWorkForCustomer;
	 * 
	 * }
	 */

	private void sortByPayment() {
		/*
		 * List<NewCustomerWork> matchJobsForWorkerSortedByPayment = new
		 * ArrayList<NewCustomerWork>( matchingJobsForWorker.values());
		 * Collections.sort(matchJobsForWorkerSortedByPayment, new
		 * Comparator<NewCustomerWork>() {
		 * 
		 * public int compare(NewCustomerWork o1, NewCustomerWork o2) { return
		 * Integer.parseInt(o1.getPayOut()) - Integer.parseInt(o2.getPayOut());
		 * } }); return matchJobsForWorkerSortedByPayment;
		 */

	}

	private void sortByLocation() {
		// Show the location which is nearest to customer location

	}

	private List<NewCustomerWork> getAllJobsForCustomer(String customerName) {

		// Get the list of active
		customerActiveWorkIDList = customerActiveWorkMap.get(customerName);

		List<NewCustomerWork> jobsForCustomer = new ArrayList<NewCustomerWork>();

		for (int index = 0; customerActiveWorkIDList != null && index < customerActiveWorkIDList.size(); index++) {
			String workID = customerActiveWorkIDList.get(index);
			jobsForCustomer.add(workDetailedList.get(workID));

		}
		printListDetails(jobsForCustomer);
		return jobsForCustomer;
	}

	private void printListDetails(List<NewCustomerWork> jobsForCustomer) {
		System.out.println("Printing the jobs for a customer");
		Iterator it = jobsForCustomer.iterator();
		while (it.hasNext()) {
			NewCustomerWork customerWork = (NewCustomerWork) it.next();
			System.out.println(customerWork);
		}
	}

	private void printListDetailsOfWorker(List<WorkerJob> jobsForWorker) {
		System.out.println("Printing the job details of a worker");
		Iterator it = jobsForWorker.iterator();
		while (it.hasNext()) {
			WorkerJob workerJob = (WorkerJob) it.next();
			System.out.println(workerJob);
		}
	}

	private boolean matchSkill(String capability, String workerSkill) {

		if (workerSkill.equals("babysitting") || workerSkill.equals("cooking")) {
			if (workerSkill.equals(capability)) {
				System.out.println("MATCH :: Skill match found. Skill is : " + workerSkill
						+ " and capability needed is " + capability);
				return true;
			}
		}
		if (workerSkill.equals(GENERAL_LABOR_SKILL)) {
			if (capability.equals("runanerrand") || capability.equals("packageDelivery") || capability.equals("pickup")
					|| capability.equals("lawnmowing") || capability.equals("homework") || capability.equals("paint")
					|| capability.equals("carpool")) {
				System.out.println("MATCH :: Skill match found. It meets the general labour capability " + capability);
				return true;
			}

		}
		System.out.println("MISMATCH :: Skill mismatch found. Skipping job." + " Skill provided was : " + workerSkill
				+ " but capability needed was " + capability);
		return false;
	}

	private boolean matchPayment(String payOut, Integer payInRange) {

		int diff = Integer.parseInt(payOut) - payInRange;
		if (diff < 0) {
			diff = diff * (-1);
		}

		int payOutDiffPercent = (diff / payInRange) * 100;

		if (payOutDiffPercent <= PAYMENT_DIFF_THRESHOLD) {
			System.out.println(
					"MATCH :: Payment match found." + "The pay out percent difference is: " + payOutDiffPercent + "%");
			return true;
		}
		System.out.println("MISMATCH :: Payment Mismatch. Skipping job as pay out percent difference is: "
				+ payOutDiffPercent + "%");
		return false;
	}

	private boolean matchLocation(String workLocation, String workerLocation, Integer locationRange) {

		if (workerLocation.equals("")) {
			System.out.println("MATCH :: Location matched with any as worker didnt provide a location");
			return true;
		}

		// Return true if both locations match
		if (workerLocation.equals(workLocation)) {
			System.out.println("MATCH :: Location matched" + workerLocation);
			return true;
		}
		// Else go for comparing if the location falls in the location range (x
		// KM) range
		System.out.println("MISMATCH :: Location mismatch : Skipping job");
		return false;
	}

	public void resetAllData() {
		workDetailedList.clear();
		workIdCounter = 0;
		workGroupedByCapabiilty.clear();
		customerActiveWorkMap.clear();
		// customerCompletedWorkMap.clear();
		workerActiveWorkMap.clear();
		// workerCompletedWorkMap.clear();
		customerRatingMap.clear();
		workerRatingMap.clear();

	}

	public void insertBulkData(String data) {
		JSONArray bulkWorkToAdd = new JSONArray(data);
		for (int index = 0; index < bulkWorkToAdd.length(); index++) {
			JSONObject newWorkAdded = bulkWorkToAdd.getJSONObject(index);
			addWorkInDetailedList(newWorkAdded);

		}
		printAllWorkDetail();

	}

	private void addWorkInDetailedList(JSONObject newWorkAdded) {
		workIdCounter++;
		// Integer countForCustomer =
		// fetchCountForGivenCustomer(newWorkAdded.getString("customer_Name"));

		NewCustomerWork newCustomerWork = new NewCustomerWork("WID" + "_" + workIdCounter,
				newWorkAdded.getString("customer_Name"), newWorkAdded.getString("job_Description"),
				newWorkAdded.getString("location"), newWorkAdded.getString("pay_Out"),
				newWorkAdded.getString("capability"));

		// Push new work to detail list
		workDetailedList.put("WID" + "_" + workIdCounter, newCustomerWork);
		// Get the list of active
		customerActiveWorkIDList = customerActiveWorkMap.get(newWorkAdded.getString("customer_Name"));
		customerRatingMap.put(newWorkAdded.getString("customer_Name"), newWorkAdded.getString("customer_Rating"));
		if (customerActiveWorkIDList == null) {
			customerActiveWorkIDList = new ArrayList<>();
		}
		customerActiveWorkIDList.add("WID" + "_" + workIdCounter);

		customerActiveWorkMap.put(newWorkAdded.getString("customer_Name"), customerActiveWorkIDList);

	}
}
