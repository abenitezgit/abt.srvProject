package abt.srvProject.model;

import java.util.HashMap;
import java.util.Map;

public class DataResponse {
	int status;
	String filters;
	String message;
	int numFound;
	int limit;
	HashMap<String, HashMap<String, String>> data = new HashMap<>();
	
	/*
	 * Getter and setter
	 */
	
	public int getStatus() {
		return status;
	}
	public HashMap<String, HashMap<String, String>> getData() {
		return data;
	}
	public void setData(HashMap<String, HashMap<String, String>> data) {
		this.data = data;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getNumFound() {
		return numFound;
	}
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}
