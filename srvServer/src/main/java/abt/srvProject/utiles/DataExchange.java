package abt.srvProject.utiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
			
			return mylib.msgResponse(0,"", data);
		} catch (IOException e) {
			return mylib.msgResponse(99, "Error proc: getStatus ("+e.getMessage()+")","");
		}
	}
	
}
