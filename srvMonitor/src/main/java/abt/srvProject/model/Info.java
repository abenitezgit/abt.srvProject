package abt.srvProject.model;

public class Info {
	//Parametros del servicio
	String srvId;
	String srvIp;
	int srvPort;
	
	//Parametros de control de tiempos de ejecucion
	int txpMain;
	int txpSync;
	
	//Parametros de recuperacion de agendas
	int ageShowHour;
	int ageGapMinute;
	
	//Parametro de intercambio de key
	String authKey;
	
	//Parametros de conexion hacia Metadata
	String dbType;
	String dbHost;
	int dbPort;
	String dbUser;
	String dbPass;
	String dbName;
	String dbInstance;
	
	//Parametros de archivos de properties y log
	String pathProperties;
	String fileProperties;
	String logProperties;
	
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
	public int getTxpMain() {
		return txpMain;
	}
	public void setTxpMain(int txpMain) {
		this.txpMain = txpMain;
	}
	public int getTxpSync() {
		return txpSync;
	}
	public void setTxpSync(int txpSync) {
		this.txpSync = txpSync;
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
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
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
	public String getPathProperties() {
		return pathProperties;
	}
	public void setPathProperties(String pathProperties) {
		this.pathProperties = pathProperties;
	}
	public String getFileProperties() {
		return fileProperties;
	}
	public void setFileProperties(String fileProperties) {
		this.fileProperties = fileProperties;
	}
	public String getLogProperties() {
		return logProperties;
	}
	public void setLogProperties(String logProperties) {
		this.logProperties = logProperties;
	}
}
