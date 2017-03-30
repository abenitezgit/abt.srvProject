package abt.srvProject.model;

import java.util.Date;

public class Task {
	String procID;
	String numSecExec;
	String typeProc;
	String srvID;
	Date fecIns;
	Date fecStart;
	Date fecFinished;
	String status;
	String uStatus;
	int errCode;
	String errMesg;
	Object param;
	
	//Getter and Setter
	
	public String getProcID() {
		return procID;
	}
	public void setProcID(String procID) {
		this.procID = procID;
	}
	public String getNumSecExec() {
		return numSecExec;
	}
	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}
	public String getTypeProc() {
		return typeProc;
	}
	public void setTypeProc(String typeProc) {
		this.typeProc = typeProc;
	}
	public String getSrvID() {
		return srvID;
	}
	public void setSrvID(String srvID) {
		this.srvID = srvID;
	}
	public Date getFecIns() {
		return fecIns;
	}
	public void setFecIns(Date fecIns) {
		this.fecIns = fecIns;
	}
	public Date getFecStart() {
		return fecStart;
	}
	public void setFecStart(Date fecStart) {
		this.fecStart = fecStart;
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
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	
	
}
