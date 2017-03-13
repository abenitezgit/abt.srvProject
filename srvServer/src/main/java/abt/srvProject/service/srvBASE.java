package abt.srvProject.service;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

public class srvBASE extends Thread{
	static final String SERVICIO="srvMonitor";
	static Logger logger = Logger.getLogger(SERVICIO);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	
	//Control de Ejecucion del servicio
	boolean init;
	
	public srvBASE(GlobalArea m) {
		try {
			gDatos = m;
			if (mylib.fileExist(gDatos.getInfo().getPathProperties()+"log4j.properties")) {
				PropertyConfigurator.configure(gDatos.getInfo().getPathProperties()+"log4j.properties");
				logger.info("Constructor iniciado");
				init = true;
			} else {
				mylib.console(1,"Archivo no encontrado: "+gDatos.getInfo().getPathProperties()+"log4j.properties");
				init = false;
			}
		} catch (Exception e) {
			mylib.console(1,"Error en constructor "+SERVICIO+" ("+e.getMessage()+")");
			init = false;
		}
	}
	
    @Override
    public void run() {
    	if (init) {
	        Timer timerMain = new Timer("thSrvMonitor");
	        timerMain.schedule(new mainTask(), 5000, gDatos.getInfo().getTxpMain()	);
	        logger.info("Servicio "+SERVICIO+" agendado cada: "+gDatos.getInfo().getTxpMain()/1000+ " segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+SERVICIO);
    	}
    }
    
    static class mainTask extends TimerTask {

        //Constructor de la clase
        public mainTask() {
        }
        
        public void run() {
        	try {
        		logger.info("Iniciando ciclo srvMonitor");
        		
        		logger.info("Terminado ciclo srvMonitor");
        	} catch (Exception e) {
        		logger.error("Error inesperado srvMonitor ("+e.getMessage()+")");
        	}
        }
    }
}
