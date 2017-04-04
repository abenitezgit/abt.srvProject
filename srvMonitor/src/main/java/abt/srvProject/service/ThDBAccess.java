package abt.srvProject.service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import abt.srvProject.dataAccess.MetaData;
import abt.srvProject.dataAccess.MetaQuery;
import abt.srvProject.model.AssignedCliProc;
import abt.srvProject.model.AssignedTypeProc;
import abt.srvProject.model.Module;
import abt.srvProject.model.Service;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThDBAccess extends Thread{
	static final String MODULE="ThDBAccess";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	static Procedures myproc;
	
	//Control de Ejecucion del servicio
	boolean init;
	static int TxP;
	
	public ThDBAccess(GlobalArea m) {
		try {
			gDatos = m;
			myproc = new Procedures(gDatos);
			String pathFileLog4j=gDatos.getInfo().getPathProperties()+"/"+gDatos.getInfo().getLogProperties();
			if (mylib.fileExist(pathFileLog4j)) {
				PropertyConfigurator.configure(pathFileLog4j);
				logger.info("Constructor iniciado");
				logger.info("Logger Name: "+logger.getName());
				logger.info("Logger Level: "+mylib.getLoggerLevel(logger));
				logger.setLevel(Level.INFO);
				logger.info("Logger SET Level: "+mylib.getLoggerLevel(logger));
				logger.trace("Logger Trace Enable");
				logger.debug("Logger DEBUG Enable");
				logger.info("Logger INFO Enable");
				init = true;
			} else {
				mylib.console(1,"Archivo no encontrado: "+pathFileLog4j);
				init = false;
			}
		} catch (Exception e) {
			mylib.console(1,"Error en constructor "+MODULE+" ("+e.getMessage()+")");
			init = false;
		}
	}
	
    @Override
    public void run() {
    	if (init) {
    		TxP = gDatos.getInfo().getTxpDB()/1000;
	        Timer timerMain = new Timer("thDBAccess");
	        timerMain.schedule(new mainTask(), 5000, TxP*1000);
	        logger.info("Servicio "+MODULE+" agendado cada: "+TxP+ " segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }
    
    static class mainTask extends TimerTask {
    	Map<String, Boolean> mapThread = new HashMap<>();
    	Module module = new Module();
    	Thread thListener;

        //Constructor de la clase
        public mainTask() {
        	module.setName(MODULE);
        	module.setType("TIMERTASK");
        	module.setTxp(TxP);
        }
        
        public void run() {
        	int ciclo=0;
        	try {
        		/**
        		 * Inicia ciclo del Modulo
        		 */
        		logger.info("Iniciando ciclo "+MODULE);
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        	
        		
        		//Recupera Datos de Servicios
        		loadServicesParam();
        		
        		//Sincroniza GroupControl y ProcControl con DB
        		syncProcessControl();
        		
        		mylib.console("services: "+mylib.serializeObjectToJSon(gDatos.getMapService(), true));

        		
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.info("Terminado ciclo "+MODULE);
        		module.setLastFecFin(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);

        	} catch (Exception e) {
        		switch (ciclo) {
        		case 1:
        			logger.error("Error generando lista de agendas "+MODULE+" ("+e.getMessage()+")");
        			break;
    			default:
    				logger.error("Error inesperado "+MODULE+" ("+e.getMessage()+")");
    				break;
        		}
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.info("Terminado ciclo "+MODULE+" con Error");
        	}
        }
    }
    
    static private void syncProcessControl() throws Exception {
    	try {
    		MetaData dbConn = new MetaData(gDatos);
    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
    		
    		dbConn.open();
    		
    		if (dbConn.isConnected()) {
    			dbConn.close();
    		}
    		
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private void loadServicesParam() throws Exception {
    	try {
    		MetaData dbConn = new MetaData(gDatos);
    		MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
    		
    		dbConn.open();
    		
    		if (dbConn.isConnected()) {
    			String vSql = dbQuery.getSqlFindServices();
    			if (dbConn.executeQuery(vSql)) {
    				ResultSet rs = dbConn.getQuery();
    				Service service;
    				while (rs.next()) {
    					service = new Service();
    					service.setSrvId(rs.getString("srvID"));
    					service.setEnable(rs.getInt("srvEnable")==1);
    					service.setSrvDesc(rs.getString("srvDesc"));
    					service.setOrderBalance(rs.getInt("orderBalance"));
    					service.setPctBalance(rs.getInt("pctBalance"));
    					
    					asignaTypeProc(service, rs.getString("srvTypeProc"));
    					asignaCliProc(service, rs.getString("srvTypeProc"));
    					
    					logger.info("Agregando servicio 1: "+mylib.serializeObjectToJSon(service, false));
    					gDatos.updateMapService(service.getSrvId(), service);
    					
    				}
    				rs.close();
    			}
    			dbConn.close();
    		}
    		
    	} catch (Exception e) {
    		logger.error("Error loadServiceParam ("+e.getMessage()+")");
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private void asignaTypeProc(Service srv, String data) throws Exception {
    	try {
	    	JSONArray ja = new JSONObject(data).getJSONArray("lstProc");
	    	AssignedTypeProc atp;
	    	
	    	for (int i=0; i<ja.length(); i++) {
	    		atp = new AssignedTypeProc();
	    		
	    		atp.setTypeProc(ja.getJSONObject(i).getString("typeProc"));
	    		atp.setMaxThread(ja.getJSONObject(i).getInt("maxThread"));
	    		
	    		srv.addTypeProc(atp);
	    	}
    	} catch (Exception e) {
    		logger.error("Error asignaTypeProc ("+e.getMessage()+")");
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private void asignaCliProc(Service srv, String data) throws Exception {
    	try {
	    	JSONArray ja = new JSONObject(data).getJSONArray("lstCli");
	    	AssignedCliProc acp;
	
	    	for (int i=0; i<ja.length(); i++) {
	    		acp = new AssignedCliProc();
	    		
	    		acp.setCliID(ja.getString(i));
	    		
	    		srv.addCliProc(acp);
	    	}
    	} catch (Exception e) {
    		logger.error("Error asignaCliProc ("+e.getMessage()+")");
    		throw new Exception(e.getMessage());
    	}
    }

}
