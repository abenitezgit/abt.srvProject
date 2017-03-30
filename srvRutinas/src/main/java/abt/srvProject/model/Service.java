package abt.srvProject.model;

import java.util.ArrayList;
import java.util.List;


public class Service {
	//Variables de identificacion del servicio
	String srvDesc;
	String srvId;
	String srvIp;
	int srvPort;
	
	//Variables de Control
	boolean enable;
	boolean activePrimaryMonitor;
	int orderBalance;
	int pctBalance;
	
	//Variables de Asignacion de Procesos y Clientes
	List<AssignedTypeProc> lstTypeProc = new ArrayList<>();
	List<AssignedCliProc> lstCliProc = new ArrayList<>();
	
	//Getter and Setter
	public String getSrvId() {
		return srvId;
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
	public List<AssignedTypeProc> getLstTypeProc() {
		return lstTypeProc;
	}
	public void setLstTypeProc(List<AssignedTypeProc> lstTypeProc) {
		this.lstTypeProc = lstTypeProc;
	}
	public List<AssignedCliProc> getLstCliProc() {
		return lstCliProc;
	}
	public void setLstCliProc(List<AssignedCliProc> lstCliProc) {
		this.lstCliProc = lstCliProc;
	}
	public String getSrvDesc() {
		return srvDesc;
	}
	public void setSrvDesc(String srvDesc) {
		this.srvDesc = srvDesc;
	}
	public int getOrderBalance() {
		return orderBalance;
	}
	public void setOrderBalance(int orderBalance) {
		this.orderBalance = orderBalance;
	}
	public int getPctBalance() {
		return pctBalance;
	}
	public void setPctBalance(int pctBalance) {
		this.pctBalance = pctBalance;
	}
	
	//Procedimiento internos
	
	public synchronized void addCliProc(AssignedCliProc acp) {
		getLstCliProc().add(acp);
	}
	
	
	public synchronized void addTypeProc(AssignedTypeProc atp) {
		getLstTypeProc().add(atp);
	}
	
	
}
