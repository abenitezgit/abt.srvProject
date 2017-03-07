package abt.srvProject.model;

public class Servicio {
	int txpMain;
	int txtKeep;
	int srvPort;
	int ageShowHour;
	int ageGapMinute;
	String authKey;
	String dbType;
	String dbHost;
	int dbPort;
	String dbUser;
	String dbPass;
	String dbName;
	String dbInstance;
	String pathProperties;
	boolean enable;
	String log4jName;
	String log4jPath;
	
	public String getLog4jPath() {
		return log4jPath;
	}
	public void setLog4jPath(String log4jPath) {
		this.log4jPath = log4jPath;
	}
	public String getLog4jName() {
		return log4jName;
	}
	public void setLog4jName(String log4jName) {
		this.log4jName = log4jName;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getPathProperties() {
		return pathProperties;
	}
	public void setPathProperties(String pathProperties) {
		this.pathProperties = pathProperties;
	}
	public int getSrvPort() {
		return srvPort;
	}
	public void setSrvPort(int srvPort) {
		this.srvPort = srvPort;
	}
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public int getTxtKeep() {
		return txtKeep;
	}
	public void setTxtKeep(int txtKeep) {
		this.txtKeep = txtKeep;
	}
	public int getAgeShowHour() {
		return ageShowHour;
	}
	public void setAgeShowHour(int ageShowHour) {
		this.ageShowHour = ageShowHour;
	}
	public int getAgeGapMinute() {
		return ageGapMinute;
	}
	public void setAgeGapMinute(int ageGapMinute) {
		this.ageGapMinute = ageGapMinute;
	}
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPass() {
		return dbPass;
	}
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbInstance() {
		return dbInstance;
	}
	public void setDbInstance(String dbInstance) {
		this.dbInstance = dbInstance;
	}
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

}
