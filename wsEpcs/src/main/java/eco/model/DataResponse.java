package eco.model;

public class DataResponse {
	int status;
	String message;
	String accTrans;
	String mesgAcc;

	/*
	 * Getter and setter
	 */
	public String getAccTrans() {
		return accTrans;
	}
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
	public void setAccTrans(String accTrans) {
		this.accTrans = accTrans;
	}
	public String getMesgAcc() {
		return mesgAcc;
	}
	public void setMesgAcc(String mesgAcc) {
		this.mesgAcc = mesgAcc;
	}
	
}
