package abt.srvProject.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.model.Interval;
import abt.srvProject.model.Module;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThRunProcess extends Thread{
	static final String MODULE="ThRunProcess";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static Procedures myproc;
	static GlobalArea gDatos;
	
	//Control de Ejecucion del servicio
	boolean init;
	
	public ThRunProcess(GlobalArea m) {
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
	        Timer timerMain = new Timer("thRunProcess");
	        timerMain.schedule(new mainTask(), 5000, gDatos.getInfo().getTxpRun()	);
	        logger.info("Servicio "+MODULE+" agendado cada: "+gDatos.getInfo().getTxpRun()/1000+ " segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }
    
    static class mainTask extends TimerTask {
    	Map<String, Boolean> mapThread = new HashMap<>();
    	Module module = new Module();
    	
    	//Declaracion de Thread de procesos
    	Thread etlThread;
    	Thread movThread;

        //Constructor de la clase
        public mainTask() {
        	module.setName(MODULE);
        	module.setType("TIMERTASK");
        	module.setTxp(gDatos.getInfo().getTxpRun());
        }
        
        public void run() {
        	try {
        		/**
        		 * Inicia ciclo del Modulo
        		 */
        		logger.info("Iniciando ciclo "+MODULE);
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		
        		//Revisala cola de Task para actualizar mapa local de Task
        		int numTaskPool = getNumTaskPool();
        		logger.info("Total de Task en Cola al inicio de extraccion: "+numTaskPool);
        		if (numTaskPool>0) {
        			for (int i=0; i< numTaskPool; i++) {
        				addNewTaskPool();
        			}
        		} else {
        			//No hay nuevos task informados
        			logger.info("No hay nuevos Task informados");
        		}
        		logger.info("Total de Task en Cola al finalizar extraccion: "+getNumTaskPool());
        		
        		logger.info("Total de Task para su ejecución: "+getNumTaskMap());
        		muestraMapTask();
        		
        		//Revisa el Map de Ejecucion de Task
        		if (getNumTaskMap()>0) {
        			//Genera lista de Task en estado READY
        			Map<String, Task> mTask = gDatos.getMapTask()
							.entrySet()
							.stream()
							.filter(p -> p.getValue().getStatus().equals("READY") )
							.collect(Collectors.toMap(map -> map.getKey() , map -> map.getValue()));
        			if (mTask.size()>0) {
        				//Si es que hay Tareas en esta READY
        				//Recorre las task para asignar thread de proceso respectivo
        				for (Map.Entry<String, Task> entry : mTask.entrySet()) {
        					String typeProc = entry.getValue().getTypeProc();
        					switch (typeProc) {
	        					case "ETL":
	        						etlThread = new ThExecEtl(gDatos, entry.getValue());
	        						etlThread.setName("EtlThread-"+etlThread.getId());
	        						etlThread.start();
	        						logger.info("Se generó Thread de ejecución: "+etlThread.getName());
	        						break;
	        					case "MOV":
	        						movThread = new ThExecMov(gDatos, entry.getValue());
	        						movThread.setName("MovThread-"+movThread.getId());
	        						movThread.start();
	        						logger.info("Se generó Thread de ejecución: "+movThread.getName());
	        						break;
	    						default:
	    							break;
        					}
        					
        				}
        			}
        			
        		} else {
        			//No hay procesos para ejecutar
        			logger.info("No hay nuevos procesos para ejecutar");
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
    
    static private void muestraMapTask() {
    	for (Map.Entry<String, Task> mapTask : gDatos.getMapTask().entrySet()) {
    		logger.info("Task Process: "+mapTask.getKey()+" status: "+mapTask.getValue().getStatus());
    	}
    }
    
    static private int getNumTaskMap() throws Exception {
    	try {
    		int numTask = gDatos.getMapTask().size();
    		return numTask;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private int getNumTaskPool() throws Exception {
    	try {
    		int numTask = gDatos.getLkdTask().size();
    		return numTask;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    
    static private void addNewTaskPool() throws Exception {
    	try {
    		Task tsk = new Task();
    		tsk = gDatos.getItemTaskPool();
    		
    		String keyTask = tsk.getKey();
    		
    		logger.info("Se ha extraido el TaskPool: "+keyTask);
    		
    		if (!gDatos.getMapTask().containsKey(keyTask)) {
    			tsk.setStatus("READY");
    			tsk.setFecIns(mylib.getDate());
    			
    			if (tsk.getTypeProc().equals("ETL")) {
    				Interval interval = new Interval();
    				String strinInterval = mylib.serializeObjectToJSon(tsk.getTxSubTask(), false);
    				interval = (Interval) mylib.serializeJSonStringToObject(strinInterval, Interval.class);
    				interval.setFecIns(tsk.getFecIns());
    				interval.setStatus(tsk.getStatus());
    				tsk.setTxSubTask(interval);
    			}

    			gDatos.addNewTaskMap(tsk);
    			
    			logger.info("Se ingreso nuevo Task: "+keyTask);
    		} else {
    			//Task ya fue ingresado al map de ejecuion de Task
    		}
    		
    	} catch (Exception e) {
    		logger.error("Error addNewTaskPool ("+e.getMessage()+")");
    		throw new Exception(e.getMessage());
    	}
    }
}
