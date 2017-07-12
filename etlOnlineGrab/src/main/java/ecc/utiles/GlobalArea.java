package ecc.utiles;

public class GlobalArea {
	String grabDbHost;
	String grabDbPort;
	String grabDbName;
	String grabDbUser;
	String grabDbPass;
	String fileConfig;
	String clusterName;
	String log4j;
	int daysBack;
	int grabValLoad;
	int grabValUpd;

	//Getter and setter
	
	public String getGrabDbPort() {
		return grabDbPort;
	}
	public String getLog4j() {
		return log4j;
	}
	public void setLog4j(String log4j) {
		this.log4j = log4j;
	}
	public int getDaysBack() {
		return daysBack;
	}
	public void setDaysBack(int daysBack) {
		this.daysBack = daysBack;
	}
	public int getGrabValLoad() {
		return grabValLoad;
	}
	public void setGrabValLoad(int grabValLoad) {
		this.grabValLoad = grabValLoad;
	}
	public int getGrabValUpd() {
		return grabValUpd;
	}
	public void setGrabValUpd(int grabValUpd) {
		this.grabValUpd = grabValUpd;
	}
	public String getFileConfig() {
		return fileConfig;
	}
	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getGrabDbHost() {
		return grabDbHost;
	}
	public void setGrabDbHost(String grabDbHost) {
		this.grabDbHost = grabDbHost;
	}
	public void setGrabDbPort(String grabDbPort) {
		this.grabDbPort = grabDbPort;
	}
	public String getGrabDbName() {
		return grabDbName;
	}
	public void setGrabDbName(String grabDbName) {
		this.grabDbName = grabDbName;
	}
	public String getGrabDbUser() {
		return grabDbUser;
	}
	public void setGrabDbUser(String grabDbUser) {
		this.grabDbUser = grabDbUser;
	}
	public String getGrabDbPass() {
		return grabDbPass;
	}
	public void setGrabDbPass(String grabDbPass) {
		this.grabDbPass = grabDbPass;
	}
	
}
