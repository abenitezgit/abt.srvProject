package eco.services;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Properties;

import common.dataAccess.mysqlDB;
import common.rutinas.Rutinas;
import eco.model.DataRequest;
import eco.model.DataResponse;
import eco.utiles.GlobalArea;

public class AccionService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataRequest dr = new DataRequest();
	DataResponse dResp = new DataResponse();
	final String proyecto = "wsEpcs";
	
	//Parametros de configuracion
	String host;
	String dbPort;
	String dbName;
	String dbUser;
	String dbPass;
	
	//Flags
	boolean flagInitComponents = false;
	boolean flagRowExist = false;
	
	//Getter and setter
	public boolean isInitComponents() {
		return flagInitComponents;
	}
	
	public boolean isRowExist() {
		return flagRowExist;
	}
	
	public DataResponse getDataResponse() {
		return dResp;
	}
		
	//Procedimientos privados
    private void fillDataRequest(String dataInput) throws Exception {
    	try {
    		dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    	} catch (Exception e) {
    		mylib.console(1,proyecto,"Error fillDataRequest: "+e.getMessage());
    		throw new Exception(e.getMessage());
    	}
    }
    
    private void setConfig(String fileConfig) throws Exception {
    	try {
    		Properties cfg = new Properties();
    		cfg.load(new FileInputStream(fileConfig));
    		
    		host = cfg.getProperty("host");
    		dbPort = cfg.getProperty("dbPort");
    		dbName = cfg.getProperty("dbName");
    		dbUser = cfg.getProperty("dbUser");
    		dbPass = cfg.getProperty("dbPass");
    		
    		cfg.clear();
    		
    	} catch (Exception e) {
			mylib.console(1,proyecto,"Error setConfig: "+e.getMessage());
			throw new Exception(e.getMessage());
    	}
    }
    
    private String getQueryAccion() throws Exception {
    	try {
    		String metodo = dr.getMetodo();
    		String vQuery = "";
    		
    		switch(metodo) {
	    		case "AccionEnt":
	    			if (!mylib.isNullOrEmpty(dr.getEntAct())) {
	    				vQuery = "select "
	    						+ " accion_transferencia as accTrans, "
	    						+ " mensaje_accion_audio as mesgAcc "
	    						+ "from "
	    						+ "	entidades "
	    						+ "where "
	    						+ " entidad = '"+dr.getEntAct()+"'";
	    			} else {
	    				throw new Exception("No vienen todos los parametros de entrada con datos");
	    			}
	    			break;
	    		case "AccionSeg":
	    			if (!mylib.isNullOrEmpty(dr.getEntAct()) && !mylib.isNullOrEmpty(dr.getCantEnt()) && !mylib.isNullOrEmpty(dr.getIntEnc()) ) {
	    				vQuery = "select "
	    						+ " accion_transferencia as accTrans, "
	    						+ " mensaje_accion_audio as mesgAcc "
	    						+ "from "
	    						+ "	segmentacion "
	    						+ "where "
	    						+ " entidad = '"+dr.getEntAct()+"' and "
	    						+ " flag = '"+dr.getCantEnt()+"' and "
	    						+ " intencion = '"+dr.getIntEnc()+"'";
	    			} else {
	    				throw new Exception("No vienen todos los parametros de entrada con datos");
	    			}
	    			break;
    		}
    		
    		return vQuery;
    	} catch (Exception e) {
			mylib.console(1,proyecto,"Error getQueryAccion: "+e.getMessage());
			throw new Exception(e.getMessage());
    	}
    }
	
	//Procedimientos publicos
	public void initComponents(String dataInput) throws Exception {
		try {
			fillDataRequest(dataInput);
			
			setConfig(gDatos.getFileConfig());
			
			flagInitComponents = true;
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error initComponents: "+e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void getAccion() throws Exception {
		try {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				String vSql = getQueryAccion();
				mylib.console(proyecto,"Query a ejecutar: "+vSql);
				if (myConn.executeQuery(vSql)) {
					mylib.console(proyecto,"Query Ejecutada Exitosamente");
					ResultSet rs = myConn.getQuery();
					if (rs.next()) {
						mylib.console(proyecto,"Se retornó un recorset");
						dResp.setAccTrans(rs.getString("accTrans"));
						dResp.setMesgAcc(rs.getString("mesgAcc"));
						flagRowExist = true;
					} else {
						mylib.console(2, proyecto,"Se retornó un recorset NULO");
					}
					rs.close();
				} else {
					mylib.console(1, proyecto,"Error getAccion(): No pudo ejecutar query");
				}
				myConn.close();
			} else {
				mylib.console(1, proyecto,"Error getAccion(): No pudo conectarse a BD");
			}
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error initComponents: "+e.getMessage());
			throw new Exception(e.getMessage());
		}
		
	}

}
