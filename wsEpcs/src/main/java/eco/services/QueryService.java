package eco.services;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import common.dataAccess.mysqlDB;
import common.rutinas.Rutinas;
import eco.model.DataRequest;
import eco.model.DataResponse;
import eco.model.QueryResponse;
import eco.utiles.GlobalArea;

public class QueryService {
	GlobalArea gDatos = new GlobalArea();
	Rutinas mylib = new Rutinas();
	DataRequest dr = new DataRequest();
	DataResponse dResp = new DataResponse();
	QueryResponse qResp = new QueryResponse();
	final String proyecto = "wsEpcs/query";
	
	
	//Parametros de control
	int rowsAfected=0;
	String query;
	String mapRow;
	
	//Parametros de configuracion
	String host;
	String dbPort;
	String dbName;
	String dbUser;
	String dbPass;
	
	//Flags
	boolean flagInitComponents = false;
	boolean flagExecuteRequest = false;
	
	//Getter and setter
	public String getMetodo() {
		return dr.getMetodo();
	}

	public String getQuery() {
		return query;
	}
	
	public boolean isInitComponents() {
		return flagInitComponents;
	}
	
	public boolean isExecuteRequest() {
		return flagExecuteRequest;
	}
	
	public int getRowsAfected() {
		return rowsAfected;
	}
	
	public int getRowsLimit() {
		return dr.getLimit();
	}
	
	public String getMapRow() {
		return mapRow;
	}
	
	//Procedimientos privados
    private void fillDataRequest(String dataInput) throws Exception {
    	try {
    		dr = (DataRequest) mylib.serializeJSonStringToObject(dataInput, DataRequest.class);
    	} catch (Exception e) {
    		mylib.console(1,proyecto,"Error fillDataRequest: "+e.getMessage());
    		throw new Exception(e.getMessage());
    	}
    }
    
    private void setConfig(String fileConfig) throws Exception {
    	try {
    		Properties cfg = new Properties();
    		cfg.load(new FileInputStream(fileConfig));
    		
    		host = cfg.getProperty("host");
    		dbPort = cfg.getProperty("dbPort");
    		dbName = cfg.getProperty("dbName");
    		dbUser = cfg.getProperty("dbUser");
    		dbPass = cfg.getProperty("dbPass");
    		
    		cfg.clear();
    		
    	} catch (Exception e) {
			mylib.console(1,proyecto,"Error setConfig: "+e.getMessage());
			throw new Exception(e.getMessage());
    	}
    }
    
