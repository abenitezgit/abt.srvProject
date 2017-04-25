package abt.srvProject.model;

import java.util.Date;

public class Interval {
	String procID;
	String intervalID;
	String fecIniInterval;
	String fecFinInterval;
	String numSecExec;
	String status;
	String uStatus;
	Date fecIns;
	Date fecUpdate;
	Date fecFinished;
	int rowsRead;
	int rowsLoad;
	int intentos;
	
	//Getter and setter

	public String getProcID() {
		return procID;
	}
	public String getFecIniInterval() {
		return fecIniInterval;
	}
	public void setFecIniInterval(String fecIniInterval) {
		this.fecIniInterval = fecIniInterval;
	}
	public String getFecFinInterval() {
		return fecFinInterval;
	}
	public void setFecFinInterval(String fecFinInterval) {
		this.fecFinInterval = fecFinInterval;
	}
	public void setProcID(String procID) {
		this.procID = procID;
	}
	public String getIntervalID() {
		return intervalID;
	}
	public void setIntervalID(String intervalID) {
		this.intervalID = intervalID;
	}
	public String getNumSecExec() {
		return numSecExec;
	}
	public void setNumSecExec(String numSecExec) {
		this.numSecExec = numSecExec;
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
	public Date getFecIns() {
		return fecIns;
	}
	public void setFecIns(Date fecIns) {
		this.fecIns = fecIns;
	}
	public Date getFecUpdate() {
		return fecUpdate;
	}
	public void setFecUpdate(Date fecUpdate) {
		this.fecUpdate = fecUpdate;
	}
	public Date getFecFinished() {
		return fecFinished;
	}
	public void setFecFinished(Date fecFinished) {
		this.fecFinished = fecFinished;
	}
	public int getRowsRead() {
		return rowsRead;
	}
	public void setRowsRead(int rowsRead) {
		this.rowsRead = rowsRead;
	}
	public int getRowsLoad() {
		return rowsLoad;
	}
	public void setRowsLoad(int rowsLoad) {
		this.rowsLoad = rowsLoad;
	}
	public int getIntentos() {
		return intentos;
	}
	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
	
	
}
