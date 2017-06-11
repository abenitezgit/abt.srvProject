package abt.srvProject.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import abt.srvProject.dataAccess.MetaData;
import abt.srvProject.dataAccess.MetaQuery;
import abt.srvProject.model.DbParam;
import abt.srvProject.model.Module;
import abt.srvProject.model.Mov;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThExecMov extends Thread {
	static final String MODULE="ThExecMov";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	static Procedures myproc;
	Module module = new Module();
	
	//Control de Ejecucion del servicio
	boolean init;
	
	//Task
	static Task task;
	
	public ThExecMov(GlobalArea m, Task ntask) {
		try {
			gDatos = m;
			task = ntask;
			String pathFileLog4j=gDatos.getInfo().getPathProperties()+"/"+gDatos.getInfo().getLogProperties();
			if (mylib.fileExist(pathFileLog4j)) {
				PropertyConfigurator.configure(pathFileLog4j);
				logger.info("Constructor iniciado");
				logger.info("Logger Name: "+logger.getName());
				logger.info("Logger Level: "+mylib.getLoggerLevel(logger));
				//logger.setLevel(Level.INFO);
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
        			//Extrae Objeto Mov
        			Mov mov = new Mov();
        			
        			String movString = mylib.serializeObjectToJSon(task.getParam(), false);
        			mov = (Mov) mylib.serializeJSonStringToObject(movString, Mov.class);
        			
        			//Crea objetos de conexión a BD Destino
        			DbParam dbParamDestino = new DbParam();
        			DbParam dbParamOrigen = new DbParam();
        			
        			myproc.setDbParamMovDestino(dbParamDestino, mov);
        			myproc.setDbParamMovSource(dbParamOrigen, mov);
        			
        			//Crea objetos de conexión a BD Source
        			MetaData dbConnSource = new MetaData(dbParamOrigen);
        			MetaData dbConnDest = new MetaData(dbParamDestino);
        			
        			
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
        					
        					//Genera Query de Extraccion
        					MetaQuery myQueryS = new MetaQuery(mov.getSDBTYPE());
        					String vSqlSource = myQueryS.getSqlQuerySource(mov);
        					
        					logger.info("Query de Extraccion generada: "+vSqlSource);
        					//Si create table está activo valida existencia de tabla destino o la crea
        					if (mov.getCreateDest()==1) {
        						if (!myproc.isTableExist(dbConnDest, mov.getDTBNAME(), mov.getDDBTYPE())) {
        							myproc.createTable(dbConnDest, mov);
        						}
        					}
        					
        					//Genera Query delete para reprocesar si es que append = 0
        					String vSqlDelete = myQueryS.getSqlDeleteRows(mov);
        					
        					logger.info("Query de borrado de registros: "+vSqlDelete);
        					
        					//Inicia proceso de copiado
        					//Ejecuta Query de Extraccion de Datos sobre BD Source
        					if (dbConnSource.executeQuery(vSqlSource)) {
        						if (mov.getAppend()==0) {
        							//Procede a borrar destino
        							int result = dbConnDest.executeUpdate(vSqlDelete);
        							logger.info("Se procedió a borrar: "+result+" registros de tabla destino");
        						}
        						
        						logger.info("Termino proceso");
        						int rowsLoad=0;
        						int rowsRead=0;
        						int maxRowsError = mov.getMaxRowsError();
        						int maxPctError = mov.getMaxPctError();
        						
        					}
        					
        					
        					
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
        		logger.info("Finalizando Exitoso Task ("+ task.getTypeProc()+ ") : "  + task.getKey());

                
            } catch (Exception  e) {
            	e.printStackTrace();
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
        		logger.info("Finalizando con Error Task ("+ task.getTypeProc()+ ") : "  + task.getKey());
            }
            
    	
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }    
}
