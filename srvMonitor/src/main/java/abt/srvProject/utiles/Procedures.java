package abt.srvProject.utiles;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.json.JSONObject;

import abt.srvProject.dataAccess.MetaData;
import abt.srvProject.dataAccess.MetaQuery;
import abt.srvProject.model.Agenda;
import abt.srvProject.model.Dependence;
import abt.srvProject.model.Grupo;
import abt.srvProject.model.Mov;
import abt.srvProject.model.MovMatch;
import abt.srvProject.model.Proceso;
import abt.srvProject.srvRutinas.Rutinas;

public class Procedures {
	GlobalArea gDatos;
	Rutinas mylib = new Rutinas();
	
	public Procedures(GlobalArea m) {
		gDatos = m;
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
		        				
		        				grupo.setLstDepend(getListaDependences(grupo.getGrpID()));
		        				
		        				//Recupera dependencias de procesos del grupo
		        				
		        				lstGrupo.add(grupo);
		        			}
		        			rs.close();
		        		} else {
		        			//Error en ejecucion de query de busqueda de grupos
		        		}
		        	} else {
		        		//No pudo ejecutar la Query de busquedas de grupo
		        	}
				} //end for
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
							
							process.setParams(getDatalleProcess(process));
							
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
	
	public Object getDatalleProcess(Proceso process) throws Exception {
        try {
        	Object param;
            switch (process.getType()) {
                case "ETL":
                    //param = getETLDetail(process);
                	param = null;
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
	
	public Mov getMovDetail(Proceso process) throws Exception {
		try {
			MetaData dbConn = new MetaData(gDatos);
			MetaQuery dbQuery = new MetaQuery(gDatos.getInfo().getDbType());
			Mov mov = new Mov();
			
			
			dbConn.open();
			
			if (dbConn.isConnected()) {
				String vSql = dbQuery.getSqlFindMov(process.getProcID());
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
	
	public String getStatus() {
		/**
		 * Debe retornar la respuesta formateada en json con jHeader y jData
		 */
		
		Map<String, Object> mapResponse = new HashMap<>();
		String data="";
		
		try {
			mapResponse.put("mapModule", gDatos.getMapModule());
			data = mylib.serializeObjectToJSon(mapResponse, false);
			
			return mylib.msgResponse("OK", data, gDatos.getInfo().getAuthKey());
		} catch (IOException e) {
			return mylib.sendError(99, "Error proc: getStatus ("+e.getMessage()+")");
		}
	}
	
	public String syncService(JSONObject jData) {
		try {
			JSONObject jService = new JSONObject(jData.getString("service"));
			
			Map<String, Object> mapSrv = jService.toMap();
			
			gDatos.updateService(mapSrv);
			
			return mylib.sendError(0,"Terminado");
			
		} catch (Exception e) {
			return mylib.sendError(99, "Error proc: syncService ("+e.getMessage()+")");
		}
	}
	
}
