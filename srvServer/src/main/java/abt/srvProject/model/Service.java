package abt.srvProject.model;

import java.util.ArrayList;
import java.util.List;

public class Service {
	//Variables de identificacion del servicio
	String srvId;
	String srvIp;
	int srvPort;
	
	//Variable de Asignacion de procesos
	List<AssignedTypeProc> lstTypeProc = new ArrayList<>();
	List<String> lstCli = new ArrayList<>();
	
	//Variables de Control
	boolean enable;
	boolean activePrimaryMonitor;
	boolean accessMetaData;
	
	//Getter and Setter
	
	public String getSrvId() {
		return srvId;
	}
	public List<AssignedTypeProc> getLstTypeProc() {
		return lstTypeProc;
	}
	public void setLstTypeProc(List<AssignedTypeProc> lstTypeProc) {
		this.lstTypeProc = lstTypeProc;
	}
	public List<String> getLstCli() {
		return lstCli;
	}
	public void setLstCli(List<String> lstCli) {
		this.lstCli = lstCli;
	}
	public boolean isAccessMetaData() {
		return accessMetaData;
	}
	public void setAccessMetaData(boolean accessMetaData) {
		this.accessMetaData = accessMetaData;
	}
	public void setSrvId(String srvId) {
		this.srvId = srvId;
	}
	public String getSrvIp() {
		return srvIp;
	}
	public void setSrvIp(String srvIp) {
		this.srvIp = srvIp;
	}
	public int getSrvPort() {
		return srvPort;
	}
	public void setSrvPort(int srvPort) {
		this.srvPort = srvPort;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isActivePrimaryMonitor() {
		return activePrimaryMonitor;
	}
	public void setActivePrimaryMonitor(boolean activePrimaryMonitor) {
		this.activePrimaryMonitor = activePrimaryMonitor;
	}
}
