package abt.srvProject.utiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import abt.srvProject.srvRutinas.Rutinas;

public class DataExchange {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public DataExchange(GlobalArea m) {
		gDatos = m;
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
			
			return mylib.msgResponse("OK", data);
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
	}
	
    public String sendProcControl() {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("lstProcControl", gDatos.getLstProcControl());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse("OK", data);
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
    	
    }
    
    

    public String sendGroupControl() {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("mapGroupControl", gDatos.getLstProcControl());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse("OK", data);
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
    	
    }

    public String sendService(String srvID) {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("typeProc", gDatos.getMapService().get(srvID).getLstTypeProc());
			mapResponse.put("cliProc", gDatos.getMapService().get(srvID).getLstCliProc());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse("OK", data);
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
    	
    }
    
    
	public String syncService(JSONObject jData) {
		try {
			JSONObject jService = new JSONObject(jData.getString("service"));
			
			
			Map<String, Object> mapSrv = jService.toMap();
			String srvId = (String) mapSrv.get("srvId");
			
			gDatos.updateService(mapSrv);
			
			return sendService(srvId);
			
		} catch (Exception e) {
			return mylib.sendError(99, "Error proc: syncService ("+e.getMessage()+")");
		}
	}




}
