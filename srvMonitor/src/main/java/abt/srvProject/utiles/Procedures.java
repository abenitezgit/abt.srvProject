package abt.srvProject.utiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import abt.srvProject.srvRutinas.Rutinas;

public class Procedures {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public Procedures(GlobalArea m) {
		gDatos = m;
	}
	
	public String getStatus() {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("mapModule", gDatos.getMapModule());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse("OK", data, gDatos.getInfo().getAuthKey());
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
	}
}
