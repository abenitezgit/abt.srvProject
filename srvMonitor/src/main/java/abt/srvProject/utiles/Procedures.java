package abt.srvProject.utiles;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.stream.Collectors;

import abt.srvProject.dataAccess.MetaData;
import abt.srvProject.dataAccess.MetaQuery;
import abt.srvProject.model.Agenda;
import abt.srvProject.model.Dependence;
import abt.srvProject.model.Etl;
import abt.srvProject.model.EtlMatch;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Interval;
import abt.srvProject.model.Mov;
import abt.srvProject.model.MovMatch;
import abt.srvProject.model.ProcControl;
import abt.srvProject.model.Proceso;
import abt.srvProject.model.Task;
import abt.srvProject.srvRutinas.Rutinas;

public class Procedures {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public Procedures(GlobalArea m) {
		gDatos = m;
	}
	
	public void updateReceiveTask(Map<String, Task> mapTask) throws Exception {
		try {
			if (mapTask.size()>0) {
				//Para cada Task recibido se procede a actualizar los objetos locales
				//mapProcControl, mapGroupControl, mapTask, mapInterval
				//Dentro de un Task se actualiza el Interval correspondiente
				for (Map.Entry<String, Task> entry : mapTask.entrySet()) {
					mylib.console("Actualizando Task: "+entry.getKey());
					
					Task task = new Task();
					String taskString = mylib.serializeObjectToJSon(entry.getValue(), false);
					task = (Task) mylib.serializeJSonStringToObject(taskString, Task.class);
					
					if (gDatos.isExistTask(entry.getKey())) {
						gDatos.updateStatusTask(entry.getKey(), task);
						String keyProc = task.getProcID()+":"+task.getNumSecExec();
						gDatos.updateStatusProcControl(keyProc, task);
					} else {
						//Tarea no fue encontrada en master
						//se sincronizará
						gDatos.addTask(task);
					}
				}
			} else {
				//No hay Task informados desde servicios
			}
		} catch (Exception e) {
			mylib.console(1,"Error updateReceiveTask ("+e.getMessage()+")");
			throw new Exception(e.getMessage());
		}
	}
	
