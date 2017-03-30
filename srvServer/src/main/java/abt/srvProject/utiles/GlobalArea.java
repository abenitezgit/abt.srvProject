package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.Service;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.AssignedTypeProc;
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
	@SuppressWarnings("unchecked")
	public synchronized void updateService(Map<String,Object> newSrv) throws Exception {
		try {
			/**
			 * Actualiza servicio local con valores provenientes de la respuesta del srvSync
			 * El map newSrv solo contiene los datos para actualizar
			 */
			
			for (Map.Entry<String, Object> entry : newSrv.entrySet()) {
				switch (entry.getKey()) {
				case "enable":
					getService().setEnable((boolean) entry.getValue());
					break;
				case "activePrimaryMonitor":
					getService().setActivePrimaryMonitor((boolean) entry.getValue());
					break;
				case "lstTypeProc":
					getService().setLstTypeProc((List<AssignedTypeProc>) entry.getValue());
					break;
				case "lstCli":
					getService().setLstCli((List<String>) entry.getValue());
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
