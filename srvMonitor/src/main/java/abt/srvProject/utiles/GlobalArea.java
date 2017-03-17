package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.Service;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Info;

public class GlobalArea {
	Rutinas mylib = new Rutinas();

	Info info;
	Map<String, Service> mapService = new HashMap<>();
	Map<String, Module> mapModule = new HashMap<>();
	
	//Objetos para almacenar los Grupos activos
	LinkedList<Grupo> lkdGrupo = new LinkedList<Grupo>(); 
	
	
	//Procedimientos internos
	
	
	public void addLkdGrupo(Grupo grupo) throws Exception {
		try {
			lkdGrupo.add(grupo);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	public synchronized void updateService(Map<String,Object> newSrv) throws Exception {

		String srvId = (String) newSrv.get("srvId");
		
		Service srv = new Service();
		if (mapService.containsKey(srvId)) {
			srv = mapService.get(srvId);
		}
		
		for (Map.Entry<String, Object> entry : newSrv.entrySet()) {
			switch (entry.getKey()) {
			case "srvId":
				srv.setSrvId((String) entry.getValue());
				break;
			case "srvIp":
				srv.setSrvIp((String) entry.getValue());
				break;
			case "srvPort":
				srv.setSrvPort((int) entry.getValue());
				break;
			case "enable":
				srv.setEnable((boolean) entry.getValue());
				break;
			case "activePrimaryMonitor":
				srv.setActivePrimaryMonitor((boolean) entry.getValue());
				break;
			}
		}
		mapService.put(srvId, srv);
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
