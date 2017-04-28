package abt.srvProject.utiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;

public class Procedures {
	static final String MODULE="Procedures";
	static Logger logger = Logger.getLogger(MODULE);
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public Procedures(GlobalArea m) {
		gDatos = m;
	}
	
	public void parseaTaskFromJson(Task task, JSONObject jTask) throws Exception {
		try {
			task.setErrCode(jTask.getInt("errCode"));
			task.setErrMesg(jTask.getString("errMesg"));
			//task.setFecFinished(jTask.getString("fecFinished"));
			
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String genMsgRequestSync() {
		JSONObject jHeader = new JSONObject();
		JSONObject jData = new JSONObject();
		
		try {
			//Prepara el MapTask de Respuesta
			Map<String, Task> mTSend = new HashMap<>(gDatos.getMapTask());
			for (Map.Entry<String, Task> loopTask : mTSend.entrySet()) {
				loopTask.getValue().setParam(null);
			}
			
			String service = mylib.serializeObjectToJSon(gDatos.getService(), false);
			String task = mylib.serializeObjectToJSon(mTSend, false);
			
			jData.put("service", service);
			jData.put("task", task);
			
			jHeader.put("data", jData);
			jHeader.put("auth", gDatos.getInfo().getAuthKey());
			jHeader.put("request", "syncService");
			
			return jHeader.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public String sendRequestMonitor(String dRequest) {
		try {
    		//Declara variables para valores de servicio monitor (master o secondary)
    		String srvMonDesc;
    		String srvMonIp;
    		int srvMonPort;
    		boolean isMaster;
    		String response = null;
    		int retrySyncConnect = 2; //Intentos de conexion a Primary o Secondary Monitor
    		
    		//Recupera Datos
    		isMaster = gDatos.getService().isActivePrimaryMonitor();
    		logger.info("ActivePrimaryMonitor: "+isMaster);
    		
    		//Intentos de conexion 
    		int it = 0;
    		while (it < retrySyncConnect) {
	    		//Actualiza datos de conexion en base a master o secondary monitor
	    		if (isMaster) {
	    			srvMonIp = gDatos.getInfo().getSrvMonIp();
	    			srvMonPort = gDatos.getInfo().getSrvMonPort();
	    			srvMonDesc = "Primary";
	    			
	    		} else {
	    			srvMonIp = gDatos.getInfo().getSrvSMonIp();
	    			srvMonPort = gDatos.getInfo().getSrvSMonPort();
	    			srvMonDesc = "Secondary";
	    		}
	    		logger.info("Conectando a Monitor: "+srvMonDesc);
	    		logger.info("MonIp: "+srvMonIp);
	    		logger.info("MonPort: "+srvMonPort);
	    		
	    		try {
					Socket skCliente = new Socket(srvMonIp, srvMonPort);
		            OutputStream aux = skCliente.getOutputStream(); 
		            ObjectOutputStream objOutput = new ObjectOutputStream(aux);
		            objOutput.writeObject(dRequest);
		            
		            InputStream inpStr = skCliente.getInputStream();
		            ObjectInputStream objInput = new ObjectInputStream(inpStr);
		            response = (String) objInput.readObject();
		            
		            it = retrySyncConnect;
	    		} catch (Exception e) {
	    			isMaster = !isMaster;
	    			Map<String,Object> srv = new HashMap<>();
	    			srv.put("activePrimaryMonitor", isMaster);
	    			gDatos.updateService(srv);
	    			it++;
	    		}
    		}
			
    		return response;
		} catch (Exception e) {
			logger.error("Error en sendRequestMonitor ("+e.getMessage()+")");
			return null;
		}
	}
	
	public String sendStatus() {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("mapModule", gDatos.getMapModule());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse(0,"", data);
		} catch (IOException e) {
			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
		}
	}
	
	public String sendService() {
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("service", gDatos.getService());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse(0,"", data);
		} catch (IOException e) {
			return mylib.msgResponse(99, "Error proc: getService ("+e.getMessage()+")","");
		}
	}
}
