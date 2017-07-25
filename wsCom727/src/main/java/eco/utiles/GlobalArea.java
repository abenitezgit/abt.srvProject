package eco.utiles;

public class GlobalArea {
	String fileConfig;
	
	
	
	public String getFileConfig() {
		return fileConfig;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}



	public GlobalArea() {
		setFileConfig("/usr/local/epcs/conf/config.properties");
	}

}
