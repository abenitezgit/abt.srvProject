package abt.srvProject.service;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import abt.srvProject.dataAccess.MetaData;
import abt.srvProject.model.DbParam;
import abt.srvProject.model.Etl;
import abt.srvProject.model.Module;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThExecEtl extends Thread {
	static final String MODULE="ThExecEtl";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	static Procedures myproc;
	Module module = new Module();
	
	//Control de Ejecucion del servicio
	boolean init;
	
	//Task
	static Task task;
	
	public ThExecEtl(GlobalArea m, Task ntask) {
		try {
			gDatos = m;
			task = ntask;
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
				
				//Set Module params
	        	module.setName(MODULE);
	        	module.setType("THREAD");
	        	module.setTxp(0);

				//Instanciando clase de Procedures
	        	myproc = new Procedures(gDatos);
	        	
				init = true;
			} else {
				mylib.console(1,"Archivo no encontrado: "+gDatos.getInfo().getPathProperties()+gDatos.getInfo().getLogProperties());
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
            try {
            	/**
            	 * Iniciando Thread 
            	 */
                logger.info("Iniciando Task ("+ task.getTypeProc()+ ") : "  + task.getKey());
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		
        		//Actualiza Status Task Global
        		gDatos.updateStatusTask(task.getKey(), "RUNNING");
        		
        		//Valida parametros informados en objeto Task.
        		if (myproc.validaTaskParam(task)) {
        			//Extrae Objeto ETL
        			Etl etl = new Etl();
        			
        			String etlString = mylib.serializeObjectToJSon(task.getParam(), false);
        			etl = (Etl) mylib.serializeJSonStringToObject(etlString, Etl.class);
        			
        			//Crea objetos de conexión a BD Destino
        			DbParam dbParamDestino = new DbParam();
        			dbParamDestino.setDbHost(etl.getdIp());
        			dbParamDestino.setDbInstance(etl.getdDbInstance());
        			dbParamDestino.setDbPort(etl.getdDbPort());
        			dbParamDestino.setDbType(etl.getdDbType());
        			dbParamDestino.setDbName(etl.getdDbName());
        			dbParamDestino.setDbPass(etl.getdUserPass());
        			dbParamDestino.setDbUser(etl.getdUserName());
        			MetaData dbConnDest = new MetaData(dbParamDestino);

        			//Crea objetos de conexión a BD Source
        			DbParam dbParamOrigen = new DbParam();
        			dbParamOrigen.setDbHost(etl.getsIp());
        			dbParamOrigen.setDbInstance(etl.getsDbInstance());
        			dbParamOrigen.setDbPort(etl.getsDbPort());
        			dbParamOrigen.setDbType(etl.getsDbType());
        			dbParamOrigen.setDbName(etl.getsDbName());
        			dbParamOrigen.setDbPass(etl.getsUserPass());
        			dbParamOrigen.setDbUser(etl.getsUserName());
        			MetaData dbConnSource = new MetaData(dbParamOrigen);
        			
        			//Muestra Parametros obtenidos
        			logger.info("Parametros Recuperados");
        			logger.info("DBOrigen Host: "+dbParamOrigen.getDbHost());
        			logger.info("DBOrigen Port: "+dbParamOrigen.getDbPort());
        			logger.info("DBOrigen Type: "+dbParamOrigen.getDbType());
        			logger.info("DBOrigen Name: "+dbParamOrigen.getDbName());
        			logger.info("DBOrigen User: "+dbParamOrigen.getDbUser());
        			logger.info("DBOrigen Pass: "+dbParamOrigen.getDbPass());
        			logger.info("----");
        			logger.info("DBDest Host: "+dbParamDestino.getDbHost());
        			logger.info("DBDest Port: "+dbParamDestino.getDbPort());
        			logger.info("DBDest Type: "+dbParamDestino.getDbType());
        			logger.info("DBDest Name: "+dbParamDestino.getDbName());
        			logger.info("DBDest User: "+dbParamDestino.getDbUser());
        			logger.info("DBDest Pass: "+dbParamDestino.getDbPass());
        			
        			//Establce conexiones a las BD
        			
        			dbConnDest.open();
        			if (dbConnDest.isConnected()) {
        				dbConnSource.open();
        				if (dbConnSource.isConnected()) {
        					//Conexion exitosa a las dos bases de datos.
        					logger.info("Conectado a Bases Origen y Destino");
        					
        					
        					
        					
        					
        				} else {
        					logger.error("No es posible conectarse a BD Origen: "+dbParamOrigen.getDbHost()+":"+dbParamOrigen.getDbName());
        				}
        			} else {
        				logger.error("No es posible conectarse a BD Destino: "+dbParamDestino.getDbHost()+":"+dbParamDestino.getDbName());
        			}
        		} else {
        			logger.error("No se ha podido validar los paremtros de entrada");
        		}
                
                /**
                 * Terminano Thread
                 */
        		module.setLastFecFin(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		logger.info("Finalizando Task ("+ task.getTypeProc()+ ") : "  + task.getKey());

                
            } catch (Exception  e) {
                logger.error("Error general en "+MODULE+" ("+e.getMessage()+")");
                /**
                 * Terminano Thread
                 */
                Date today;
                SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                today = new Date();
        		module.setLastFecFin(formatter.format(today));
        		gDatos.getMapModule().put(MODULE, module);
        		logger.info("Finalizando Task ("+ task.getTypeProc()+ ") : "  + task.getKey());
            }
            
    	
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }    
}
