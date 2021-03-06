package abt.srvProject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.model.Agenda;
import abt.srvProject.model.GroupControl;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Module;
import abt.srvProject.model.ProcControl;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThProcess extends Thread{
	static final String MODULE="ThProcess";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	static Procedures myproc;
	
	//Control de Ejecucion del servicio
	boolean init;
	static int TxP;
	
	public ThProcess(GlobalArea m) {
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
    		TxP = gDatos.getInfo().getTxpIns()/1000;
	        Timer timerMain = new Timer("thProcess");
	        timerMain.schedule(new mainTask(), 5000, TxP*1000);
	        logger.info("Servicio "+MODULE+" agendado cada: "+TxP+ " segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }
    
    static class mainTask extends TimerTask {
    	Map<String, Boolean> mapThread = new HashMap<>();
    	Module module = new Module();

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
        		
        		//Setea los Objetos a utilizar
        		List<Agenda> lstAgenda = new ArrayList<>();	//Contendrá la lista de Agendas Activas
        		List<Grupo> lstGrupo = new ArrayList<>(); //Lista de grupos potenciales a activar
        	
        		
        		//Pregunta si esta permitido realizar el proceso
        		//Si Thread DBAccess esta en operacion no se realiza este thread
        		if (gDatos.isSyncMetadata()) {
        		
	        		//Recupera Lista de Agendas Activas
	        		lstAgenda = myproc.getActiveAgendas();
	        		
	        		//Recupera Grupos Asociados a las Agendas
	        		if (lstAgenda.size()>0) {
		        		
	        			//Busca todos los grupos que potencialmente podrian ejecutarse de acuerdo a las
	        			//agendas encontradas en lstAgenda
		        		lstGrupo = myproc.getGrupos(lstAgenda);
		        		
		        		//Agrega los grupos encontrados a la cola de grupos activos
		        		myproc.appendColaGruposActivos(lstGrupo);
		        		
		        		mylib.console("Total Agendas encontradas: "+lstAgenda.size());
		        		mylib.console("Total de grupos encontrados: "+lstGrupo.size());
		        		
	        		} else {
	        			logger.info("No hay agendas activas en estos momentos");
	        		}
	        		
	        		//Actualiza el mapa de control de procesos
	        		/*
	        		 * Se actualizan los mapas mapGroupControl y mapProcControl
	        		 * los cuales son los objetos que deben sincronizarse con la BD para el control de proceso 
	        		 */
	    			myproc.appendProcessControl();
	        			
	    			//muestralstProcControl();
	        		
	        		//muestramapGroupControl();
	            		
	        		/**
	        		 * Crea las TASK correspondientes para ser enviadas a los servidores correspondientes
	        		 */
	        		myproc.appendNewTask();
	        		
	        		
	        		/**
	        		 * Analiza Task huerfanos recibidos de servicios
	        		 * Task que quedaron vigentes en servicios pero que no estan en master
	        		 * Se deben recrear los procesos procControl, groupControl y mapInterval (ETL's)
	        		 */

	        		
	        		
        		} else {
        			logger.info("Aun no se ha generado la sincronización con Metadata");
        		}

        		
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
    
    static void muestralstProcControl() {
    	for (Map.Entry<String, ProcControl> mapPc : gDatos.getMapProcControl().entrySet()) {
    		mylib.console("procControl: "+mapPc.getKey()+" - "+mapPc.getValue().getStatus());
    	}
    	
    }
    
    static void muestraMapTask() throws IOException {
    	for (Map.Entry<String, Task> mapT : gDatos.getMapTask().entrySet()) {
    		mylib.console("task: "+mapT.getKey()+" srvID: "+mapT.getValue().getSrvID()+" status: "+mapT.getValue().getStatus());
    		mylib.console("mapJson: "+mylib.serializeObjectToJSon(mapT.getValue(), false));
    	}
    }
    
    static void muestramapGroupControl() {
    	for (Map.Entry<String, GroupControl> entry : gDatos.getMapGroupControl().entrySet()) {
    		mylib.console("groupControl: "+entry.getKey()+"+"+entry.getValue().getStatus()+"+"+entry.getValue().getFecIns());
    	}
    }
    
    static void muestraListaGrupoJson(Map<String, Grupo> mapGrupo) {
    	for (Map.Entry<String, Grupo> entry : mapGrupo.entrySet()) {
    		try {
				mylib.console(mylib.serializeObjectToJSon(entry.getValue(), false));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    static void muestraListaGrupo(Map<String,Grupo> mapGrupo) {
		for (Map.Entry<String, Grupo> entry : mapGrupo.entrySet()) {
			mylib.console("GrpID: "+entry.getKey());
			mylib.console(">"+entry.getValue().getCliDesc());
			mylib.console(">"+entry.getValue().getCliID());
			mylib.console(">"+entry.getValue().getEnable());
			mylib.console(">"+entry.getValue().getGrpDESC());
			mylib.console(">"+entry.getValue().getGrpID());
			mylib.console(">"+entry.getValue().getHorDesc());
			mylib.console(">"+entry.getValue().getMaxTimeExec());
			mylib.console(">"+entry.getValue().getNumSecExec());
			mylib.console(">"+entry.getValue().getTypeBalance());
			
			for (int i=0; i<entry.getValue().getLstProcess().size(); i++) {
				mylib.console("   proc>"+entry.getValue().getLstProcess().get(i).getProcID());
				mylib.console("   proc>"+entry.getValue().getLstProcess().get(i).getnOrder());
				mylib.console("   proc>"+entry.getValue().getLstProcess().get(i).getType());
				mylib.console("   proc>"+entry.getValue().getLstProcess().get(i).getCritical());
			}
			
			for (int i=0; i<entry.getValue().getLstDepend().size(); i++) {
				mylib.console("   depend>"+entry.getValue().getLstDepend().get(i).getGrpID());
				mylib.console("   depend>"+entry.getValue().getLstDepend().get(i).getProcHijo());
				mylib.console("   depend>"+entry.getValue().getLstDepend().get(i).getProcPadre());
				mylib.console("   depend>"+entry.getValue().getLstDepend().get(i).getCritical());
			}
		}
    }
}
