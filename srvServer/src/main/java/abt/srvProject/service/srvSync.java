package abt.srvProject.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.model.Module;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class srvSync extends Thread{
	static final String MODULE="srvSync";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static Procedures myproc;
	static GlobalArea gDatos;
	
	//Control de Ejecucion del servicio
	boolean init;
	
	public srvSync(GlobalArea m) {
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
	        Timer timerMain = new Timer("thSync");
	        timerMain.schedule(new mainTask(), 5000, gDatos.getInfo().getTxpSync()	);
	        logger.info("Servicio "+MODULE+" agendado cada: "+gDatos.getInfo().getTxpSync()/1000+ " segundos");
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
        	module.setTxp(gDatos.getInfo().getTxpSync());
        }
        
        public void run() {
        	try {
        		/**
        		 * Inicia ciclo del Modulo
        		 */
        		logger.info("Iniciando ciclo "+MODULE);
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		
        		String dRequest = myproc.genMsgRequestSync();
        		logger.info("Data Request: "+dRequest);
        		
        		if (!mylib.isNull(dRequest)) {
        			String dResponse = myproc.sendRequestMonitor(dRequest);
        			logger.info("Data Response: "+dResponse);
        			
        			if (!mylib.isNull(dResponse)) {
        				//La respuesta es una actualización de:
        				//la habilitación del servicio
        				//la lista de clientes
        				//el map de procesos asignados
        				
        				
        			} else {
        				//Respuesta nula
        				logger.error("No hay respuesta desde Monitor");
        			}
        		} else {
        			//No se pudo generar SyncService
        			logger.error("Request mal generado");
        		}
        		
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.info("Terminado ciclo "+MODULE);
        		module.setLastFecFin(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);

        	} catch (Exception e) {
        		logger.error("Error inesperado "+MODULE+" ("+e.getMessage()+")");
        	}
        }
    }
}
