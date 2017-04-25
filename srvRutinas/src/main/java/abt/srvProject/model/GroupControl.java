package abt.srvProject.model;

import java.util.Date;

public class GroupControl {
	String procID;
	int order;
	Date fecIns;
	Date fecUpdate;
	Date fecFinished;
	String status;
	String uStatus;
	int errCode;
	String errMesg;
	
	//Getter and Setter
	
	public String getProcID() {
		return procID;
	}
	public Date getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(Date fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public void setProcID(String procID) {
		this.procID = procID;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Date getFecIns() {
		return fecIns;
	}
	public void setFecIns(Date fecIns) {
		this.fecIns = fecIns;
	}
	public Date getFecFinished() {
		return fecFinished;
	}
	public void setFecFinished(Date fecFinished) {
		this.fecFinished = fecFinished;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getuStatus() {
		return uStatus;
	}
	public void setuStatus(String uStatus) {
		this.uStatus = uStatus;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMesg() {
		return errMesg;
	}
	public void setErrMesg(String errMesg) {
		this.errMesg = errMesg;
	}
}
