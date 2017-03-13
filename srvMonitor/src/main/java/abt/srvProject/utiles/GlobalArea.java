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
	Map<String, Service> mapService = new HashMap<>();
	Map<String, Module> mapModule = new HashMap<>();
	
	//Procedimientos internos
	public synchronized void updateService(Service newSrv) {
		Service service = new Service();
		
		if (mapService.containsKey(newSrv.getSrvId())) {
			service.setEnable(newSrv.isEnable());
			
			if (!mylib.isNull(newSrv.getSrvId())) {
				service.setSrvId(newSrv.getSrvId());
			}
			
			if (!mylib.isNull(newSrv.getSrvIp())) {
				service.setSrvIp(newSrv.getSrvIp());
			}
			
			if (newSrv.getSrvPort()!=0) {
				service.setSrvPort(newSrv.getSrvPort());
			}
		} else {
			mapService.put(newSrv.getSrvId(), newSrv);
		}
	}
	
	
	//Getter and Setter
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

	public Map<String, Service> getMapService() {
		return mapService;
	}

	public void setMapService(Map<String, Service> mapService) {
		this.mapService = mapService;
	}

}
