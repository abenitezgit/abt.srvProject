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

public class DataOreka {
	Rutinas mylib = new Rutinas();
	String fecIni;
	String fecFin;
	String org;
	String suborg;
	HashMap<String, List<RowModel>> mapGrab = new HashMap<>();
	
	public DataOreka (String fIni, String fFin, String forg, String fsuborg) {
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
    		sqlDB sqlConn = new sqlDB("172.18.66.51","base_oreka","1433","sa","econtact2010",10);
    		sqlConn.open();
    		
    		mylib.console("Conectado a sqlServer...");
    		
    		String vSql = "select "
    				+ " RECORDNAME "
    				+ " from call_record where "
    				+ "		RECORDSTARTTIME  >= '" + fecIni + "' and "
    				+ "		RECORDSTARTTIME   < '" + fecFin + "'";

    		if (sqlConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = sqlConn.getQuery();
    			
    			mylib.console("Recuperando Keys desde SQL Server...");
    			
    			//Generando lista de keys a borrar
    			
    			while (rs.next()) {
    				String key = "+"+org+"+"+suborg+"+" + rs.getString("RECORDNAME");
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
    		sqlDB sqlConn = new sqlDB("172.18.66.51","base_oreka","1433","sa","econtact2010",10);
    		sqlConn.open();
    		
    		mylib.console("Conectado a sqlServer...");
    		
    		String vSql = "select "
    				+ " '"+org+"' ORG, "
    				+ " '"+suborg+"' SUBORG, "
    				+ " ANI, "
    				+ " DNIS, "
    				+ " SIPCALLERID, "
    				+ " RECORDSTARTTIME, "
    				+ " RECORDENDTIME, "
    				+ " RECORDNAME, "
    				+ " RECORDLOCALPATH, "
    				+ " RECORDSTATUS, "
    				+ " RECORDMODE, "
    				+ " RECORDSEARCHKEY, "
    				+ " RECORDURL, "
    				+ " RECORDSERVERNAME, "
    				+ " TENANT_DBID, "
    				+ " CUSTOM_CHAR01 "
    				+ " from call_record where "
    				+ "		RECORDSTARTTIME  >= '" + fecIni + "' and "
    				+ "		RECORDSTARTTIME   < '" + fecFin + "'";
    		
    		if (sqlConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = sqlConn.getQuery();
    			
    			mylib.console("Se recupero Resultset...");
    	        
    	        List<RowModel> lstCq = new ArrayList<>();
    			while (rs.next()) {
    				lstCq = new ArrayList<>();
    				getLstCq(lstCq, rs);
    				
    				if (!mylib.isNullOrEmpty(rs.getString("RECORDNAME"))) {
				    	//String fecIni = mylib.getDateString(rs.getString("FECHAINICIO"), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
				    	String fName = rs.getString("RECORDNAME");
	
	    				String key = rs.getString("ORG") + "+" + rs.getString("SUBORG") + "+" + fName;
						mapGrab.put(key, lstCq);
    				}
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
				switch (rsmd.getColumnName(i).toUpperCase()) {
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
				case "SIPCALLERID":
					rm.setColumn("fk02");
					rm.setFamily("k1");
					rm.setValue(rs.getString("SIPCALLERID"));
					break;
				case "RECORDSTARTTIME":
					rm.setColumn("fecini");
					rm.setFamily("f0");
					rm.setValue(mylib.getDateString(rs.getString("RECORDSTARTTIME"),"yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss"));
					break;
				case "RECORDENDTIME":
					rm.setColumn("fecterm");
					rm.setFamily("f1");
					rm.setValue(mylib.getDateString(rs.getString("RECORDENDTIME"),"yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss"));
					break;
				case "RECORDNAME":
					rm.setColumn("fname");
					rm.setFamily("f0");
					rm.setValue(rs.getString("RECORDNAME"));
					break;
				case "RECORDLOCALPATH":
					rm.setColumn("localpath");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RECORDLOCALPATH"));
					break;
				case "RECORDSTATUS":
					rm.setColumn("status");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RECORDSTATUS"));
					break;
				case "RECORDSEARCHKEY":
					rm.setColumn("fk03");
					rm.setFamily("k1");
					rm.setValue(rs.getString("RECORDSEARCHKEY"));
					break;
				case "RECORDURL":
					rm.setColumn("url");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RECORDURL"));
					break;
				case "RECORDSERVERNAME":
					rm.setColumn("server");
					rm.setFamily("f1");
					rm.setValue(rs.getString("RECORDSERVERNAME"));
					break;
				case "TENANT_DBID":
					rm.setColumn("tenant");
					rm.setFamily("f1");
					rm.setValue(rs.getString("TENANT_DBID"));
					break;
				case "CUSTOM_CHAR01":
					rm.setColumn("custom1");
					rm.setFamily("f2");
					rm.setValue(rs.getString("CUSTOM_CHAR01"));
					break;
				}
				if (!mylib.isNullOrEmpty(rm.getValue())) {
					lstRm.add(rm);
				}
			} //end for
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
}