	public Map<String, Task> getMapTaskServicePending(String srvID) throws Exception {
		try {
			//Variable local de paso
			Map<String, Task> mapT = new HashMap<>();
			
			mapT = gDatos.getMapTask()
					.entrySet()
					.stream()
					.filter(p -> p.getValue().getSrvID().equals(srvID) && p.getValue().getStatus().equals("PENDING"))
					.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
			
			return mapT;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public boolean isDependencesFinished(String keyProc, ProcControl pc) throws Exception {
		try {
			boolean response = true;
			
			List<String> depProc = pc.getDependences();
			if (depProc.size()>0) {
				for (int i=0; i<depProc.size(); i++) {
					String[] split = depProc.get(i).split(":");
					String procID = split[0];
					String critical = split[1];
					
					boolean isFin = true;
					String uStatus;
					String status;
					
					try {
						status = gDatos.getMapProcControl().get(procID+":"+pc.getNumSecExec()).getStatus();
						
						try {
							uStatus = gDatos.getMapProcControl().get(procID+":"+pc.getNumSecExec()).getuStatus();
							
							if (status.equals("FINISHED")) {
								if (uStatus.equals("ERROR")) {
									if (critical.equals("1")) {
										isFin = false;
									} else {
										isFin = true;
									}
								} else {
									//status="SUCCESS"
									isFin=true;
								}
							} else {
								isFin = false;
							}
							
						} catch (Exception e) {
							isFin = false;
						}
					}
					catch (Exception e) {
						isFin = false;
					}
					
					response = response && isFin;
				}
			}

			return response;
			
		} catch (Exception e) {
			mylib.console(1,"Error isDependencesFinished ("+e.getMessage()+")");
			throw new Exception(e.getMessage());
		}
	}
	
	public void appendNewTask() throws Exception {
		try {
			//Filtra lista de ProcControl con status UNASSIGNED
			Map<String, ProcControl> map_pc = gDatos.getMapProcControl()
												.entrySet()
												.stream()
												.filter(p -> p.getValue().getStatus().equals("UNASSIGNED") || p.getValue().getTypeProc().equals("ETL"))
												.collect(Collectors.toMap(map -> map.getKey() , map -> map.getValue()));
			mylib.console("Total procesos para generar Task: "+map_pc.size());
			if (map_pc.size()>0) {
				for (Map.Entry<String, ProcControl> mapUA : map_pc.entrySet()) {
					//Valida si el proceso tiene dependencias finalizadas para generar un TASK
					if (isDependencesFinished(mapUA.getKey(), mapUA.getValue())) {
						//Variable de control para crear Task y recuperar intervalID de procesos ETL
						Interval interval = new Interval();
						boolean isGenTask = true;
						
						//Valida para procesos ETL si existen intervalos pendientes para poder
						//crear el Task correspondiente
						//Si no hay intervalos el procID debe marcarse como finalizado
						switch (mapUA.getValue().getTypeProc()) {
						case "ETL":
							if (!mapUA.getValue().getStatus().equals("FINISHED")) {
								interval = gDatos.getIntervalTask(mapUA.getValue());
								if (interval != null) {
									isGenTask = true; 
								} else {
									//pendiente crear condicion si es nula la asignacion de intervalos
									//para finalizar el proceso
									isGenTask = false;
								}
							}
							break;
						default:
							isGenTask = true;
							interval = null;
						}
						
						if (isGenTask) {
							//Recupera un serverID para crear TASK
							String srvID = "srv00001"; //getServerAssigned(mapUA.getValue().getTypeProc());

							//Si se recupero un srvID
							if (!mylib.isNull(srvID)) {
								//Se crea Task asignado a un servidor
								gDatos.addTask(mapUA.getValue(), srvID, interval);
								gDatos.updateStatusProcControl(mapUA.getKey(),"PENDING");
							} else {
								//No  hay servidores disponibles para asignar Tarea
							}
						}
					} else {
						//Dependencias no han finalizado
						mylib.console("Dependencias del proceso: "+mapUA.getKey()+" no han terminado");
					}
				}
			} else {
				mylib.console("No hay procesos para asignar Task");
			}
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void appendProcessControl() throws Exception {
		try {
			//Busca todos los grupos nuevos activados en Cola de Grupos
			if (gDatos.getLkdGrupo().size()>0) {
				Grupo grupo;
				int numItems = gDatos.getLkdGrupo().size();
				while (numItems > 0) {
					grupo = new Grupo();
					grupo = gDatos.getLkdGrupo().remove();
					
					//Valida si grupo ya está inscrito en groupControl
					String key = grupo.getGrpID()+":"+grupo.getNumSecExec();
					if (!gDatos.isExistGrupoIns(key)) {
						gDatos.inscribeGroup(key);
						gDatos.inscribeProcess(grupo);
					}
					numItems = gDatos.getLkdGrupo().size();
				}
				
			} else {
				//No hay nuevos grupos en cola de grupos.
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void appendColaGruposActivos(List<Grupo> lstGrupo) throws Exception {
		try {
			for (int i=0; i<lstGrupo.size(); i++) {
				gDatos.addLkdGrupo(lstGrupo.get(i));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Grupo> getGrupos(List<Agenda> lstAgendas) throws Exception {
		try {
			
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			List<Grupo> lstGrupo = new ArrayList<>();
			Agenda agenda;
			
			MetaData dbConn = new MetaData(gDatos);
			dbConn.open();
			
			if (dbConn.isConnected()) {
				for (int i=0; i<lstAgendas.size(); i++) {
	        		//Datos de la Agenda buscada
	        		agenda = new Agenda();
		        	agenda = lstAgendas.get(i);
		        	
		        	Grupo grupo;
		        	
		        	String vSql = dbQuery.getSqlFindGroup(agenda);
		        	
		        	if (dbConn.executeQuery(vSql)) {
		        		ResultSet rs = dbConn.getQuery();
		        		if (rs!=null) {
		        			while (rs.next()) {
		        				grupo = new Grupo();
		        				mylib.parseaGrupo(grupo,rs);
		        				
		        				//Recupera procesos del Grupo
		        				grupo.setLstProcess(getListaProcesos(grupo.getGrpID()));
		        				
		        				//Recupera dependencias de procesos del grupo
		        				grupo.setLstDepend(getListaDependences(grupo.getGrpID()));
		        				
		        				if (grupo.getLstProcess().size()>0) {
		        					lstGrupo.add(grupo);
		        				} else {
		        					mylib.console(1,"No hay procesos asignados al grupo: "+grupo.getGrpID());
		        				}
		        			}
		        			rs.close();
		        		} else {
		        			//Error en ejecucion de query de busqueda de grupos
		        		}
		        	} else {
		        		//No pudo ejecutar la Query de busquedas de grupo
		        	}
				} //end for
				dbConn.close();
			} else {
				//No es posible conectarse a la base de datos
			} 
			
			return lstGrupo;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Dependence> getListaDependences(String grpID) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			List<Dependence> lstDependences = new ArrayList<>();
			Dependence dependence;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindDependences(grpID);
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs!=null) {
						while (rs.next()) {
							dependence = new Dependence();
							mylib.parseaDependence(dependence, rs);
							lstDependences.add(dependence);
						}
						rs.close();
					} else {
						//Error ejecutando la Query
					}
				} else {
					//Error ejecutando la Query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la BD
			}
			
			return lstDependences;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public List<Proceso> getListaProcesos(String grpID) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			List<Proceso> lstProcess = new ArrayList<>();
			Proceso process;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindProcess(grpID);
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs!=null) {
						while (rs.next()) {
							process = new Proceso();
							mylib.parseaProceso(process, rs);
							
							process.setParams(getDetalleProcess(process));
							
							lstProcess.add(process);
						}
						rs.close();
					} else {
						//Error ejecutando la query
					}
					
				} else {
					//Error ejecutando la Query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la BD
			}
			
			return lstProcess;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Object getDetalleProcess(Proceso process) throws Exception {
        try {
        	Object param;
            switch (process.getType()) {
                case "ETL":
                    param = getEtlDetail(process);
                    break;
                case "MOV":
                	param = getMovDetail(process);
                	break;
                default:
                	param = null;
            }
            return param;
        } catch (Exception e) {
        	throw new Exception(e.getMessage());
        }
    }
	
	public List<EtlMatch> getListaEtlMatch(String etlID) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			List<EtlMatch> lstEtlMatch = new ArrayList<>();
			EtlMatch etlMatch;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindEtlMatch(etlID);
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						etlMatch = new EtlMatch();
						mylib.parseaEtlMatch(etlMatch, rs);
						lstEtlMatch.add(etlMatch);
					}
					rs.close();
				} else {
					//no pudo ejecutar la query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la BD
			}
			return lstEtlMatch;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public Etl getEtlDetail(Proceso process) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			Etl etl = new Etl();
			
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindEtl(process.getProcID());
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						mylib.parseaEtl(etl, rs);
						
						//Recupera Match del Etl
						etl.setLstEtlMatch(getListaEtlMatch(etl.getEtlID()));
						
					} else {
						//No hay registros para la consulta realizada
					}
				} else {
					//No pudo ejecutar query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la base de datos
			}
			
			return etl;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public Mov getMovDetail(Proceso process) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			Mov mov = new Mov();
			
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindMov(process.getProcID());
				mylib.console("Query de FindMov: "+vSql);
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					if (rs.next()) {
						mylib.parseaMov(mov, rs);
						
						//Recupera Match del Mov
						mov.setLstMovMatch(getListaMovMatch(mov.getMovID()));
						
					} else {
						//No hay registros para la consulta realizada
					}
				} else {
					//No pudo ejecutar query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la base de datos
			}
			
			return mov;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public List<MovMatch> getListaMovMatch(String movID) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			List<MovMatch> lstMovMatch = new ArrayList<>();
			MovMatch movMatch;
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindMovMatch(movID);
				if (dbConn.executeQuery(vSql)) {
					ResultSet rs = dbConn.getQuery();
					while (rs.next()) {
						movMatch = new MovMatch();
						mylib.parseaMovMatch(movMatch, rs);
						lstMovMatch.add(movMatch);
					}
					rs.close();
				} else {
					//no pudo ejecutar la query
				}
				dbConn.close();
			} else {
				//No pudo conectarse a la BD
			}
			return lstMovMatch;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
    public List<Agenda> getActiveAgendas() throws Exception {
    	try {
	    	MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
	    	List<Agenda> lstAgenda = new ArrayList<>();
	    	
	    	Calendar iteratorCalendar;
	    	SimpleTimeZone tz;
	    	int year;
	    	int month;
	    	int dayOfMonth;
	    	int dayOfWeek;
	    	int weekOfYear;
	    	int weekOfMonth;
	    	int findHour;
	        int findMinutes;
	        
	        String posmonth;
	        String posdayOfMonth;
	        String posdayOfWeek;
	        String posweekOfYear;
	        String posweekOfMonth;
	        
	        String iteratorHour;
	        String iteratorMinute;
	        String posIteratorHour;
	        String posIteratorMinute;
	        
	        String numSecExec;
	
	        /**
	         * Busca Todas las agendas que deberían ser activadas en el periodo de findMinutes (5) minutos de holgura
	         * Actualiza la lista global de agendas activas gDatos.lstActiveAgendas
	         * 
	         * Llena la lista local lstAgenda()
	         * 
	         */
	    	
	    	//Set Data Calendar
	        //Setea Calendario en Base al TimeZone
	        //
	        String[] ids = TimeZone.getAvailableIDs(-3 * 60 * 60 * 1000);
	        String clt = ids[0];
	        tz = new SimpleTimeZone(-3 * 60 * 60 * 1000, clt);
	        tz.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	        tz.setEndRule(Calendar.AUGUST, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	        Calendar calendar = new GregorianCalendar(tz);
	
	        year       = calendar.get(Calendar.YEAR);
	        month      = calendar.get(Calendar.MONTH); // Jan = 0, dec = 11
	        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); 
	        dayOfWeek  = calendar.get(Calendar.DAY_OF_WEEK);
	        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
	        weekOfMonth= calendar.get(Calendar.WEEK_OF_MONTH);
	
	        findHour    = gDatos.getInfo().getAgeShowHour();  //Cantidad de Horas definidas para la muestra de las agendas
	        findMinutes = gDatos.getInfo().getAgeGapMinute(); //GAP en minutos para encontrar agendas que deberían haberse activado
	
	        //Genera las variables de Posicion a comparar con las guardadas en la base de datos
	        
	        //Ajusta Semana del Mes para que no retorne una semana 5
	        if (weekOfMonth==5 ||weekOfMonth==6) {
	            weekOfMonth = 4;
	        }
	        
	        posmonth = String.valueOf(month+1);
	        posdayOfMonth = String.valueOf(dayOfMonth);
	        posdayOfWeek = String.valueOf(dayOfWeek);
	        posweekOfYear = String.valueOf(weekOfYear);
	        posweekOfMonth = String.valueOf(weekOfMonth);
	
	        //Vuelve a inicializar las variables para realizar la busqueda de agendas que activaran grupos
	        //
	        iteratorCalendar = new GregorianCalendar(tz);
	        iteratorHour = String.valueOf(iteratorCalendar.get(Calendar.HOUR_OF_DAY));
	        posIteratorHour = String.valueOf(Integer.valueOf(iteratorHour)+1);
	        
	        MetaData dbConn = new MetaData(gDatos);
	        
	        dbConn.open();
	        
	        if (dbConn.isConnected()) {
		        for (int i=-findMinutes; i<=0; i++) {
		            iteratorCalendar = new GregorianCalendar(tz);
		            iteratorCalendar.add(Calendar.MINUTE, i);
		            iteratorMinute = String.valueOf(iteratorCalendar.get(Calendar.MINUTE));
		            posIteratorMinute = String.valueOf(Integer.valueOf(iteratorMinute)+1);
		            
		            //Para cada agenda que se encuentra en el periodo buscado se le asignara un numero unico de secuencia de ejecucion
		            //Esto para no confundir las mismas agendas en periodos de ejecución buscados en el gap
		            //Este numero de ejecucuion identificara a cada procesos y grupo respectivo
		            //
		            numSecExec = String.format("%04d", year)+String.format("%02d", month+1)+String.format("%02d", dayOfMonth)+String.format("%02d", Integer.valueOf(iteratorHour))+String.format("%02d", Integer.valueOf(iteratorMinute));
		            
		            /**
		             * Busca Agendas correspondientes al minuto exacto del iterador del ciclo for
		             */
		            String vSQL = dbQuery.getSqlFindAgeActive(iteratorMinute, posmonth, posdayOfMonth, posdayOfWeek, posweekOfYear, posweekOfMonth, posIteratorHour, posIteratorMinute);
	            	Agenda agenda;
	            	if (dbConn.executeQuery(vSQL)) {
	            		ResultSet rs = dbConn.getQuery();
	                    if (rs!=null) {
	                        while (rs.next()) {
	                            agenda = new Agenda();
	                            agenda.setHoraAgenda(rs.getString("horaAgenda"));
	                            agenda.setAgeID(rs.getString("ageID"));
	                            agenda.setMonth(rs.getString("month"));
	                            agenda.setDayOfMonth(rs.getString("dayOfMonth"));
	                            agenda.setWeekOfYear(rs.getString("weekOfYear"));
	                            agenda.setWeekOfMonth(rs.getString("weekOfMonth"));
	                            agenda.setHourOfDay(rs.getString("hourOfDay"));
	                            agenda.setNumSecExec(numSecExec);
	                            lstAgenda.add(agenda);
	                        }
	                        rs.close();
	                    }
	            	}
		        } //end for
		        dbConn.close();
	        }
	        return lstAgenda;
    	} catch (Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
    

}
