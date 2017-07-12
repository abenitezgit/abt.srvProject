package abt.srvProject.wsSrvProject;

import java.sql.ResultSet;

import org.json.JSONObject;

public class Procedures {
	
	
	public String getSummary() {
		mysqlDB myConn = new mysqlDB();
		int totalProcess = 0;
		try {
			myConn.setDbHost("localhost");
			myConn.setDbName("srvConf");
			myConn.setDbPort("3306");
			myConn.setDbUser("process");
			myConn.setDbPass("usrProc-01");
			myConn.setTimeOut(10);
			
			myConn.open();
			
			if (myConn.isConnected()) {
				String vSql = "select count(*) from tma_exec_control where active=1";
				if (myConn.executeQuery(vSql)) {
					ResultSet rs = myConn.getQuery();
					if (rs.next()) {
						totalProcess = rs.getInt(1);
					}
				}
				myConn.close();
			}
			
			JSONObject jData = new JSONObject();
			jData.put("Total Process Activos", totalProcess);
			
			
			return okResponse(jData);
			
		} catch (Exception e) {
			return errorResponse(e.getMessage());
		}
	}
	
	public String okResponse(JSONObject jData) {
		JSONObject jHeader = new JSONObject();
		
		jHeader.put("status", 0);
		jHeader.put("mesg", "SUCCESS");
		jHeader.put("data", jData);
		
		return jHeader.toString();
	}
	
	public String errorResponse(String errMesg) {
		JSONObject jHeader = new JSONObject();
		JSONObject jData = new JSONObject();
		
		jData.put("errMesg", errMesg);
		jHeader.put("status", 99);
		jHeader.put("mesg", "ERROR");
		jHeader.put("data", jData);
		
		return jHeader.toString();
	}
	
}
