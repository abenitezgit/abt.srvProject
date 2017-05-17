package abt.srvProject.utiles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import abt.srvProject.model.Module;
import abt.srvProject.model.ProcControl;
import abt.srvProject.model.Proceso;
import abt.srvProject.model.Service;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;
import abt.srvProject.model.Dependence;
import abt.srvProject.model.Etl;
import abt.srvProject.model.GroupControl;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Info;
import abt.srvProject.model.Interval;

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
	Map<String, Interval> mapInterval = new HashMap<>();
	boolean isSyncMetadata;
	
	//Getter and Setter
	
	public Map<String, Module> getMapModule() {
		return mapModule;
	}

	public Map<String, Interval> getMapInterval() {
		return mapInterval;
	}

	public void setMapInterval(Map<String, Interval> mapInterval) {
		this.mapInterval = mapInterval;
	}

	public boolean isSyncMetadata() {
		return isSyncMetadata;
	}

	public void setSyncMetadata(boolean isSyncMetadata) {
		this.isSyncMetadata = isSyncMetadata;
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
	
	public void updateStatusTask(String key, Task task) throws Exception {
		try {
			if (getMapTask().containsKey(key)) {
				if (!getMapTask().get(key).getStatus().equals(task.getStatus())) {
					getMapTask().get(key).setErrCode(task.getErrCode());
					getMapTask().get(key).setErrMesg(task.getErrMesg());
					getMapTask().get(key).setFecFinished(task.getFecFinished());
					getMapTask().get(key).setFecIns(task.getFecIns());
					getMapTask().get(key).setFecUpdate(task.getFecUpdate());
					getMapTask().get(key).setStatus(task.getStatus());
					getMapTask().get(key).setuStatus(task.getuStatus());
					
					if (task.getTypeProc().equals("ETL")) {
						Interval interval = new Interval();
						String stringInterval = mylib.serializeObjectToJSon(task.getTxSubTask(), true);
						interval = (Interval) mylib.serializeJSonStringToObject(stringInterval, Interval.class);
						interval.setFecFinished(task.getFecFinished());
						interval.setFecUpdate(task.getFecUpdate());
						interval.setStatus(task.getStatus());
						
						getMapTask().get(key).setTxSubTask(interval);
						
						updateStatusMapInterval(interval, task);
					}
					
					mylib.console("Se actualizó Task: "+key);
				} else {
					mylib.console("No hay cambios que realizar para el Task: "+key);
				}
			} else {
				//Tratando de actualizar un Task que ya no existe
				//esto es un error
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isExistTask(String keyTask) throws Exception {
		try {
			if (getMapTask().containsKey(keyTask)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public synchronized void updateStatusGroupControl(String key, ProcControl pc) throws Exception {
		try {
			if (getMapGroupControl().containsKey(key)) {
				mylib.console("GrpKey: "+key);
				mylib.console("ProcStatus: "+pc.getStatus());
				mylib.console("GrpStatusActual: "+getMapGroupControl().get(key).getStatus());
				
				switch (pc.getStatus()) {
					case "PENDING":
						if (getMapGroupControl().get(key).getStatus().equals("UNASSIGNED")) {
							getMapGroupControl().get(key).setStatus(pc.getStatus());
						}
						break;
					case "READY":
						if (getMapGroupControl().get(key).getStatus().equals("PENDING")) {
							getMapGroupControl().get(key).setStatus(pc.getStatus());
						}
						break;
					case "RUNNING":
						if (getMapGroupControl().get(key).getStatus().equals("READY") || getMapGroupControl().get(key).getStatus().equals("PENDING")) {
							getMapGroupControl().get(key).setStatus(pc.getStatus());
						}
						break;
					case "FINISHED":
						if (getMapGroupControl().get(key).getStatus().equals("RUNNING")) {
							if (isFinishedProcessGroup(key)) {
								getMapGroupControl().get(key).setStatus(pc.getStatus());
							}
						}
						break;
				}
				getMapGroupControl().get(key).setErrCode(pc.getErrCode());
				getMapGroupControl().get(key).setErrMesg(pc.getErrMesg());
				getMapGroupControl().get(key).setFecFinished(pc.getFecFinished());
				getMapGroupControl().get(key).setFecIns(pc.getFecIns());
				getMapGroupControl().get(key).setFecUpdate(pc.getFecUpdate());
				getMapGroupControl().get(key).setProcID(pc.getProcID());
				getMapGroupControl().get(key).setuStatus(pc.getuStatus());
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isFinishedProcessGroup(String key) throws Exception {
		try {
			boolean response = true;
			String[] param = key.split(":");
			for (Map.Entry<String, ProcControl> entry : getMapProcControl().entrySet()) {
				String grpID = entry.getValue().getGrpID();
				String numSecExec = entry.getValue().getNumSecExec();
				String status = entry.getValue().getStatus();
				if (grpID.equals(param[0])  && numSecExec.equals(param[1]) && !status.equals("FINISHED")) {
					response = false;
				}
			}
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	public synchronized void updateStatusProcControl(String keyProc, Task task) throws Exception {
		try {
			
			if (getMapProcControl().containsKey(keyProc)) {
				if (!getMapProcControl().get(keyProc).getStatus().equals(task.getStatus())) {
					getMapProcControl().get(keyProc).setErrCode(task.getErrCode());
					getMapProcControl().get(keyProc).setErrMesg(task.getErrMesg());
					getMapProcControl().get(keyProc).setFecFinished(task.getFecFinished());
					getMapProcControl().get(keyProc).setFecIns(task.getFecIns());
					getMapProcControl().get(keyProc).setFecUpdate(task.getFecUpdate());
					getMapProcControl().get(keyProc).setStatus(task.getStatus());
					getMapProcControl().get(keyProc).setuStatus(task.getuStatus());
					
					String keyGroup = getMapProcControl().get(keyProc).getGrpID()+":"+task.getNumSecExec();
					updateStatusGroupControl(keyGroup, getMapProcControl().get(keyProc));
					
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
				getMapProcControl().get(key).setFecUpdate(mylib.getDate());
				
				if (status.equals("FINISHED")) {
					getMapProcControl().get(key).setuStatus("SUCCESS");
					getMapProcControl().get(key).setFecFinished(mylib.getDate());
				}
				
				updateStatusGroupControl(getMapProcControl().get(key).getGrpID()+":"+getMapProcControl().get(key).getNumSecExec(), getMapProcControl().get(key));
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
	
	public synchronized void addTask(Task task) throws Exception {
		try {
			getMapTask().put(task.getKey(), task);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized Interval getIntervalTask(ProcControl pc) throws Exception {
		try {
			Interval response = null;
			//Adiciona al Task el intervalo que ejecutará
			mylib.console("Total de Intervalos de Etl's Encontrados: "+getMapInterval().size());
			if (getMapInterval().size()>0) {
				Map<String, Interval> mapInt = getMapInterval()
							.entrySet()
							.stream()
							.filter(p -> p.getValue().getStatus().equals("UNASSIGNED") && p.getValue().getProcID().equals(pc.getProcID()))
							.collect(Collectors.toMap(map -> map.getKey() , map -> map.getValue()));
				
				//Valida si existe algun intervalo UNASSIGNED para el procID respectivo
				if (mapInt.size()>0) {
					Interval interval = new Interval();
					for (Map.Entry<String, Interval> entry : mapInt.entrySet()) {
						interval = entry.getValue();
						break;
					}
					
					response = interval;
				} else {
					//No hay intervalos disponibles para el proceso correspondiente
					response = null;
				}
			} else {
				//Aun no se ha generado el mapInterval
			}
				
			return response;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void updateStatusMapInterval(Interval interval, Task task) throws Exception {
		try {
			String key = interval.getProcID()+":"+interval.getIntervalID();
			if (getMapInterval().containsKey(key)) {
				if (!getMapInterval().get(key).getStatus().equals(task.getStatus())) {
					interval.setFecUpdate(task.getFecUpdate());
					interval.setFecFinished(task.getFecFinished());
					interval.setStatus(task.getStatus());
					getMapInterval().put(key, interval);
				} else {
					//No encontro key en el mapInterval
					//Esto es un error
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public synchronized void addTask(ProcControl pc, String srvID, Object subTask) throws Exception {
		try {
			Task task = new Task();
			
			task.setFecIns(mylib.getDate());
			task.setNumSecExec(pc.getNumSecExec());
			task.setParam(pc.getParam());
			task.setProcID(pc.getProcID());
			task.setSrvID(srvID);
			task.setStatus("PENDING");
			task.setTypeProc(pc.getTypeProc());
			task.setTxSubTask(subTask);


			String keyTask;
			if (pc.getTypeProc().equals("ETL")) {
				Interval interval = new Interval();
				interval = (Interval) subTask;
				updateStatusMapInterval(interval,task);

				keyTask = pc.getProcID()+":"+pc.getNumSecExec()+":"+interval.getIntervalID();
				
			} else {
				keyTask = pc.getProcID()+":"+pc.getNumSecExec();
			}
			
			task.setKey(keyTask);
			mapTask.put(keyTask, task);
			
			
		} catch (Exception e) {
			e.printStackTrace();
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
					lst.add(lstDependences.get(i).getProcPadre()+":"+lstDependences.get(i).getCritical());
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
				//Ingresa un proceso de control
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
				
				//Si es un proceso ETL crear o actualiza el mapInterval
				if (pControl.getTypeProc().equals("ETL")) {
					//Crea los intervalos ID correspondientes al proceso
					//segun configuracion del proceso
					genNewIntervals((Etl) pControl.getParam());
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
    public void genNewIntervals(Etl etl) throws Exception {
    	try {
    			    		
	        //Extre Fecha Actual
	        Date today;
	        Date fecGap;
	        Date fecIni;
	        Date fecItera;
	        Date fecIntervalIni;
	        Date fecIntervalFin;
	
	        @SuppressWarnings("unused")
			int MinItera;
	        int HoraItera;
	        int DiaItera;
	        int MesItera;
	        int AnoItera;
	
	        long numInterval;
	        String localIntervalID;
	        @SuppressWarnings("unused")
			String todayChar;


	        //Setea Fecha Actual
	        //
	        today = new Date();
	        
	        //Variables del Objeto ETL
	        //
	        int vTimeGap = etl.getTimeGap();
	        int vTimePeriod = etl.getTimePeriod();
	        int vTimeGen = etl.getTimeGen();
	        String vUnitMeasure = etl.getUnitMeasure();
	        String vETLID = etl.getEtlID();
            
	        //Setea Fecha GAP - Desface de tiempo en extraccion
	        //
	        Calendar c = Calendar.getInstance();
	        c.add(Calendar.MINUTE, -(vTimeGap+vTimePeriod));
	        fecGap = c.getTime();

	        //Setea Fecha Inicio Inscripcion/Revision de Intervalos
	        //
	
	        c.setTime(today);
	        c.add(Calendar.MINUTE, -vTimeGen);
	        fecIni = c.getTime();

	        mylib.console("Datos del ETLID: "+vETLID);
	        mylib.console("Fecha Actual: "+ today);
	        mylib.console("Fecha GAP   : "+ fecGap);
	        mylib.console("Fecha IniIns: "+ fecIni);
        
	        fecItera = fecIni;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	        //SimpleDateFormat sdfToday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String IntervalIni;
	        String IntervalFin;
	        Interval interval;
	        String keyMap;
        
	        while (fecItera.compareTo(fecGap) < 0) {
	            //Crea Objecto Interval
	            //
	            interval = new Interval();
	
	            //Extrae Intervalo para Fecha fecItera
	            //
	            c.setTime(fecItera);
	            AnoItera = c.get(Calendar.YEAR);
	            MesItera = c.get(Calendar.MONTH);
	            DiaItera = c.get(Calendar.DAY_OF_MONTH);
	            HoraItera = c.get(Calendar.HOUR_OF_DAY);
	            MinItera = c.get(Calendar.MINUTE);
	
	            //Valida si el intervalo de extraccion (cETL_INTERVALUNIDAD) es por:
	            //  Minutos     : 0
	            //  Horas       : 1
	            //  Dias        : 2
	            //  Semanas     : 3
	            //  Mensuales   : 4
	            //  Anuales     : 5
	
	            switch (vUnitMeasure) {
	                case "MINUTE":
	                    fecIntervalIni = null;
	                    fecIntervalFin = null;
	                    numInterval = 60/vTimePeriod;
	                    for (int i=1;i<=numInterval;i++) {
	                        c.set(AnoItera, MesItera, DiaItera, HoraItera, (i)*vTimePeriod,0);
	                        fecIntervalFin = c.getTime();
	                        if (fecIntervalFin.compareTo(fecItera) >0 ) {
	                            c.set(AnoItera, MesItera, DiaItera, HoraItera, (i-1)*vTimePeriod,0);
	                            fecIntervalIni = c.getTime();
	                            break;
	                        }
	                    }
	                    c.setTime(fecItera);
	                    c.add(Calendar.MINUTE, vTimePeriod);
	                    fecItera = c.getTime();
	
	
	                    IntervalIni = sdf.format(fecIntervalIni);
	                    IntervalFin = sdf.format(fecIntervalFin);
	                    localIntervalID = IntervalIni+'-'+IntervalFin;
	
	                    interval.setIntervalID(localIntervalID);
	                    interval.setStatus("UNASSIGNED");
	                    interval.setFecIns(mylib.getDate());
	                    interval.setFecUpdate(mylib.getDate());
	                    interval.setFecIniInterval(IntervalIni);
	                    interval.setFecFinInterval(IntervalFin);
	                    interval.setNumSecExec(etl.getNumSecExec());
	                    interval.setProcID(etl.getEtlID());
	
	                    keyMap = etl.getEtlID()+":"+localIntervalID;
	                    if (!getMapInterval().containsKey(keyMap)) {
	                    	getMapInterval().put(keyMap, interval);
	                    	mylib.console("Se inscribió intervalID: "+keyMap+ " para el ETL: "+ etl.getEtlID());
	                    }
	
	                    break;
	
	                    case "HOUR":
	                        fecIntervalIni = null;
	                        fecIntervalFin = null;
	                        numInterval = 24/vTimePeriod;
	                        for (int i=1;i<=numInterval;i++) {
	                            c.set(AnoItera, MesItera, DiaItera, (i)*vTimePeriod, 0, 0);
	                            fecIntervalFin = c.getTime();
	                            if (fecIntervalFin.compareTo(fecItera) >0 ) {
	                                c.set(AnoItera, MesItera, DiaItera, (i-1)*vTimePeriod, 0, 0);
	                                fecIntervalIni = c.getTime();
	                                break;
	                            }
	                        }
	                        c.setTime(fecItera);
	                        c.add(Calendar.HOUR_OF_DAY, vTimePeriod);
	                        fecItera = c.getTime();
	
	                        IntervalIni = sdf.format(fecIntervalIni);
	                        IntervalFin = sdf.format(fecIntervalFin);
	                        localIntervalID = IntervalIni+'-'+IntervalFin;                    
	
		                    interval.setIntervalID(localIntervalID);
		                    interval.setStatus("UNASSIGNED");
		                    interval.setFecIns(mylib.getDate());
		                    interval.setFecUpdate(mylib.getDate());
		                    interval.setFecIniInterval(IntervalIni);
		                    interval.setFecFinInterval(IntervalFin);
		                    interval.setNumSecExec(etl.getNumSecExec());
	
		                    keyMap = etl.getEtlID()+":"+localIntervalID;
		                    if (!getMapInterval().containsKey(keyMap)) {
		                    	getMapInterval().put(keyMap, interval);
		                    	mylib.console("Se inscribió intrevalID: "+keyMap+ " para el ETL: "+ etl.getEtlID());
		                    }
	
	                        break;
	
	                    case "2":
	                    case "3":
	                    case "4":
	                    case "5":
	                    default:
	                } //end switch
	            } //end while
    	} catch (Exception e) {
    		mylib.console(1,"Error generando mapa de intervalos .."+e.getMessage());
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
	
	public boolean isExistProcControl(String key) throws Exception {
		try {
			return getMapProcControl().containsKey(key);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isExistGroupControl(String key) throws Exception {
		try {
			return getMapGroupControl().containsKey(key);
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
		/**
		 * Se recibe un Map de parametros y valores
		 * Si el srvID existe ya en el mapService, se extraen sus actuales valores para
		 * prceder a modificar solo lo informado
		 */
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
