package abt.srvProject.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.model.Module;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;

public class srvMonitor extends Thread{
	static final String MODULE="srvMonitor";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	
	//Control de Ejecucion del servicio
	boolean init;
	static int TxP;
	
	public srvMonitor(GlobalArea m) {
		try {
			gDatos = m;
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
    		TxP = gDatos.getInfo().getTxpMain()/1000;
	        Timer timerMain = new Timer("thSrvMonitor");
	        timerMain.schedule(new mainTask(), 5000, gDatos.getInfo().getTxpMain()	);
	        logger.info("Servicio "+MODULE+" agendado cada: "+TxP+ " segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }
    
    static class mainTask extends TimerTask {
    	Map<String, Boolean> mapThread = new HashMap<>();
    	Module module = new Module();
    	Thread thListener;
    	Thread thProcess;
    	Thread thDBAccess;

        //Constructor de la clase
        public mainTask() {
        	module.setName(MODULE);
        	module.setType("TIMERTASK");
        	module.setTxp(TxP);
        }
        
        @SuppressWarnings("deprecation")
		public void run() {
        	String threadName;
        	try {
        		/**
        		 * Inicia ciclo del Modulo
        		 */
        		logger.info("Iniciando ciclo "+MODULE);
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		
        		/**
        		 * Analiza los Thread activos y los registra en map Local mapThread
        		 */
        		logger.info("Actualizando status Threads activos");
        		actualizaStatusThread();
        		
        		
        		/**
        		 * Levanta Listener
        		 */
        		threadName = "thListener";
                try {
                    if (!mapThread.get(threadName)) {
                    	logger.info("Iniciando Thread "+threadName);
                        thListener = new ThListener(gDatos);
                        thListener.setName(threadName);
                        thListener.start();
                    } 
                } catch (Exception e) {
                    mapThread.replace(threadName, false);
                    logger.error("Error al Iniciar Thread: "+threadName+" ("+e.getMessage()+")");
                    if (thListener.isAlive()) {
                    	thListener.destroy();
                    }
                }
        		

        		/**
        		 * Levanta srvProcess
        		 */
                threadName = "thProcess";
                try {
                    if (!mapThread.get(threadName)) {
                    	logger.info("Iniciando Thread "+threadName);
                    	thProcess = new ThProcess(gDatos);
                    	thProcess.setName(threadName);
                    	thProcess.start();
                    } 
                } catch (Exception e) {
                    mapThread.replace(threadName, false);
                    logger.error("Error al Iniciar Thread: "+threadName+" ("+e.getMessage()+")");
                    if (thProcess.isAlive()) {
                    	thProcess.destroy();
                    }
                }

        		/**
        		 * Levanta ThDBAccess
        		 */
                threadName = "thDBAccess";
                try {
                    if (!mapThread.get(threadName)) {
                    	logger.info("Iniciando Thread "+threadName);
                    	thProcess = new ThDBAccess(gDatos);
                    	thProcess.setName(threadName);
                    	thProcess.start();
                    } 
                } catch (Exception e) {
                    mapThread.replace(threadName, false);
                    logger.error("Error al Iniciar Thread: "+threadName+" ("+e.getMessage()+")");
                    if (thProcess.isAlive()) {
                    	thProcess.destroy();
                    }
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
        
        private void actualizaStatusThread() {
            
        	mapThread.put("thListener", false);
        	mapThread.put("thSync", false);
        	mapThread.put("thProcess", false);
        	mapThread.put("thDBAccess", false);
            
            //Thread tr = Thread.currentThread();
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            //System.out.println("Current Thread: "+tr.getName()+" ID: "+tr.getId());
            for ( Thread t : threadSet){
                //System.out.println("Thread :"+t+":"+"state:"+t.getState()+" ID: "+t.getId());
                if (t.getName().equals("thListener")) {
                	mapThread.replace("thListener", true);
                }
                if (t.getName().equals("thSync")) {
                	mapThread.replace("thSync", true);
                }
                if (t.getName().equals("thProcess")) {
                	mapThread.replace("thProcess", true);
                }
                if (t.getName().equals("thDBAccess")) {
                	mapThread.replace("thDBAccess", true);
                }
            }
            
            /**
             * Informa Threads encontrados
             */
            for (Map.Entry<String, Boolean> entry : mapThread.entrySet()) {
                logger.info("Thread analizado: " + entry.getKey() + ", valor=" + entry.getValue());
            }
        }
    }
}
