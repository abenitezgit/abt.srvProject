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
	
	//Procedimientos internos
	public synchronized void updateService(Service newSrv) throws Exception{

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
	}
	
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
}
