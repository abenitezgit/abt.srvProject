package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.Servicio;

public class GlobalArea {

	Servicio servicio;
	Map<String, Module> mapModule = new HashMap<>();
	
	public Map<String, Module> getMapModule() {
		return mapModule;
	}

	public void setMapModule(Map<String, Module> mapModule) {
		this.mapModule = mapModule;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	
}
