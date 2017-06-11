package abt.srvProject.utiles;

import abt.srvProject.model.DataRequest;

public class GlobalArea {
	Rutinas mylib = new Rutinas();
    
	String log4Config;
    String fileConfig;
    String hdpCluster;
    String hdpTbName;
    String solrCollection;
    DataRequest dr = new DataRequest();
    
    //Getter and Setter
    
	public String getLog4Config() {
		return log4Config;
	}

	public String getHdpTbName() {
		return hdpTbName;
	}

	public void setHdpTbName(String hdpTbName) {
		this.hdpTbName = hdpTbName;
	}

	public DataRequest getDr() {
		return dr;
	}

	public void setDr(DataRequest dr) {
		this.dr = dr;
	}

	public void setLog4Config(String log4Config) {
		this.log4Config = log4Config;
	}

    public String getFileConfig() {
		return fileConfig;
	}

	public void setFileConfig(String fileConfig) {
		this.fileConfig = fileConfig;
	}

	public String getHdpCluster() {
		return hdpCluster;
	}

	public void setHdpCluster(String hdpCluster) {
		this.hdpCluster = hdpCluster;
	}

	public String getSolrCollection() {
		return solrCollection;
	}

	public void setSolrCollection(String solrCollection) {
		this.solrCollection = solrCollection;
	}

	/**
     * Procedures
     */
	public boolean initComponent() {
		try {
			boolean isSuccess=true;
			
			loadFileConfig();
			
			if (!mylib.fileExist(getLog4Config())) { isSuccess = false; }
			if (!mylib.fileExist(getFileConfig())) { isSuccess = false; }

			
			return isSuccess;
		} catch (Exception e) {
			System.out.println("Error initComponent ("+e.getMessage()+")");
			e.printStackTrace();
			return false;
		}
	}
	
	public void loadFileConfig() throws Exception {
		try {
			log4Config = "/usr/local/hadoop/conf/log4j.properties";
	        fileConfig = "/usr/local/hadoop/conf/hadoop.properties";
	        hdpCluster = "ecoprod01";
	        hdpTbName  = "grabdata";
	        solrCollection = "collgrabdata";
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
                
}
