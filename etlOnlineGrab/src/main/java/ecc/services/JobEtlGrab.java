package ecc.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import common.dataAccess.HBaseDB;
import common.dataAccess.oracleDB;
import common.model.colModel;
import common.rutinas.Rutinas;
import ecc.utiles.GlobalArea;

public class JobEtlGrab extends Thread {
	static final String MODULE="thJobEtlGrab";
	static Logger logger = Logger.getLogger(MODULE);
	static Rutinas mylib = new Rutinas();
	static GlobalArea gDatos;
	
	//Control de Ejecucion del servicio
	boolean init;
	
	public JobEtlGrab(GlobalArea m) {
		try {
			gDatos = m;
			if (mylib.fileExist(gDatos.getLog4j())) {
				PropertyConfigurator.configure(gDatos.getLog4j());
				logger.info("Constructor iniciado");
				logger.info("Logger Name: "+logger.getName());
				logger.info("Logger Level: "+mylib.getLoggerLevel(logger));
				logger.setLevel(Level.INFO);
				logger.info("Logger SET Level: "+mylib.getLoggerLevel(logger));
				logger.trace("Logger Trace Enable");
				logger.debug("Logger DEBUG Enable");
				logger.info("Logger INFO Enable");
				init = true;
			} else {
				mylib.console(1,"Archivo no encontrado: "+gDatos.getLog4j());
				init = false;
			}
		} catch (Exception e) {
			mylib.console(1,"Error en constructor "+MODULE+" ("+e.getMessage()+")");
			init = false;
		}
	}
	
    @Override
    public void run() {
    	if (init) {
		    Timer timerMain = new Timer("thJobEtlGrab");
		    timerMain.schedule(new mainTask(), 5000, 10000);
		    logger.info("Servicio "+MODULE+" agendado cada: 10 segundos");
    	} else {
    		mylib.console(1,"Abortando servicio por error en constructor "+MODULE);
    	}
}

    static class mainTask extends TimerTask {
    	
    	public mainTask() {
    	}
    	
		public void run() {
			try {
        		/**
        		 * Inicia ciclo del Modulo
        		 */
				
				//variables de datos
				HashMap<String, List<colModel>> mapGrab = new HashMap<>();
				List<String> lstIDCDR = new ArrayList<>();
				
        		logger.info("Iniciando ciclo "+MODULE);

				oracleDB dbConn = new oracleDB(gDatos.getGrabDbHost(),
												gDatos.getGrabDbName(), 
												gDatos.getGrabDbPort(), 
												gDatos.getGrabDbUser(), 
												gDatos.getGrabDbPass(), 
												10);
				logger.info("Conectando a BD");
				dbConn.open();
				
				if (dbConn.isConnected()) {
					logger.info("Conexion a BD Exitosa");
					String vSql = getQueryEtl();
					logger.info("Ejecutando Query Etl...");
					if (dbConn.executeQuery(vSql)) {
						ResultSet rs = dbConn.getQuery();
						
		    	        List<colModel> lstCq = new ArrayList<>();

		    	        logger.info("Generando Mapeo de Datos para Hbase...");
		    			while (rs.next()) {
		    				lstCq = new ArrayList<>();
		    				getLstCq(lstCq, rs);
		    				
		    				String key = rs.getString("SKILL") + "+" + rs.getString("FECHA") + "+" + rs.getString("IDCDR");
							mapGrab.put(key, lstCq);
							lstIDCDR.add(rs.getString("IDCDR"));
		    			}
		    			rs.close();
		    			
		    			logger.info("Se mapearon "+mapGrab.size()+ " registros para ser insertados en HBAse");
		    			
		    			if (mapGrab.size()>0) {
			    			logger.info("Insertando Registros en HBase");
			    			logger.info("Conectando a HBase...");
			    			
			    			HBaseDB hbConn = new HBaseDB();
			    			hbConn.setConfig(gDatos.getFileConfig(), gDatos.getClusterName(), "grabaciones");
			    			hbConn.putRow(mapGrab);
			    			logger.info("Registros ensertados exitosamente en HBase");
			    			
							//Actualizando status en base origen
			    			logger.info("Actualizando status Registros en Base Origen");
			    			
			    			int rowsAffected = lstIDCDR.size();
			    			int rowsUpdated = 0;
			    			for (String idCDR : lstIDCDR) {
			    				String vUpd = getQueryUpdateFlag(idCDR);
			    				int updatedRows = dbConn.executeUpdate(vUpd);
			    				if (updatedRows==1) {
			    					rowsUpdated++;
			    				}
			    			}
			    			logger.info("Total de Registros afectos a Modificacion: "+rowsAffected);
			    			logger.info("Total de Registros modificados: "+rowsUpdated);
			    			
		    			} else {
		    				logger.info("No hay registros nuevos para ser insertados en HBase");
		    			}
		    			
					} else {
						logger.error("No es posible ejecutar la query de etl");
					}
					
					dbConn.close();
					logger.info("Cerrado BD Exitoso");
				} else {
					logger.error("No es posible conectarse a BD");
				}
				
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.info("Terminado ciclo "+MODULE);
			} catch (Exception e) {
    			logger.error("Error inesperado "+MODULE+" ("+e.getMessage()+")");
        		/**
        		 * Finalizando ciclo del Modulo
        		 */
        		logger.error("Terminado ciclo "+MODULE+" con Error");
			}
		}
		
