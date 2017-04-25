package abt.srvProject.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcControl {
	String grpID;
	String numSecExec;
	String procID;
	String typeProc;
	int order;
	Date fecIns;
	Date fecUpdate;
	Date fecFinished;
	String status;
	String uStatus;
	int errCode;
	String errMesg;
	List<String> dependences = new ArrayList<>();
	Object param;
	
	//Getter and Setter
	
	public String getGrpID() {
		return grpID;
	}
	public Date getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(Date fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public String getTypeProc() {
		return typeProc;
	}
	public void setTypeProc(String typeProc) {
		this.typeProc = typeProc;
	}
	public List<String> getDependences() {
		return dependences;
	}
	public void setDependences(List<String> dependences) {
		this.dependences = dependences;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public void setGrpID(String grpID) {
		this.grpID = grpID;
	}
	public String getNumSecExec() {
		return numSecExec;
	}
	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
	}
	public String getProcID() {
		return procID;
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
