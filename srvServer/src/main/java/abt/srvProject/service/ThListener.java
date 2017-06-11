package abt.srvProject.service;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;

import abt.srvProject.model.Module;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.utiles.GlobalArea;
import abt.srvProject.utiles.Procedures;

public class ThListener extends Thread{
	static final String MODULE="ThListener";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	static Procedures myproc;
	Module module = new Module();
	
	//Control de Ejecucion del servicio
	boolean init;
	
	public ThListener(GlobalArea m) {
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
            	 * Iniciando Thread Listener
            	 */
                logger.info("Iniciando Listener Server port: " + gDatos.getInfo().getSrvPort());
        		module.setLastFecIni(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);

                ServerSocket skServidor = new ServerSocket(gDatos.getInfo().getSrvPort());
                String inputData;
                String outputData = null;
                String dRequest;
                String dAuth;
                JSONObject jHeader;
                JSONObject jData;
                
                while (gDatos.getService().isEnable()) {
                    Socket skCliente = skServidor.accept();
                    InputStream inpStr = skCliente.getInputStream();
                    ObjectInputStream objInput = new ObjectInputStream(inpStr);
                    
                    //Espera Entrada
                    //
                    try {
                    	inputData  = (String) objInput.readObject();
                    	logger.info("Recibiendo RX: "+inputData);
                        
                        jHeader = new JSONObject(inputData);
                        jData = jHeader.getJSONObject("data");
                        
                        dAuth = jHeader.getString("auth");
                        dRequest = jHeader.getString("request");

                        if (dAuth.equals(gDatos.getInfo().getAuthKey())) {
                        	//logger.info("Recibiendo TX("+ dRequest +"): "+ jData.toString());
                            switch (dRequest) {
                            	case "getStatus":
                            		outputData = myproc.sendStatus();
                            		break;
                            	case "getTask":
                            		outputData = myproc.sendTask();
                            		break;
                            	default:
                            		outputData = mylib.sendError(61);
                            }
                        } else {
                            outputData = mylib.sendError(60);
                        }
                    } catch (Exception e) {
                        outputData = mylib.sendError(90);
                    }
                    //Envia Respuesta
                    //
                    OutputStream outStr = skCliente.getOutputStream();
                    ObjectOutputStream ObjOutput = new ObjectOutputStream(outStr); 
                    
                    if (outputData==null) {
                    	outputData = mylib.sendError(90);
                    } 
                    
                    ObjOutput.writeObject(outputData);
                    logger.info("Enviando TX(): "+ outputData);
                    
                    //Cierra Todas las conexiones
                    //
                    inpStr.close();
                    ObjOutput.close();
                    objInput.close();
                    skCliente.close();
                }
                skServidor.close();
                
                /**
                 * Terminano Thread Listener
                 */
        		module.setLastFecFin(mylib.getDateNow());
        		gDatos.getMapModule().put(MODULE, module);
        		logger.info("Finalizando Listener!");

                
            } catch (Exception  e) {
                logger.error("Error general en "+MODULE+" ("+e.getMessage()+")");
            }
            
    	
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
    }    
}
