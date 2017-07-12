package eco.hbase.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.sqlDB;
import eco.hbase.model.*;

public class DataC2C {
	Rutinas mylib = new Rutinas();
	String fecIni;
	String fecFin;
	String org;
	String suborg;
	HashMap<String, List<RowModel>> mapGrab = new HashMap<>();
	
	public DataC2C (String fIni, String fFin, String forg, String fsuborg) {
		this.fecIni = fIni;
		this.fecFin = fFin;
		this.org = forg;
		this.suborg = fsuborg;
	}
	
	public HashMap<String, List<RowModel>> getMapGrab() throws Exception {
		return mapGrab;
	}
	
	public List<String> getOnlyKeys()  throws Exception {
		try {
			List<String> lstKey = new ArrayList<>();
    		//Extrae datos desde SQL
			//172.17.233.185: s1-sqlnod02-vtr.callcenter.vtr.cl
    		sqlDB sqlConn = new sqlDB("eco-sql-002.e-contact.cl","cfg_grabaciones","1433","sqlAdmin","Adminsql02",10);
    		sqlConn.open();
    		
    		mylib.console("Conectado a sqlServer...");
    		
    		String vSql = "select "
    				+ " RecordName "
    				+ " from CALL_RECORD where tenant_dbid=7 and "
    				+ "		RecordStartTime  >= '" + fecIni + "' and "
    				+ "		RecordStartTime   < '" + fecFin + "'";

    		if (sqlConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = sqlConn.getQuery();
    			
    			mylib.console("Recuperando Keys desde SQL Server...");
    			
    			//Generando lista de keys a borrar
    			
    			while (rs.next()) {
    				String key = org+"+"+suborg+"+" + rs.getString("RecordName");
    				lstKey.add(key);
    			}
    			
    			mylib.console("Se generaron "+ lstKey.size() + " keys para borrar");
    			
    			rs.close();
    		}
    		sqlConn.close();
			return lstKey;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
	public void executeDataGrab() throws Exception {
		try {
			
    		//Extrae datos desde SQL
    		sqlDB sqlConn = new sqlDB("eco-sql-002.e-contact.cl","cfg_grabaciones","1433","sqlAdmin","Adminsql02",10);
    		sqlConn.open();
    		
    		mylib.console("Conectado a sqlServer...");
    		
    		String vSql = "select "
    				+ " '"+org+"' ORG, "
    				+ " '"+suborg+"' SUBORG, "
    				+ " ANI, "
    				+ " DNIS, "
    				+ " RecordStartTime, "
    				+ " RecordName, "
    				+ " RecordLocalPath, "
    				+ " RecordStatus, "
    				+ " RecordSearchKey, "
    				+ " RecordURL, "
    				+ " RecordServerName, "
    				+ " CALLID01, "
    				+ " TENANT_DBID, "
    				+ " TRANSCRIPTION, "
    				+ " TRANSCRIPTION_ALT, "
    				+ " TRANSCRIPTION03"
    				+ " from CALL_RECORD where tenant_dbid=7 and "
    				+ "		RecordStartTime  >= '" + fecIni + "' and "
    				+ "		RecordStartTime   < '" + fecFin + "'";
    		
    		if (sqlConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = sqlConn.getQuery();
    			
    			mylib.console("Se recupero Resultset...");
    	        
    	        List<RowModel> lstCq = new ArrayList<>();
    			while (rs.next()) {
    				lstCq = new ArrayList<>();
    				getLstCq(lstCq, rs);
    				
			    	//String fecIni = mylib.getDateString(rs.getString("RecordStartTime"), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			    	String fName = rs.getString("RecordName");

    				String key = rs.getString("ORG") + "+" + rs.getString("SUBORG") + "+" + fName;
					mapGrab.put(key, lstCq);
    			}
    			
    			mylib.console("Completo Mapeo de Filas retornadas...");
    		}

			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void getLstCq(List<RowModel> lstRm, ResultSet rs)  throws Exception {
		try {
			RowModel rm = new RowModel();
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			for (int i=1; i<=numCols; i++) {
				rm = new RowModel();
				boolean isAdd = true;
				switch (rsmd.getColumnName(i)) {
				case "ORG":
					rm.setColumn("org");
					rm.setFamily("f0");
					rm.setValue(rs.getString("ORG"));
					break;
				case "SUBORG":
					rm.setColumn("suborg");
					rm.setFamily("f0");
					rm.setValue(rs.getString("SUBORG"));
					break;
				case "ANI":
					rm.setColumn("ani");
					rm.setFamily("f0");
					rm.setValue(rs.getString("ANI"));
					break;
				case "DNIS":
					rm.setColumn("dnis");
					rm.setFamily("f0");
					rm.setValue(rs.getString("DNIS"));
					break;
				case "RecordStartTime":
					rm.setColumn("fecini");
					rm.setFamily("f0");
					rm.setValue(mylib.getDateString(rs.getString("RecordStartTime"),"yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss"));
					break;
				case "RecordName":
					rm.setColumn("fname");
					rm.setFamily("f0");
					rm.setValue(rs.getString("RecordName"));
					break;
				case "RecordLocalPath":
					rm.setColumn("localpath");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RecordLocalPath"));
					break;
				case "RecordURL":
					rm.setColumn("url");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RecordURL"));
					break;
				case "RecordStatus":
					rm.setColumn("status");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RecordStatus"));
					break;
				case "TENANT_DBID":
					rm.setColumn("tenant");
					rm.setFamily("f1");
					rm.setValue(rs.getString("TENANT_DBID"));
					break;
				case "CALLID01":
					rm.setColumn("callid");
					rm.setFamily("k1");
					rm.setValue(rs.getString("CALLID01"));
					break;
				case "TRANSCRIPTION":
					rm.setColumn("ttext");
					rm.setFamily("f0");
					rm.setValue(mylib.nvlString(rs.getString("TRANSCRIPTION")));
					break;
				case "TRANSCRIPTION_ALT":
					rm.setColumn("ttext2");
					rm.setFamily("f0");
					rm.setValue(mylib.nvlString(rs.getString("TRANSCRIPTION_ALT")));
					break;
				case "TRANSCRIPTION03":
					rm.setColumn("ttext3");
					rm.setFamily("f0");
					rm.setValue(mylib.nvlString(rs.getString("TRANSCRIPTION03")));
					break;
				default:
					isAdd = false;
				}
				if (isAdd) {
					if (!mylib.isNullOrEmpty(rm.getValue())) {
						lstRm.add(rm);
					}
				}
			}
			
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
}
