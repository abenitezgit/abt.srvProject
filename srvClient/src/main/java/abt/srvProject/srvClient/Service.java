package abt.srvProject.srvClient;

public class Service {
	//Variables de identificacion del servicio
	String srvId;
	String srvIp;
	int srvPort;
	
	//Variables de Control
	boolean enable;
	boolean activePrimaryMonitor;
	
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
}
