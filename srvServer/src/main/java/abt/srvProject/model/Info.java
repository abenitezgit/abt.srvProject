package abt.srvProject.model;

public class Info {
	//Parametros de informacion del servicio
	String srvId;
	String srvIp;
	int srvPort;
	
	//Variables de identificacion del monitor (master y secondary)
	String srvMonIp;
	int srvMonPort;
	String srvSMonIp;
	int srvSMonPort;

	//Parametros de conrol de ciclo de schedule
	int txpMain;
	int txpSync;
	
	//Clave de intercambio de mensajes
	String authKey;

	//Parametros de archivo de properties y log
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
	public String getSrvMonIp() {
		return srvMonIp;
	}
	public void setSrvMonIp(String srvMonIp) {
		this.srvMonIp = srvMonIp;
	}
	public int getSrvMonPort() {
		return srvMonPort;
	}
	public void setSrvMonPort(int srvMonPort) {
		this.srvMonPort = srvMonPort;
	}
	public String getSrvSMonIp() {
		return srvSMonIp;
	}
	public void setSrvSMonIp(String srvSMonIp) {
		this.srvSMonIp = srvSMonIp;
	}
	public int getSrvSMonPort() {
		return srvSMonPort;
	}
	public void setSrvSMonPort(int srvSMonPort) {
		this.srvSMonPort = srvSMonPort;
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
	public String getAuthKey() {
		return authKey;
	}
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
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
