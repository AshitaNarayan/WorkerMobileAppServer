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

import com.workerewebapp.app.data.WorkDetail;
import com.workerewebapp.app.data.MyJobDetail;
import com.workerewebapp.service.WorkFinderService;

@Service
public class WorkFinderMobileApplicationService implements WorkFinderService {

	ConcurrentHashMap<String, WorkDetail> workDetailedList = new ConcurrentHashMap<>();

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

	ArrayList<MyJobDetail> matchingJobsForWorker = null;

	@Override
	public List<WorkDetail> addNewWorkByCustomer(String data) {
		JSONObject newWorkAdded = new JSONObject(data);
		addWorkInDetailedList(newWorkAdded);
		printAllWorkDetail();
		return getAllJobsForCustomer(newWorkAdded.getString("customer_Name"));

	}

	public List<WorkDetail> getWorkForCustomer(String data) {
		JSONObject newWorkAdded = new JSONObject(data);
		return getAllJobsForCustomer(newWorkAdded.getString("customer_Name"));

	}

	private void printAllWorkDetail() {
		System.out.println();
		Iterator it = workDetailedList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}

	@Override
	public List<MyJobDetail> matchJobWithWorkerWorkRequest(String data) {
		JSONObject newRequestByWorkerForMatchingJob = new JSONObject(data);

		Integer locationRange = 1;
		Integer payInRange = 1;

		if (newRequestByWorkerForMatchingJob.get("location_Range") instanceof String) {
			locationRange = Integer.parseInt((String) newRequestByWorkerForMatchingJob.get("location_Range"));
		} else {
			locationRange = (Integer) newRequestByWorkerForMatchingJob.get("location_Range");
		}

		if (newRequestByWorkerForMatchingJob.get("pay_In_Range") instanceof String) {
			payInRange = Integer.parseInt((String) newRequestByWorkerForMatchingJob.get("pay_In_Range"));
		} else {
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
			WorkDetail newWork = (WorkDetail) pair.getValue();
			// Match location
			if (matchLocation(newWork.getLocation(), workerLocation, locationRange)) {
				// Match Payment Range
				if (matchPayment(newWork.getPayOut(), payInRange)) {

					if (matchSkill(newWork.getCapability(), workerSkill)) {
						// add to the list of returned jobs, need to sort by
						// customer rating to show on worker list
						WorkDetail workDetail = (WorkDetail) pair.getValue();
						MyJobDetail workerJob = new MyJobDetail(workDetail.getCustomerName(),
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

	private ArrayList<MyJobDetail> sortMatchedJobsByCustomerRating(ArrayList<MyJobDetail> matchingJobsForWorker) {

		Collections.sort(matchingJobsForWorker, new Comparator<MyJobDetail>() {

			public int compare(MyJobDetail o1, MyJobDetail o2) {
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

	private ArrayList<MyJobDetail> sortMatchedJobsByWorkerRating(ArrayList<MyJobDetail> matchingJobsForWorker) {

		Collections.sort(matchingJobsForWorker, new Comparator<MyJobDetail>() {

			public int compare(MyJobDetail o1, MyJobDetail o2) {
				double x = Double.parseDouble(o2.getWorkerRating());
				double y = Double.parseDouble(o1.getWorkerRating());
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

	private List<WorkDetail> getAllJobsForCustomer(String customerName) {

		// Get the list of active
		customerActiveWorkIDList = customerActiveWorkMap.get(customerName);

		List<WorkDetail> jobsForCustomer = new ArrayList<WorkDetail>();

		for (int index = 0; customerActiveWorkIDList != null && index < customerActiveWorkIDList.size(); index++) {
			String workID = customerActiveWorkIDList.get(index);
			jobsForCustomer.add(workDetailedList.get(workID));

		}
		printListDetails(jobsForCustomer);
		return jobsForCustomer;
	}

	private void printListDetails(List<WorkDetail> jobsForCustomer) {
		System.out.println("Printing the jobs for a customer");
		Iterator it = jobsForCustomer.iterator();
		while (it.hasNext()) {
			WorkDetail customerWork = (WorkDetail) it.next();
			System.out.println(customerWork);
		}
	}

	private void printListDetailsOfWorker(List<MyJobDetail> jobsForWorker) {
		System.out.println("Printing the job details of a worker");
		Iterator it = jobsForWorker.iterator();
		while (it.hasNext()) {
			MyJobDetail workerJob = (MyJobDetail) it.next();
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
		customerCompletedWorkMap.clear();
		workerActiveWorkMap.clear();
		workerCompletedWorkMap.clear();
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

		WorkDetail newCustomerWork = new WorkDetail("WID" + "_" + workIdCounter,
				newWorkAdded.getString("customer_Name"), newWorkAdded.getString("job_Description"),
				newWorkAdded.getString("location"), newWorkAdded.getString("pay_Out"),
				newWorkAdded.getString("capability"));

		// Push new work to detail list
		workDetailedList.put("WID" + "_" + workIdCounter, newCustomerWork);
		customerRatingMap.put(newWorkAdded.getString("customer_Name"), newWorkAdded.getString("customer_Rating"));
		if (newWorkAdded.has("status")) {
			newCustomerWork.setStatusOfWork(newWorkAdded.getString("status"));
			if (newWorkAdded.getString("status").equals("Interested")
					|| newWorkAdded.getString("status").equals("Accepted")) {
				// Get the list of active
				customerActiveWorkIDList = customerActiveWorkMap.get(newWorkAdded.getString("customer_Name"));

				if (customerActiveWorkIDList == null) {
					customerActiveWorkIDList = new ArrayList<>();
				}
				customerActiveWorkIDList.add("WID" + "_" + workIdCounter);

				customerActiveWorkMap.put(newWorkAdded.getString("customer_Name"), customerActiveWorkIDList);
			}
			if (newWorkAdded.getString("status").equals("Completed")) {
				// Get the list of active
				customerCompletedWorkIDList = customerCompletedWorkMap.get(newWorkAdded.getString("customer_Name"));
				if (customerCompletedWorkIDList == null) {
					customerCompletedWorkIDList = new ArrayList<>();
				}
				customerCompletedWorkIDList.add("WID" + "_" + workIdCounter);

				customerCompletedWorkMap.put(newWorkAdded.getString("customer_Name"), customerCompletedWorkIDList);
			}
		} else {// Get the list of active
			customerActiveWorkIDList = customerActiveWorkMap.get(newWorkAdded.getString("customer_Name"));

			if (customerActiveWorkIDList == null) {
				customerActiveWorkIDList = new ArrayList<>();
			}
			customerActiveWorkIDList.add("WID" + "_" + workIdCounter);

			customerActiveWorkMap.put(newWorkAdded.getString("customer_Name"), customerActiveWorkIDList);
		}

		/** If bulk data insert do additional steps **/
		if (newWorkAdded.has("bid_Amount")) {
			newCustomerWork.setBidPlaced(newWorkAdded.getInt("bid_Amount"));
		}
		if (newWorkAdded.has("worker_Name")) {
			newCustomerWork.setWorkerName(newWorkAdded.getString("worker_Name"));
		}
		if (newWorkAdded.has("customer_Rated")) {
			newCustomerWork.setCustomerRatedforWork(newWorkAdded.getInt("customer_Rated"));
		}
		if (newWorkAdded.has("worker_Rated")) {
			newCustomerWork.setWorkRatedForWorker(newWorkAdded.getInt("worker_Rated"));
		}

		if (newWorkAdded.has("status")) {
			newCustomerWork.setStatusOfWork(newWorkAdded.getString("status"));
			workerRatingMap.put(newWorkAdded.getString("worker_Name"), newWorkAdded.getString("worker_Rating"));
			if (newWorkAdded.getString("status").equals("Interested")
					|| newWorkAdded.getString("status").equals("Accepted")) {
				// Get the list of active
				workerActiveWorkIDList = workerActiveWorkMap.get(newWorkAdded.getString("worker_Name"));

				if (workerActiveWorkIDList == null) {
					workerActiveWorkIDList = new ArrayList<>();
				}
				workerActiveWorkIDList.add("WID" + "_" + workIdCounter);

				workerActiveWorkMap.put(newWorkAdded.getString("worker_Name"), workerActiveWorkIDList);
			}

			if (newWorkAdded.getString("status").equals("Completed")) {
				// Get the list of active
				workerCompletedWorkIDList = workerCompletedWorkMap.get(newWorkAdded.getString("worker_Name"));
				if (workerCompletedWorkIDList == null) {
					workerCompletedWorkIDList = new ArrayList<>();
				}
				workerCompletedWorkIDList.add("WID" + "_" + workIdCounter);

				workerCompletedWorkMap.put(newWorkAdded.getString("worker_Name"), workerCompletedWorkIDList);
			}

		}

	}

	@Override
	public void workerInterestInJob(String data) {

		JSONObject workerInterestInJob = new JSONObject(data);
		String workID = workerInterestInJob.getString("workID");
		WorkDetail workDetail = workDetailedList.get(workID);
		workDetail.setBidPlaced(Integer.parseInt(workerInterestInJob.getString("bidAmount")));
		workDetail.setWorkerName(workerInterestInJob.getString("worker_Name"));
		workDetail.setStatusOfWork("Interested");

		// Place in worker active map

		workerActiveWorkIDList = workerActiveWorkMap.get(workerInterestInJob.getString("worker_Name"));
		if (workerActiveWorkIDList == null) {
			workerActiveWorkIDList = new ArrayList<String>();
		}

		workerActiveWorkIDList.add(workID);
		workerActiveWorkMap.put(workerInterestInJob.getString("worker_Name"), workerActiveWorkIDList);

		// Place in customer active map

		/*customerActiveWorkIDList = customerActiveWorkMap.get(workerInterestInJob.getString("customerName"));
		if (customerActiveWorkIDList == null) {
			customerActiveWorkIDList = new ArrayList<String>();
		}

		customerActiveWorkIDList.add(workID);
		customerActiveWorkMap.put(workerInterestInJob.getString("customerName"), customerActiveWorkIDList);*/

		workerRatingMap.put(workerInterestInJob.getString("worker_Name"),
				workerInterestInJob.getString("worker_Rating"));

		System.out.println("Added work ID against customer and worker active list maps " + workID);
	}

	@Override
	public List<MyJobDetail> getInterestedAndAcceptedJobsListForWorker(String data) {

		ArrayList<MyJobDetail> interestedAndAcceptedJobsListForWorker = new ArrayList<MyJobDetail>();
		JSONObject workerInterestInJob = new JSONObject(data);
		String workerName = workerInterestInJob.getString("worker_Name");

		workerActiveWorkIDList = workerActiveWorkMap.get(workerName);

		for (int index = 0; workerActiveWorkIDList != null && index < workerActiveWorkIDList.size(); index++) {
			WorkDetail workDetail = workDetailedList.get(workerActiveWorkIDList.get(index));

			if (workDetail.getStatusOfWork().equals("Interested") || workDetail.getStatusOfWork().equals("Accepted")) {
				MyJobDetail workerJob = new MyJobDetail(workDetail.getCustomerName(),
						customerRatingMap.get(workDetail.getCustomerName()), workDetail.getCapability(),
						workDetail.getLocation(), workDetail.getPayOut(), workDetail.getWorkId(),
						workDetail.getJobDescription());
				workerJob.setStatus(workDetail.getStatusOfWork());
				workerJob.setPayIn(workDetail.getBidPlaced());
				workerJob.setWorkerName(workDetail.getWorkerName());

				interestedAndAcceptedJobsListForWorker.add(workerJob);
			}
		}

		workerCompletedWorkIDList = workerCompletedWorkMap.get(workerName);

		for (int index = 0; workerCompletedWorkIDList != null && index < workerCompletedWorkIDList.size(); index++) {
			WorkDetail workDetail = workDetailedList.get(workerCompletedWorkIDList.get(index));

			if (workDetail.getStatusOfWork().equals("Completed")) {
				MyJobDetail workerJob = new MyJobDetail(workDetail.getCustomerName(),
						customerRatingMap.get(workDetail.getCustomerName()), workDetail.getCapability(),
						workDetail.getLocation(), workDetail.getPayOut(), workDetail.getWorkId(),
						workDetail.getJobDescription());
				workerJob.setStatus(workDetail.getStatusOfWork());
				workerJob.setPayIn(workDetail.getBidPlaced());
				workerJob.setWorkerName(workDetail.getWorkerName());
				workerJob.setWorkerRated(workDetail.getWorkRatedForWorker());
				workerJob.setCustomerRated(workDetail.getCustomerRatedforWork());

				interestedAndAcceptedJobsListForWorker.add(workerJob);
			}
		}
		return sortMatchedJobsByCustomerRating(interestedAndAcceptedJobsListForWorker);
	}

	@Override
	public List<MyJobDetail> getInterestedJobsListForCustomer(String data) {
		ArrayList<MyJobDetail> interestedAndAcceptedJobsListForCustomer = new ArrayList<MyJobDetail>();
		JSONObject workerInterestInJob = new JSONObject(data);
		String customerName = workerInterestInJob.getString("customer_Name");

		customerActiveWorkIDList = customerActiveWorkMap.get(customerName);

		for (int index = 0; customerActiveWorkIDList != null && index < customerActiveWorkIDList.size(); index++) {
			WorkDetail workDetail = workDetailedList.get(customerActiveWorkIDList.get(index));
			if (workDetail.getStatusOfWork() != null && (workDetail.getStatusOfWork().equals("Interested")
					|| workDetail.getStatusOfWork().equals("Accepted"))) {

				MyJobDetail workerJob = new MyJobDetail(workDetail.getCustomerName(),
						customerRatingMap.get(workDetail.getCustomerName()), workDetail.getCapability(),
						workDetail.getLocation(), workDetail.getPayOut(), workDetail.getWorkId(),
						workDetail.getJobDescription());

				workerJob.setStatus(workDetail.getStatusOfWork());
				workerJob.setPayIn(workDetail.getBidPlaced());
				workerJob.setWorkerName(workDetail.getWorkerName());
				workerJob.setWorkerRating(workerRatingMap.get(workDetail.getWorkerName()));
				interestedAndAcceptedJobsListForCustomer.add(workerJob);
			}
		}
		customerCompletedWorkIDList = customerCompletedWorkMap.get(customerName);

		for (int index1 = 0; customerCompletedWorkIDList != null
				&& index1 < customerCompletedWorkIDList.size(); index1++) {
			WorkDetail workDetail1 = workDetailedList.get(customerCompletedWorkIDList.get(index1));
			if (workDetail1.getStatusOfWork() != null && (workDetail1.getStatusOfWork().equals("Completed"))) {
				MyJobDetail workerJob1 = new MyJobDetail(workDetail1.getCustomerName(),
						customerRatingMap.get(workDetail1.getCustomerName()), workDetail1.getCapability(),
						workDetail1.getLocation(), workDetail1.getPayOut(), workDetail1.getWorkId(),
						workDetail1.getJobDescription());

				workerJob1.setStatus(workDetail1.getStatusOfWork());
				workerJob1.setPayIn(workDetail1.getBidPlaced());
				workerJob1.setWorkerName(workDetail1.getWorkerName());
				workerJob1.setWorkerRating(workerRatingMap.get(workDetail1.getWorkerName()));
				
				workerJob1.setCustomerRated(workDetail1.getCustomerRatedforWork());
				workerJob1.setWorkerRated(workDetail1.getWorkRatedForWorker());
				
				interestedAndAcceptedJobsListForCustomer.add(workerJob1);
			}
		}

		return sortMatchedJobsByWorkerRating(interestedAndAcceptedJobsListForCustomer);
	}

	@Override
	public void customerAcceptsJob(String data) {
		JSONObject customerAcceptsJob = new JSONObject(data);
		String workID = customerAcceptsJob.getString("workID");
		WorkDetail workDetail = workDetailedList.get(workID);
		workDetail.setStatusOfWork("Accepted");

		System.out.println("Updated work to status of Accepted " + workID);

	}

	@Override
	public void workerCompleteJob(String data) {
		JSONObject workerCompleteJob = new JSONObject(data);
		String workID = workerCompleteJob.getString("workID");
		WorkDetail workDetail = workDetailedList.get(workID);
		workDetail.setCustomerRatedforWork(workerCompleteJob.getInt("customer_rating_given"));
		workDetail.setStatusOfWork("Completed");

		String customerName = workerCompleteJob.getString("customerName");
		customerActiveWorkIDList = customerActiveWorkMap.get(customerName);
		// Remove from the active list of customer
		for (int index = 0; customerActiveWorkIDList != null && index < customerActiveWorkIDList.size(); index++) {
			String workIdFromList = customerActiveWorkIDList.get(index);
			if (workIdFromList.equals(workID)) {
				customerActiveWorkIDList.remove(index);
			}

		}

		String workerName = workerCompleteJob.getString("workerName");
		workerActiveWorkIDList = workerActiveWorkMap.get(workerName);
		// Remove from the active list of worker
		for (int index = 0; workerActiveWorkIDList != null && index < workerActiveWorkIDList.size(); index++) {
			String workIdFromList = workerActiveWorkIDList.get(index);
			if (workIdFromList.equals(workID)) {
				workerActiveWorkIDList.remove(index);
			}

		}
		// Add to completed list of worker

		// Add to completed list of customer

		
		workerCompletedWorkIDList = workerCompletedWorkMap.get(workerCompleteJob.getString("workerName"));
		if (workerCompletedWorkIDList == null) {
			workerCompletedWorkIDList = new ArrayList<String>();
		}

		workerCompletedWorkIDList.add(workID);
		workerCompletedWorkMap.put(workerCompleteJob.getString("workerName"), workerCompletedWorkIDList);

		// Place in customer active map

		customerCompletedWorkIDList = customerCompletedWorkMap.get(workerCompleteJob.getString("customerName"));
		if (customerCompletedWorkIDList == null) {
			customerCompletedWorkIDList = new ArrayList<String>();
		}

		customerCompletedWorkIDList.add(workID);
		customerCompletedWorkMap.put(workerCompleteJob.getString("customerName"), customerCompletedWorkIDList);
		// Trigger overall rate calculation
		System.out.println("Updated work to status of Completed " + workID);

	}

	@Override
	public void customerRatesWorker(String data) {
		JSONObject customerRatesWorker = new JSONObject(data);
		String workID = customerRatesWorker.getString("workID");
		WorkDetail workDetail = workDetailedList.get(workID);
		workDetail.setWorkRatedForWorker(customerRatesWorker.getInt("worker_rating_given"));

		System.out.println("Updated work with rating of worker " + workID);
		
	}
}
