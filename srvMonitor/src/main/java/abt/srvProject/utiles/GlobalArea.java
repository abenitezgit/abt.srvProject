package abt.srvProject.utiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.ProcControl;
import abt.srvProject.model.Proceso;
import abt.srvProject.model.Service;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.Dependence;
import abt.srvProject.model.GroupControl;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Info;

public class GlobalArea {
	Rutinas mylib = new Rutinas();

	Info info;
	Map<String, Service> mapService = new HashMap<>();
	Map<String, Module> mapModule = new HashMap<>();
	
	//Objetos para almacenar los Grupos activos
	LinkedList<Grupo> lkdGrupo = new LinkedList<Grupo>(); 
	
	//Objetos de Control de Procesos
	Map<String, ProcControl> mapProcControl = new HashMap<>();
	Map<String, GroupControl> mapGroupControl = new HashMap<>();
	Map<String, Task> mapTask = new HashMap<>();
	
	//Getter and Setter
	
	
	public Map<String, Module> getMapModule() {
		return mapModule;
	}

	public Map<String, ProcControl> getMapProcControl() {
		return mapProcControl;
	}

	public void setMapProcControl(Map<String, ProcControl> mapProcControl) {
		this.mapProcControl = mapProcControl;
	}

	public Map<String, Task> getMapTask() {
		return mapTask;
	}

	public void setMapTask(Map<String, Task> mapTask) {
		this.mapTask = mapTask;
	}

	public LinkedList<Grupo> getLkdGrupo() {
		return lkdGrupo;
	}


	public void setLkdGrupo(LinkedList<Grupo> lkdGrupo) {
		this.lkdGrupo = lkdGrupo;
	}


	public Map<String, GroupControl> getMapGroupControl() {
		return mapGroupControl;
	}


	public void setMapGroupControl(Map<String, GroupControl> mapGroupControl) {
		this.mapGroupControl = mapGroupControl;
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

	
	
	//Procedimientos internos
	
	public synchronized void updateStatusGroupControl(String key, String status) throws Exception {
		try {
			if (getMapGroupControl().containsKey(key)) {
				switch (status) {
					case "PENDING":
						if (getMapGroupControl().get(key).getStatus().equals("UNASSIGNED")) {
							getMapGroupControl().get(key).setStatus(status);
						}
						break;
				}
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateStatusProcControl(String key, String status) throws Exception {
		try {
			if (getMapProcControl().containsKey(key)) {
				getMapProcControl().get(key).setStatus(status);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void addProcControl(ProcControl pc) throws Exception {
		try {
			getMapProcControl().put(pc.getProcID()+":"+pc.getNumSecExec(), pc);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void addTask(ProcControl pc, String srvID) throws Exception {
		try {
			Task task = new Task();
			
			task.setFecIns(mylib.getDate());
			task.setNumSecExec(pc.getNumSecExec());
			task.setParam(pc.getParam());
			task.setProcID(pc.getProcID());
			task.setSrvID(srvID);
			task.setStatus("PENDING");
			task.setTypeProc(pc.getTypeProc());

			String keyTask = pc.getProcID()+":"+pc.getNumSecExec();
			mapTask.put(keyTask, task);
			
		} catch (Exception e) {
			mylib.console(1,"Error addTask ("+e.getMessage()+")");
			throw new Exception(e.getMessage());
		}
	}
	
	
	public void addService(String key, Service srv) throws Exception {
		try {
			getMapService().put(key, srv);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateMapService(String key, Service srv) throws Exception {
		try {
			if (getMapService().containsKey(key)) {
				//update typeProc y cliProc
				getMapService().get(key).setEnable(srv.isEnable());
				getMapService().get(key).setLstTypeProc(srv.getLstTypeProc());
				getMapService().get(key).setLstCliProc(srv.getLstCliProc());
				
			} else {
				//add
				mylib.console("Agregando servicio key: "+key+" detail: "+mylib.serializeObjectToJSon(srv, false));
				addService(key, srv);
			}
			
		} catch (Exception e) {
			mylib.console(1,"Error updateMapService ("+e.getMessage()+")");
			throw new Exception(e.getMessage());
		}
		
	}
	
	public synchronized void addMapGroupControl(String key, GroupControl gControl) throws Exception {
		try {
			getMapGroupControl().put(key, gControl);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<String> getProcDependences(List<Dependence> lstDependences, String procID) throws Exception {
		try {
			List<String> lst = new ArrayList<>();
			for (int i=0; i<lstDependences.size(); i++) {
				if (lstDependences.get(i).getProcHijo().equals(procID)) {
					lst.add(lstDependences.get(i).getProcPadre()+"+"+lstDependences.get(i).getCritical());
				}
			}
			return lst;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void inscribeProcess(Grupo grupo) throws Exception {
		try {
			List<Proceso> lstProcess = new ArrayList<>();
			List<Dependence> lstDependences = new ArrayList<>();
			lstProcess = grupo.getLstProcess();
			lstDependences = grupo.getLstDepend();
			ProcControl pControl;

			for (int i=0; i<lstProcess.size(); i++) {
				pControl = new ProcControl();
				pControl.setGrpID(grupo.getGrpID());
				pControl.setNumSecExec(grupo.getNumSecExec());
				pControl.setProcID(lstProcess.get(i).getProcID());
				pControl.setTypeProc(lstProcess.get(i).getType());
				pControl.setOrder(lstProcess.get(i).getnOrder());
				pControl.setStatus("UNASSIGNED");
				pControl.setFecIns(mylib.getDate());
				pControl.setParam(lstProcess.get(i).getParams());
				pControl.setDependences(getProcDependences(lstDependences, lstProcess.get(i).getProcID()));
				addProcControl(pControl);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void inscribeGroup(String key) throws Exception {
		try {
			GroupControl gControl = new GroupControl();
			gControl.setFecIns(mylib.getDate());
			gControl.setStatus("UNASSIGNED");
			addMapGroupControl(key, gControl);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isExistGrupoIns(String key) throws Exception {
		try {
			if (mapGroupControl.containsKey(key)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void addLkdGrupo(Grupo grupo) throws Exception {
		try {
			lkdGrupo.add(grupo);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	
	public synchronized void updateService(Map<String,Object> newSrv) throws Exception {
		try {
			String srvId = (String) newSrv.get("srvId");
			
			Service srv = new Service();
			
			if (getMapService().containsKey(srvId)) {
				srv = getMapService().get(srvId);
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
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	

}
