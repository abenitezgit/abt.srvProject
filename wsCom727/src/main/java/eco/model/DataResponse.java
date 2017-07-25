package eco.model;

import java.util.ArrayList;
import java.util.List;

public class DataResponse {
	int status;
	String message;
	List<Establecimiento> lstEstablec = new ArrayList<>();

	/*
	 * Getter and setter
	 */
	
	public int getStatus() {
		return status;
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
	public List<Establecimiento> getLstEstablec() {
		return lstEstablec;
	}
	public void setLstEstablec(List<Establecimiento> lstEstablec) {
		this.lstEstablec = lstEstablec;
	}
}