		private String getQueryUpdateFlag(String IDCDR) throws Exception {
			String vSql = "update GRABACIONES_ECC_PROD_BIGDATA "
					+ "			set BKPBIG = "+gDatos.getGrabValUpd()+" "
					+ "		where "
					+ "			BKPBIG = "+gDatos.getGrabValLoad()+" and "
					+ "			IDCDR = "+IDCDR;
			return vSql;
		}
		
		private String getQueryEtl() throws Exception {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, gDatos.getDaysBack()*(-1));
			Date fecha = cal.getTime();
			String strFecha = dateFormat.format(fecha);

    		String vSql = "select "
    				+ " ANI, "
    				+ " DNIS, "
    				+ " NUMERODISCADO, "
    				+ " CONNID, "
    				+ " UNIQUEID, "
    				+ " STARTTIME, "
    				+ " ENDTIME, "
    				+ " to_char(FECHA,'rrrrmmddhh24miss') FECHA, "
    				+ " DURATIONTOTAL, "
    				+ " STATUSCHANNEL, "
    				+ " CHANNEL, "
    				+ " PROVEEDOR, "
    				+ " ASTERISKSERVER, "
    				+ " SERVICIO, "
    				+ " CALLSONLINE, "
    				+ " PATHRECORDER, "
    				+ " NAMERECORDER, "
    				+ " RECORDNUM, "
    				+ " ATTRIBUTETHISQUEUE, "
    				+ " ATTRIBUTEOTHERDN, "
    				+ " ATTRIBUTETHISDN, "
    				+ " USERDATA2GENESYS, "
    				+ " VIRTUALQUEUE, "
    				+ " SIPCALLERID, "
    				+ " CODIGOSERVICIO, "
    				+ " INTERACTION_ID, "
    				+ " RESULTADO_SEGMENTO, "
    				+ " TTR, "
    				+ " URL, "
    				+ " TIPO_INTERACTION, "
    				+ " SKILL, "
    				+ " TIPO_RECURSO, "
    				+ " NOMBRE_RECURSO, "
    				+ " RUT, "
    				+ " NOMBRE, "
    				+ " GESTION, "
    				+ " ROL_RECURSO, "
    				+ " AGENTE, "
    				+ " IDCDR, "
    				+ " INFOGESTION, "
    				+ " BKPFTP, "
    				+ " PATHRECORDER2 "
    				+ " from GRABACIONES_ECC_PROD_BIGDATA where BKPBIG="+gDatos.getGrabValLoad()+" and "
    				+ "		FECHA  >= to_date('"+ strFecha + "','rrrr-mm-dd hh24:mi:ss') and "
    				+ "     rownum <= 5000";
    		return vSql;
		}
		
