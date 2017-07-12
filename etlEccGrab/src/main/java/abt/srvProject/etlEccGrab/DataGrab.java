package abt.srvProject.etlEccGrab;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abt.srvProject.dataAccess.oracleDB;
import abt.srvProject.model.RowModel;
import abt.srvProject.srvRutinas.Rutinas;

public class DataGrab {
	Rutinas mylib = new Rutinas();
	String fecIni;
	String fecFin;
	HashMap<String, List<RowModel>> mapGrab = new HashMap<>();
	
	public DataGrab (String fIni, String fFin) {
		this.fecIni = fIni;
		this.fecFin = fFin;
	}
	
	public HashMap<String, List<RowModel>> getMapGrab() throws Exception {
		return mapGrab;
	}
	
	public void executeDataGrab() throws Exception {
		try {
			
    		//Extrae datos desde Oracle
    		oracleDB oraConn = new oracleDB("10.240.8.32","cvprod","1523","ivr","ivr01",10);
    		oraConn.open();
    		
    		mylib.console("Conectado a oracle...");
    		
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
    				+ " from GRABACIONES_ECC_PROD_BIGDATA where BKPBIG=1 and "
    				+ "		FECHA  >= to_date('"+ fecIni + "','rrrr-mm-dd hh24:mi:ss') and "
    				+ "		FECHA   < to_date('"+ fecFin + "','rrrr-mm-dd hh24:mi:ss')";
    		
    		if (oraConn.executeQuery(vSql)) {
    			
    			mylib.console("Query ejecutada...");
    			
    			ResultSet rs = oraConn.getQuery();
    			
    			mylib.console("Se recupero Resultset...");
    	        
    	        List<RowModel> lstCq = new ArrayList<>();
    			while (rs.next()) {
    				lstCq = new ArrayList<>();
    				getLstCq(lstCq, rs);
    				
    				String key = rs.getString("SKILL") + "+" + rs.getString("FECHA") + "+" + rs.getString("IDCDR");
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
				case "ANI":
					rm.setColumn("01");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("ANI"));
					break;
				case "DNIS":
					rm.setColumn("02");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("DNIS"));
					break;
				case "NUMERODISCADO":
					rm.setColumn("03");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("NUMERODISCADO"));
					break;
				case "CONNID":
					rm.setColumn("04");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("CONNID"));
					break;
				case "UNIQUEID":
					rm.setColumn("05");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("UNIQUEID"));
					break;
				case "STARTTIME":
					rm.setColumn("06");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("STARTTIME"));
					break;
				case "ENDTIME":
					rm.setColumn("07");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("ENDTIME"));
					break;
				case "FECHA":
					rm.setColumn("08");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("FECHA"));
					break;
				case "DURATIONTOTAL":
					rm.setColumn("09");
					rm.setFamily("cf1");
					rm.setValue(rs.getString("DURATIONTOTAL"));
					break;
				case "STATUSCHANNEL":
					rm.setColumn("10");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("STATUSCHANNEL")));
					break;
				case "CHANNEL":
					rm.setColumn("11");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("CHANNEL")));
					break;
				case "PROVEEDOR":
					rm.setColumn("12");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("PROVEEDOR")));
					break;
				case "ASTERISKSERVER":
					rm.setColumn("13");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("ASTERISKSERVER")));
					break;
				case "SERVICIO":
					rm.setColumn("14");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("SERVICIO")));
					break;
				case "CALLSONLINE":
					rm.setColumn("15");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("CALLSONLINE")));
					break;
				case "PATHRECORDER":
					rm.setColumn("16");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("PATHRECORDER")));
					break;
				case "NAMERECORDER":
					rm.setColumn("17");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("NAMERECORDER")));
					break;
				case "RECORDNUM":
					rm.setColumn("18");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("RECORDNUM")));
					break;
				case "ATTRIBUTETHISQUEUE":
					rm.setColumn("19");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("ATTRIBUTETHISQUEUE")));
					break;
				case "ATTRIBUTEOTHERDN":
					rm.setColumn("20");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("ATTRIBUTEOTHERDN")));
					break;
				case "ATTRIBUTETHISDN":
					rm.setColumn("21");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("ATTRIBUTETHISDN")));
					break;
				case "USERDATA2GENESYS":
					rm.setColumn("22");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("USERDATA2GENESYS")));
					break;
				case "VIRTUALQUEUE":
					rm.setColumn("23");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("VIRTUALQUEUE")));
					break;
				case "SIPCALLERID":
					rm.setColumn("24");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("SIPCALLERID")));
					break;
				case "CODIGOSERVICIO":
					rm.setColumn("25");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("CODIGOSERVICIO")));
					break;
				case "INTERACTION_ID":
					rm.setColumn("26");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("INTERACTION_ID")));
					break;
				case "RESULTADO_SEGMENTO":
					rm.setColumn("27");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("RESULTADO_SEGMENTO")));
					break;
				case "TTR":
					rm.setColumn("28");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("TTR")));
					break;
				case "URL":
					rm.setColumn("29");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("URL")));
					break;
				case "TIPO_INTERACTION":
					rm.setColumn("30");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("TIPO_INTERACTION")));
					break;
				case "SKILL":
					rm.setColumn("31");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("SKILL")));
					break;
				case "TIPO_RECURSO":
					rm.setColumn("32");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("TIPO_RECURSO")));
					break;
				case "NOMBRE_RECURSO":
					rm.setColumn("33");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("NOMBRE_RECURSO")));
					break;
				case "RUT":
					rm.setColumn("34");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("RUT")));
					break;
				case "NOMBRE":
					rm.setColumn("35");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("NOMBRE")));
					break;
				case "GESTION":
					rm.setColumn("36");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("GESTION")));
					break;
				case "ROL_RECURSO":
					rm.setColumn("37");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("ROL_RECURSO")));
					break;
				case "AGENTE":
					rm.setColumn("38");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("AGENTE")));
					break;
				case "IDCDR":
					rm.setColumn("39");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("IDCDR")));
					break;
				case "INFOGESTION":
					rm.setColumn("40");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("INFOGESTION")));
					break;
				case "BKPFTP":
					rm.setColumn("41");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("BKPFTP")));
					break;
				case "PATHRECORDER2":
					rm.setColumn("42");
					rm.setFamily("cf1");
					rm.setValue(mylib.nvlString(rs.getString("PATHRECORDER2")));
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
