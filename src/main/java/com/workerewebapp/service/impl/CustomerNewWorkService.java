package com.workerewebapp.service.impl;

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
import com.workerewebapp.service.CustomerNewWorkServiceInterface;

@Service
public class CustomerNewWorkService implements CustomerNewWorkServiceInterface {

	ConcurrentHashMap<String, NewCustomerWork> newWorkByCustomer = new ConcurrentHashMap<>();

	int newWorkByCustomerCount = 0;

	int PAYMENT_DIFF_THRESHOLD = 10;

	String GENERAL_LABOR_SKILL = "GeneralLabor";

	ConcurrentHashMap<String, NewCustomerWork> matchingJobsForWorker = new ConcurrentHashMap<>();

	@Override
	public void addNewWorkByCustomer(String data) {
		JSONObject newWorkAdded = new JSONObject(data);
		newWorkByCustomerCount++;

		NewCustomerWork newCustomerWork = new NewCustomerWork(
				newWorkAdded.getString("customer_Name") + "_" + newWorkByCustomerCount,
				newWorkAdded.getString("customer_Name"), newWorkAdded.getString("job_Description"),
				newWorkAdded.getString("location"), newWorkAdded.getString("pay_Out"),
				newWorkAdded.getString("capability"), newWorkAdded.getString("customer_Rating"));

		newWorkByCustomer.put(newWorkAdded.getString("customer_Name") + "_" + newWorkByCustomerCount, newCustomerWork);

		printNewWorkByCustomer();

	}

	private void printNewWorkByCustomer() {
		System.out.println();
		Iterator it = newWorkByCustomer.entrySet().iterator();
		while (it.hasNext()) {
			System.out.println("New work Added By Customer Map");
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}

	@Override
	public List<NewCustomerWork> matchJobWithWorkerWorkRequest(String data) {
		JSONObject newRequestByWorkerForMatchingJob = new JSONObject(data);

		Integer locationRange = 1;
		if (!newRequestByWorkerForMatchingJob.get("location_Range").equals("")) {
			locationRange = Integer.parseInt((String) newRequestByWorkerForMatchingJob.get("location_Range"));
		}
		Integer payInRange = 1;
		if (newRequestByWorkerForMatchingJob.get("pay_In_Range").equals("")) {
			payInRange = (Integer) newRequestByWorkerForMatchingJob.get("pay_In_Range");
		}
		String workerLocation = (String) newRequestByWorkerForMatchingJob.get("worker_Location");
		String workerSkill = (String) newRequestByWorkerForMatchingJob.get("worker_Skill");

		Iterator it = newWorkByCustomer.entrySet().iterator();
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
						matchingJobsForWorker.put((String) pair.getKey(), (NewCustomerWork) pair.getValue());
						System.out.println("Inserted" + pair.getKey() + " = " + pair.getValue());
					}

				}
			}

		}

		return sortByCustomerRating();
		// List<NewCustomerWork> matchJobsForWorkerSortedByCustomerRating =
		// sortByCustomerRating();
		// List<NewCustomerWork> matchJobsForWorkerSortedByPayment =
		// sortByPayment();
		// sortByLocation();

		// return
		// createJSONListFromResults(matchJobsForWorkerSortedByCustomerRating,
		// matchJobsForWorkerSortedByPayment);

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

	private List<NewCustomerWork> sortByPayment() {
		List<NewCustomerWork> matchJobsForWorkerSortedByPayment = new ArrayList<NewCustomerWork>(
				matchingJobsForWorker.values());
		Collections.sort(matchJobsForWorkerSortedByPayment, new Comparator<NewCustomerWork>() {

			public int compare(NewCustomerWork o1, NewCustomerWork o2) {
				return Integer.parseInt(o1.getCustomerRating()) - Integer.parseInt(o2.getCustomerRating());
			}
		});
		return matchJobsForWorkerSortedByPayment;

	}

	private void sortByLocation() {
		// Show the location which is nearest to customer location

	}

	private List<NewCustomerWork> sortByCustomerRating() {
		List<NewCustomerWork> matchJobsForWorkerSortedByCustomerRating = new ArrayList<NewCustomerWork>(
				matchingJobsForWorker.values());
		Collections.sort(matchJobsForWorkerSortedByCustomerRating, new Comparator<NewCustomerWork>() {

			public int compare(NewCustomerWork o1, NewCustomerWork o2) {
				return ((int) Double.parseDouble(o1.getCustomerRating()))
						- ((int) Double.parseDouble(o2.getCustomerRating()));
				// return Integer.parseInt() -
				// Integer.parseInt(o2.getCustomerRating());
			}
		});
		return matchJobsForWorkerSortedByCustomerRating;

	}

	private boolean matchSkill(String capability, String workerSkill) {

		if (workerSkill.equals("babysitting") || workerSkill.equals("cooking")) {
			return workerSkill.equals(capability);
		}
		if (workerSkill.equals(GENERAL_LABOR_SKILL)) {
			if (capability.equals("runanerrand") || capability.equals("packageDelivery")) {
				return true;
			}

		}
		return false;
	}

	private boolean matchPayment(String payOut, Integer payInRange) {
		return true;

		/*
		 * int diff = Integer.parseInt(payOut) - payInRange; if (diff < 0) {
		 * diff = diff * (-1); }
		 * 
		 * int payOutDiffPercent = (diff / payInRange) * 100;
		 * 
		 * if (payOutDiffPercent <= PAYMENT_DIFF_THRESHOLD) { return true; }
		 */
		// return false;
	}

	private boolean matchLocation(String workLocation, String workerLocation, Integer locationRange) {

		// Return true if both locations match
		if (workerLocation.equals(workLocation)) {
			return true;
		}
		// Else go for comparing if the location falls in the location range (x
		// KM) range

		return false;
	}

}