		private void getLstCq(List<colModel> lstCm, ResultSet rs)  throws Exception {
			try {
				colModel cm = new colModel();
				ResultSetMetaData rsmd = rs.getMetaData();
				int numCols = rsmd.getColumnCount();
				for (int i=1; i<=numCols; i++) {
					cm = new colModel();
					boolean isAdd = true;
					switch (rsmd.getColumnName(i)) {
					case "ANI":
						cm.setColumn("01");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("ANI"));
						break;
					case "DNIS":
						cm.setColumn("02");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("DNIS"));
						break;
					case "NUMERODISCADO":
						cm.setColumn("03");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("NUMERODISCADO"));
						break;
					case "CONNID":
						cm.setColumn("04");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("CONNID"));
						break;
					case "UNIQUEID":
						cm.setColumn("05");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("UNIQUEID"));
						break;
					case "STARTTIME":
						cm.setColumn("06");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("STARTTIME"));
						break;
					case "ENDTIME":
						cm.setColumn("07");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("ENDTIME"));
						break;
					case "FECHA":
						cm.setColumn("08");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("FECHA"));
						break;
					case "DURATIONTOTAL":
						cm.setColumn("09");
						cm.setFamily("cf1");
						cm.setValue(rs.getString("DURATIONTOTAL"));
						break;
					case "STATUSCHANNEL":
						cm.setColumn("10");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("STATUSCHANNEL")));
						break;
					case "CHANNEL":
						cm.setColumn("11");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("CHANNEL")));
						break;
					case "PROVEEDOR":
						cm.setColumn("12");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("PROVEEDOR")));
						break;
					case "ASTERISKSERVER":
						cm.setColumn("13");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("ASTERISKSERVER")));
						break;
					case "SERVICIO":
						cm.setColumn("14");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("SERVICIO")));
						break;
					case "CALLSONLINE":
						cm.setColumn("15");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("CALLSONLINE")));
						break;
					case "PATHRECORDER":
						cm.setColumn("16");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("PATHRECORDER")));
						break;
					case "NAMERECORDER":
						cm.setColumn("17");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("NAMERECORDER")));
						break;
					case "RECORDNUM":
						cm.setColumn("18");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("RECORDNUM")));
						break;
					case "ATTRIBUTETHISQUEUE":
						cm.setColumn("19");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("ATTRIBUTETHISQUEUE")));
						break;
					case "ATTRIBUTEOTHERDN":
						cm.setColumn("20");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("ATTRIBUTEOTHERDN")));
						break;
					case "ATTRIBUTETHISDN":
						cm.setColumn("21");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("ATTRIBUTETHISDN")));
						break;
					case "USERDATA2GENESYS":
						cm.setColumn("22");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("USERDATA2GENESYS")));
						break;
					case "VIRTUALQUEUE":
						cm.setColumn("23");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("VIRTUALQUEUE")));
						break;
					case "SIPCALLERID":
						cm.setColumn("24");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("SIPCALLERID")));
						break;
					case "CODIGOSERVICIO":
						cm.setColumn("25");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("CODIGOSERVICIO")));
						break;
					case "INTERACTION_ID":
						cm.setColumn("26");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("INTERACTION_ID")));
						break;
					case "RESULTADO_SEGMENTO":
						cm.setColumn("27");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("RESULTADO_SEGMENTO")));
						break;
					case "TTR":
						cm.setColumn("28");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("TTR")));
						break;
					case "URL":
						cm.setColumn("29");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("URL")));
						break;
					case "TIPO_INTERACTION":
						cm.setColumn("30");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("TIPO_INTERACTION")));
						break;
					case "SKILL":
						cm.setColumn("31");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("SKILL")));
						break;
					case "TIPO_RECURSO":
						cm.setColumn("32");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("TIPO_RECURSO")));
						break;
					case "NOMBRE_RECURSO":
						cm.setColumn("33");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("NOMBRE_RECURSO")));
						break;
					case "RUT":
						cm.setColumn("34");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("RUT")));
						break;
					case "NOMBRE":
						cm.setColumn("35");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("NOMBRE")));
						break;
					case "GESTION":
						cm.setColumn("36");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("GESTION")));
						break;
					case "ROL_RECURSO":
						cm.setColumn("37");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("ROL_RECURSO")));
						break;
					case "AGENTE":
						cm.setColumn("38");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("AGENTE")));
						break;
					case "IDCDR":
						cm.setColumn("39");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("IDCDR")));
						break;
					case "INFOGESTION":
						cm.setColumn("40");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("INFOGESTION")));
						break;
					case "BKPFTP":
						cm.setColumn("41");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("BKPFTP")));
						break;
					case "PATHRECORDER2":
						cm.setColumn("42");
						cm.setFamily("cf1");
						cm.setValue(mylib.nvlString(rs.getString("PATHRECORDER2")));
						break;
					default:
						isAdd = false;
					}
					if (isAdd) {
						if (!mylib.isNullOrEmpty(cm.getValue())) {
							lstCm.add(cm);
						}
					}
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
    } //end class mainTask
}
