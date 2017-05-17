package abt.srvProject.utiles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import abt.srvProject.model.Module;
import abt.srvProject.model.Service;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.Info;
import abt.srvProject.model.Interval;

public class GlobalArea {
	Rutinas mylib = new Rutinas();

	Info info;
	Service service = new Service();
	Map<String, Module> mapModule = new HashMap<>();
	Map<String, Task> mapTask = new HashMap<>();
	
	//Cola de Task
	LinkedList<Task> lkdTask = new LinkedList<>(); 
	
	//Getter and Setter
	
	public Service getService() {
		return service;
	}

	public LinkedList<Task> getLkdTask() {
		return lkdTask;
	}

	public void setLkdTask(LinkedList<Task> lkdTask) {
		this.lkdTask = lkdTask;
	}

	public Map<String, Task> getMapTask() {
		return mapTask;
	}

	public void setMapTask(Map<String, Task> mapTask) {
		this.mapTask = mapTask;
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
	
	public synchronized Task getItemTaskPool() throws Exception {
		try {
			Task tsk = new Task();
			tsk = getLkdTask().remove();
			return tsk;
		} catch (Exception e) {
			mylib.console(1,"Error getItemTaskPool ("+e.getMessage()+")");
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void addNewTaskMap(Task task) throws Exception {
		try {
			getMapTask().put(task.getKey(), task);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateLkdTask(Map<String, Task> mapL) throws Exception {
		/**
		 * Actualiza la cola de Tareas locales desde monitor
		 * agregar nuevos task
		 * cancelar task si aun no se han ejecutado
		 */
		try {
			//Para cada Task recibida
			Task tsk;
			for (Map.Entry<String, Task> entry : mapL.entrySet()) {
				String mapTString = mylib.serializeObjectToJSon(entry.getValue(), false);
				tsk = new Task();
				tsk = (Task) mylib.serializeJSonStringToObject(mapTString, Task.class);
				//tsk = mapT.getValue();
				getLkdTask().add(tsk);
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateStatusTask(String keyTask, String status, String uStatus) throws Exception {
		try {
			if (!getMapTask().get(keyTask).getStatus().equals(status)) {
				getMapTask().get(keyTask).setStatus(status);
				getMapTask().get(keyTask).setFecUpdate(mylib.getDate());
				getMapTask().get(keyTask).setuStatus(uStatus);
	
				switch (status) {
				case "FINISHED":
					getMapTask().get(keyTask).setFecFinished(mylib.getDate());
					break;
				default:
					break;
				}
				
				Interval interval = new Interval();
				interval = (Interval) getMapTask().get(keyTask).getTxSubTask();
				
				if (!interval.getStatus().equals(status)) {
					interval.setStatus(status);
					interval.setFecUpdate(mylib.getDate());
					if (status.equals("FINISHED")) {
						interval.setFecFinished(mylib.getDate());
					}
				}
				
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateStatusTask(String keyTask, String status) throws Exception {
		try {
			if (!getMapTask().get(keyTask).getStatus().equals(status)) {
				getMapTask().get(keyTask).setStatus(status);
				getMapTask().get(keyTask).setFecUpdate(mylib.getDate());
	
				if (getMapTask().get(keyTask).getTypeProc().equals("ETL")) {
					Interval interval = new Interval();
					interval = (Interval) getMapTask().get(keyTask).getTxSubTask();
					
					if (!interval.getStatus().equals(status)) {
						interval.setStatus(status);
						interval.setFecUpdate(mylib.getDate());
					}
					getMapTask().get(keyTask).setTxSubTask(interval);
				}
				
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateService(Map<String,Object> newSrv) throws Exception {
		try {
			/**
			 * Actualiza servicio local con valores provenientes de la respuesta del srvSync
			 * El map newSrv solo contiene los datos para actualizar
			 */
			
			for (Map.Entry<String, Object> entry : newSrv.entrySet()) {
				switch (entry.getKey()) {
				case "srvId":
					getService().setSrvId((String) entry.getValue());
					break;
				case "srvDesc":
					getService().setSrvDesc((String) entry.getValue());
					break;
				case "srvIp":
					getService().setSrvIp((String) entry.getValue());
					break;
				case "srvPort":
					getService().setSrvPort((int) entry.getValue());
					break;
				case "enable":
					getService().setEnable((boolean) entry.getValue());
					break;
				case "activePrimaryMonitor":
					getService().setActivePrimaryMonitor((boolean) entry.getValue());
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