	private boolean isValDataInsertSeg() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getIntEnc())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getCantEnt())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getAccTrans())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getMesgAcc())) {
			valida = false;
		}
		return valida;
	}
	
	private boolean isValDataInsertEnt() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getExist())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getAccTrans())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getMesgAcc())) {
			valida = false;
		}
		return valida;
	}
	
	private boolean isValDataSelectCmbEnt() throws Exception {
		boolean valida=true;
		return valida;
	}

	private boolean isValDataSelectEnt() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getLimit())) {
			valida = false;
		}
		return valida;
	}

	private boolean isValDataSelectSeg() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getIntEnc())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getLimit())) {
			valida = false;
		}
		return valida;
	}
	
	private boolean isValDataDeleteEnt() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		return valida;
	}

	private boolean isValDataDeleteSeg() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getIntEnc())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getCantEnt())) {
			valida = false;
		}
		return valida;
	}
	
	private boolean isValDataUpdateEnt() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getExist())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getAccTrans())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getMesgAcc())) {
			valida = false;
		}
		return valida;
	}
	
	private boolean isValDataUpdateSeg() throws Exception {
		boolean valida=true;
		if (mylib.isNullOrEmpty(dr.getIntEnc())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getEntAct())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getCantEnt())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getAccTrans())) {
			valida = false;
		}
		if (mylib.isNullOrEmpty(dr.getMesgAcc())) {
			valida = false;
		}
		return valida;
	}
	
	private String getQueryInsertEnt() {
		String vSql = "insert into entidades ("
				+ "								entidad, "
				+ "								existe, "
				+ "								accion_transferencia, "
				+ "								mensaje_accion_audio "
				+ "							) "
				+ "		values				( "
				+ "								'"+dr.getEntAct()+"', "
				+ "								'"+dr.getExist()+"', "
				+ "								'"+dr.getAccTrans()+"', "		
				+ "								'"+dr.getMesgAcc()+"' "
				+ "							)";
		return vSql;
	}

	private String getQueryInsertSeg() {
		String vSql = "insert into segmentacion ("
				+ "								intencion, "
				+ "								entidad, "
				+ "								flag, "
				+ "								accion_transferencia, "
				+ "								mensaje_accion_audio "
				+ "							) "
				+ "		values				( "
				+ "								'"+dr.getIntEnc()+"', "
				+ "								'"+dr.getEntAct()+"', "
				+ "								'"+dr.getCantEnt()+"', "
				+ "								'"+dr.getAccTrans()+"', "		
				+ "								'"+dr.getMesgAcc()+"' "
				+ "							)";
		return vSql;
	}
	
	private String getQuerySelectCmbEnt() {
		String vSql = "select "
					+ " entidad "
					+ " from entidades ";
		return vSql;
	}
	
	private String getQuerySelectEnt() {
		String sufijoLimit = " limit "+ dr.getLimit();
		String vSql = "";
		
		if (dr.getEntAct().equals("*")) {
			vSql = "select "
					+ " entidad, "
					+ " existe, "
					+ " accion_transferencia, "
					+ " mensaje_accion_audio "
					+ " from entidades ";
		} else {
			vSql = "select "
					+ " entidad, "
					+ " existe, "
					+ " accion_transferencia, "
					+ " mensaje_accion_audio "
					+ " from entidades "
					+ " where "
					+ "		entidad = '"+dr.getEntAct()+"' ";
		}
		
		vSql = vSql + sufijoLimit;
		return vSql;
	}

	private String getQuerySelectSeg() {
		String sufijoLimit = " limit "+ dr.getLimit();
		String vSql = "";
		
		if (dr.getIntEnc().equals("*")) {
			vSql = "select "
					+ " intencion, "
					+ " entidad, "
					+ " flag, "
					+ " accion_transferencia, "
					+ " mensaje_accion_audio "
					+ " from segmentacion ";
		} else {
			vSql = "select "
					+ " intencion, "
					+ " entidad, "
					+ " flag, "
					+ " accion_transferencia, "
					+ " mensaje_accion_audio "
					+ " from segmentacion "
					+ " where "
					+ "		intencion = '"+dr.getIntEnc()+"' ";
		}
		
		vSql = vSql + sufijoLimit;
		return vSql;
	}
	
	private String getQueryDeleteEnt() {
		String vSql = "delete from entidades "
				+ "		where entidad = '"+dr.getEntAct()+"'";
		return vSql;
	}
	
	private String getQueryDeleteSeg() {
		String vSql = "delete from segmentacion "
				+ "		where intencion = '"+dr.getIntEnc()+"' "
						+ " and entidad = '"+dr.getEntAct()+"' "
						+ " and flag = '"+dr.getCantEnt()+"'";
		return vSql;
	}
	
	private String getQueryUpdateEnt() {
		String vSql = "update entidades "
				+ "		set "
				+ "			existe = '"+dr.getExist()+"', "
				+ "			accion_transferencia = '"+dr.getAccTrans()+"', "
				+ "			mensaje_accion_audio = '"+dr.getMesgAcc()+"' "
				+ "		where "
				+ "			entidad = '"+dr.getEntAct()+"'";
		return vSql;
	}
	
	private String getQueryUpdateSeg() {
		String vSql = "update segmentacion "
				+ "		set "
				+ "			accion_transferencia = '"+dr.getAccTrans()+"', "
				+ "			mensaje_accion_audio = '"+dr.getMesgAcc()+"' "
				+ "		where intencion = '"+dr.getIntEnc()+"' "
				+ " 			and entidad = '"+dr.getEntAct()+"' "
				+ " 			and flag = '"+dr.getCantEnt()+"'";
		return vSql;
	}

	private int InsertEnt() throws Exception {
		int rowsInserted=0;
		if (isValDataInsertEnt()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryInsertEnt();
				rowsInserted = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error InsertEnt, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error InsertEnt, no pudo validar datos entrada");
		}
		return rowsInserted;
	}
	
	private int InsertSeg() throws Exception {
		int rowsInserted=0;
		if (isValDataInsertSeg()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryInsertSeg();
				rowsInserted = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error InsertSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error InsertSeg, no pudo validar datos entrada");
		}
		return rowsInserted;
	}
	
	private String SelectCmbEnt() throws Exception {
		JSONObject header = new JSONObject();
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		if (isValDataSelectCmbEnt()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQuerySelectCmbEnt();
				mylib.console("Query a ejecutar: "+query);
				if (myConn.executeQuery(query)) {
					ResultSet rs = myConn.getQuery();
					while (rs.next()) {
						jo = new JSONObject();
						
						jo.put("value", rs.getString(1));
						jo.put("text", rs.getString(1));

						ja.put(jo);
					}
					header.put("options", ja);
					rs.close();
				} else {
					mylib.console(1,proyecto,"Error selecEnt: no pudo ejecutar query");
					throw new Exception("Error selecEnt: no pudo ejecutar query");
				}
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error InsertSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error InsertSeg, no pudo validar datos entrada");
		}
		return header.toString();
	}
	
	private String selectEnt() throws Exception {
		JSONObject head = new JSONObject();
		JSONObject joId = new JSONObject();
		JSONArray jaData = new JSONArray();
		JSONArray jaRows = new JSONArray();
		if (isValDataSelectEnt()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQuerySelectEnt();
				mylib.console("Query a ejecutar: "+query);
				if (myConn.executeQuery(query)) {
					ResultSet rs = myConn.getQuery();
					int r =1;
					while (rs.next()) {
						jaData = new JSONArray();
						joId = new JSONObject();
						
						jaData.put(rs.getString(1));
						jaData.put(rs.getString(2));
						jaData.put(rs.getString(3));
						jaData.put(rs.getString(4));
						
						joId.put("id", r);
						r++;
						joId.put("data", jaData);
						
						jaRows.put(joId);
						
						head.put("rows", jaRows);
					}
					rs.close();
				} else {
					mylib.console(1,proyecto,"Error selecEnt: no pudo ejecutar query");
					throw new Exception("Error selecEnt: no pudo ejecutar query");
				}
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error InsertSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error InsertSeg, no pudo validar datos entrada");
		}
		return head.toString();
	}

	private String selectSeg() throws Exception {
		JSONObject head = new JSONObject();
		JSONObject joId = new JSONObject();
		JSONArray jaData = new JSONArray();
		JSONArray jaRows = new JSONArray();
		if (isValDataSelectSeg()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQuerySelectSeg();
				if (myConn.executeQuery(query)) {
					ResultSet rs = myConn.getQuery();
					int r=1;
					while (rs.next()) {
						jaData = new JSONArray();
						joId = new JSONObject();
						
						jaData.put(rs.getString(1));
						jaData.put(rs.getString(2));
						jaData.put(rs.getString(3));
						jaData.put(rs.getString(4));
						jaData.put(rs.getString(5));
						
						joId.put("id", r);
						r++;
						joId.put("data", jaData);
						
						jaRows.put(joId);
						
						head.put("rows", jaRows);
					}
					rs.close();
				} else {
					mylib.console(1,proyecto,"Error selecSeg: no pudo ejecutar query");
					throw new Exception("Error selecSeg: no pudo ejecutar query");
				}
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error InsertSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error InsertSeg, no pudo validar datos entrada");
		}
		return head.toString();
	}

	private int DeleteEnt() throws Exception {
		int rowsDeleted=0;
		if (isValDataDeleteEnt()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryDeleteEnt();
				mylib.console("Query: "+query);
				rowsDeleted = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error DeleteEnt, no pudo conectarse a BD");
				throw new Exception("Error DeleteEnt, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error DeleteEnt, no pudo validar datos entrada");
			throw new Exception("Error DeleteEnt, no pudo validar datos entrada");
		}
		return rowsDeleted;
	}

	private int DeleteSeg() throws Exception {
		int rowsDeleted=0;
		if (isValDataDeleteSeg()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryDeleteSeg();
				mylib.console("Query: "+query);
				rowsDeleted = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error DeleteSeg, no pudo conectarse a BD");
				throw new Exception("Error DeleteSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error DeleteSeg, no pudo validar datos entrada");
			throw new Exception("Error DeleteSeg, no pudo validar datos entrada");
		}
		return rowsDeleted;
	}

	private int UpdateEnt() throws Exception {
		int rowsUpdated=0;
		if (isValDataUpdateEnt()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryUpdateEnt();
				mylib.console("Query: "+query);
				rowsUpdated = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error UpdateEnt, no pudo conectarse a BD");
				throw new Exception("Error UpdateEnt, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error UpdateEnt, no pudo validar datos entrada");
			throw new Exception("Error UpdateEnt, no pudo validar datos entrada");
		}
		return rowsUpdated;
	}
	
	private int UpdateSeg() throws Exception {
		int rowsUpdated=0;
		if (isValDataUpdateSeg()) {
			mysqlDB myConn = new mysqlDB(host, dbName, dbPort, dbUser, dbPass, 10);
			myConn.open();
			
			if (myConn.isConnected()) {
				mylib.console(proyecto,"Conectado a mySQL");
				query = getQueryUpdateSeg();
				mylib.console("Query: "+query);
				rowsUpdated = myConn.executeUpdate(query);
				myConn.close();
			} else {
				mylib.console(1,proyecto,"Error UpdateSeg, no pudo conectarse a BD");
				throw new Exception("Error UpdateSeg, no pudo conectarse a BD");
			}
		} else {
			mylib.console(1,proyecto,"Error UpdateSeg, no pudo validar datos entrada");
			throw new Exception("Error UpdateSeg, no pudo validar datos entrada");
		}
		return rowsUpdated;
	}

	//Procedimientos publicos
	public void initComponents(String dataInput) throws Exception {
		try {
			fillDataRequest(dataInput);
			
			setConfig(gDatos.getFileConfig());
			
			flagInitComponents = true;
		} catch (Exception e) {
			mylib.console(1,proyecto,"Error initComponents: "+e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public void executeRequest() throws Exception {
		switch(dr.getMetodo()) {
			case "InsertEnt":
				rowsAfected = InsertEnt();
				if (rowsAfected>0) {
					flagExecuteRequest = true;
				}
				break;
			case "InsertSeg":
				rowsAfected = InsertSeg();
				if (rowsAfected>0) {
					flagExecuteRequest = true;
				}
				break;
			case "SelectEnt":
				mapRow = selectEnt();
				rowsAfected = 0;
				flagExecuteRequest = true;
				break;
			case "SelectSeg":
				mapRow = selectSeg();
				rowsAfected = 0;
				flagExecuteRequest = true;
				break;
			case "DeleteEnt":
				rowsAfected = DeleteEnt();
				flagExecuteRequest = true;
				break;
			case "DeleteSeg":
				rowsAfected = DeleteSeg();
				flagExecuteRequest = true;
				break;
			case "UpdateEnt":
				rowsAfected = UpdateEnt();
				if (rowsAfected>0) {
					flagExecuteRequest = true;
				}
				break;
			case "UpdateSeg":
				rowsAfected = UpdateSeg();
				if (rowsAfected>0) {
					flagExecuteRequest = true;
				}
				break;
			case "SelectCmbEnt":
				mapRow = SelectCmbEnt();
				rowsAfected = 0;
				flagExecuteRequest = true;
				break;
		}
	}
	
	
}
