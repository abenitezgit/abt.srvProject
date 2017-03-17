package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.Service;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.Info;

public class GlobalArea {
	Rutinas mylib = new Rutinas();

	Info info;
	Service service = new Service();
	Map<String, Module> mapModule = new HashMap<>();
	
	//Getter and Setter
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Map<String, Module> getMapModule() {
		return mapModule;
	}

	public void setMapModule(Map<String, Module> mapModule) {
		this.mapModule = mapModule;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}
	
	//Procedimientos internos
	public synchronized void updateService(Map<String,Object> newSrv) throws Exception {
		
		for (Map.Entry<String, Object> entry : newSrv.entrySet()) {
			switch (entry.getKey()) {
			case "srvId":
				service.setSrvId((String) entry.getValue());
				break;
			case "srvIp":
				service.setSrvIp((String) entry.getValue());
				break;
			case "srvPort":
				service.setSrvPort((int) entry.getValue());
				break;
			case "enable":
				service.setEnable((boolean) entry.getValue());
				break;
			case "activePrimaryMonitor":
				service.setActivePrimaryMonitor((boolean) entry.getValue());
				break;
			}
		}
	}

}
