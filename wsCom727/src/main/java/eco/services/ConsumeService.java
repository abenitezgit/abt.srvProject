package eco.services;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Properties;

import common.dataAccess.mysqlDB;
import common.rutinas.Rutinas;
import eco.model.DataRequest;
import eco.model.DataResponse;
import eco.model.Establecimiento;
import eco.utiles.GlobalArea;


public class ConsumeService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataRequest dr = new DataRequest();
	DataResponse dResp = new DataResponse();
	final String proyecto = "wsCom727";
	
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
    
    private String getQueryComuna(int tc) throws Exception {
    	try {
    		String vQuery = "";
    		
    		switch(tc) {
	    		case 1:
    				vQuery = "select "
    						+ " establec as ESTABLEC, "
    						+ " tipo_establec as TIPO_ESTABLEC, "
    						+ " comuna as COMUNA, "
    						+ " direccion as DIRECCION, "
    						+ " telefono as TELEFONO "
    						+ "from "
    						+ "	tb_establec "
    						+ "where "
    						+ " establec = '"+dr.getEstablec()+"'";
	    			break;
	    		case 2:
    				vQuery = "select "
    						+ " establec as ESTABLEC, "
    						+ " tipo_establec as TIPO_ESTABLEC, "
    						+ " comuna as COMUNA, "
    						+ " direccion as DIRECCION, "
    						+ " telefono as TELEFONO "
    						+ "from "
    						+ "	tb_establec "
    						+ "where "
    						+ " establec = '"+dr.getEstablec()+"' "
    						+ " and comuna = '"+dr.getComuna()+"'";
	    			break;
    		}
    		
    		return vQuery;
    	} catch (Exception e) {
			mylib.console(1,proyecto,"Error getQueryComuna: "+e.getMessage());
			throw new Exception(e.getMessage());
    	}
    }
	
	//Procedimientos publicos
    
    public int getTipoConsulta() throws Exception {
    	try {
    		if (dr.getMetodo().equals("select")) {
    			if (!mylib.isNullOrEmpty(dr.getEstablec())) {
    				if (!mylib.isNullOrEmpty(dr.getComuna())) {
    					return 2; //Query Establec+Comuna
    				} else {
    					return 1; //Query Establec.
    				}
    			} else {
    				return -1;
    			}
    		} else {
    			return -1;
    		}
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error getTipoConsulta: "+e.getMessage());
			throw new Exception(e.getMessage());
		}
    }
    
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
	
	public void getSelect(int tipoConsulta) throws Exception {
		try {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				String vSql = getQueryComuna(tipoConsulta);
				mylib.console(proyecto,"Query a ejecutar: "+vSql);
				if (myConn.executeQuery(vSql)) {
					mylib.console(proyecto,"Query Ejecutada Exitosamente");
					ResultSet rs = myConn.getQuery();
					
					Establecimiento establec = new Establecimiento();
					
					if (rs!=null) {
						while (rs.next()) {
							establec = new Establecimiento();
							establec.setEstablec(rs.getString("ESTABLEC"));
							establec.setTipoEstab(rs.getString("TIPO_ESTABLEC"));
							establec.setComuna(rs.getString("COMUNA"));
							establec.setDireccion(rs.getString("DIRECCION"));
							establec.setTelefono(rs.getString("TELEFONO"));
							dResp.getLstEstablec().add(establec);
							flagRowExist = true;
						}
						
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
