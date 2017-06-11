package eco.hbase.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.sqlDB;
import eco.hbase.model.*;

public class DataGrab {
	Rutinas mylib = new Rutinas();
	String fecIni;
	String fecFin;
	String org;
	String suborg;
	HashMap<String, List<RowModel>> mapGrab = new HashMap<>();
	
	public DataGrab (String fIni, String fFin, String forg, String fsuborg) {
		this.fecIni = fIni;
		this.fecFin = fFin;
		this.org = forg;
		this.suborg = fsuborg;
	}
	
	public HashMap<String, List<RowModel>> getMapGrab() throws Exception {
		return mapGrab;
	}
	
	public void executeDataGrab() throws Exception {
		try {
			
    		//Extrae datos desde SQL
    		sqlDB sqlConn = new sqlDB("172.18.66.51","grabaciones","1433","sa","econtact2010",10);
    		sqlConn.open();
    		
    		mylib.console("Conectado a sqlServer...");
    		
    		String vSql = "select "
    				+ " '"+org+"' ORG, "
    				+ " '"+suborg+"' SUBORG, "
    				+ " UNIQUEID, "
    				+ " ANI, "
    				+ " DNIS, "
    				+ " FECHAINICIO, "
    				+ " FNAME, "
    				+ " UBICACIONLOCAL, "
    				+ " URL, "
    				+ " DURACION "
    				+ " from grabaciones where "
    				+ "		fechainicio  >= '" + fecIni + "' and "
    				+ "		fechainicio   < '" + fecFin + "'";
    		
    		if (sqlConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = sqlConn.getQuery();
    			
    			mylib.console("Se recupero Resultset...");
    	        
    	        List<RowModel> lstCq = new ArrayList<>();
    			while (rs.next()) {
    				lstCq = new ArrayList<>();
    				getLstCq(lstCq, rs);
    				
			    	//String fecIni = mylib.getDateString(rs.getString("FECHAINICIO"), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
			    	String fName = rs.getString("FNAME");

    				String key = "+" + rs.getString("ORG") + "+" + rs.getString("SUBORG") + "+" + fName;
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
				case "UNIQUEID":
					rm.setColumn("fk01");
					rm.setFamily("k1");
					rm.setValue(rs.getString("UNIQUEID"));
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
				case "FECHAINICIO":
					rm.setColumn("fecini");
					rm.setFamily("f0");
					rm.setValue(mylib.getDateString(rs.getString("FECHAINICIO"),"yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss"));
					break;
				case "FNAME":
					rm.setColumn("fname");
					rm.setFamily("f0");
					rm.setValue(rs.getString("FNAME"));
					break;
				case "UBICACIONLOCAL":
					rm.setColumn("localpath");
					rm.setFamily("f1");
					rm.setValue(rs.getString("UBICACIONLOCAL"));
					break;
				case "URL":
					rm.setColumn("url");
					rm.setFamily("f1");
					rm.setValue(rs.getString("URL"));
					break;
				case "DURACION":
					rm.setColumn("duracion");
					rm.setFamily("f0");
					rm.setValue(rs.getString("DURACION"));
					break;
				case "ACCION":
					rm.setColumn("accion");
					rm.setFamily("f1");
					rm.setValue(rs.getString("ACCION"));
					break;
				case "FLAGREPORTES":
					rm.setColumn("flagrep");
					rm.setFamily("f1");
					rm.setValue(rs.getString("FLAGREPORTES"));
					break;
				case "NUMEROTRANSFERENCIA":
					rm.setColumn("numtransf");
					rm.setFamily("f1");
					rm.setValue(rs.getString("NUMEROTRANSFERENCIA"));
					break;
				case "ESTADOLLAMADA":
					rm.setColumn("estadollam");
					rm.setFamily("f1");
					rm.setValue(rs.getString("ESTADOLLAMADA"));
					break;
				case "STATUS":
					rm.setColumn("status");
					rm.setFamily("f1");
					rm.setValue(rs.getString("STATUS"));
					break;
				case "TIPOLLAMADA":
					rm.setColumn("tipollam");
					rm.setFamily("f0");
					rm.setValue(rs.getString("TIPOLLAMADA"));
					break;
				case "PLAT_DBID":
					rm.setColumn("platdbid");
					rm.setFamily("f1");
					rm.setValue(rs.getString("PLAT_DBID"));
					break;
				case "TENANT":
					rm.setColumn("tenant");
					rm.setFamily("f1");
					rm.setValue(rs.getString("TENANT"));
					break;
				case "CORTELLAMADA":
					rm.setColumn("cortellam");
					rm.setFamily("f1");
					rm.setValue(rs.getString("CORTELLAMADA"));
					break;
				}
				if (!mylib.isNullOrEmpty(rm.getValue())) {
					lstRm.add(rm);
				}
			}
			
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	
}
