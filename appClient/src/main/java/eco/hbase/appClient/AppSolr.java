package eco.hbase.appClient;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.SolRDB;

public class AppSolr {
	static Rutinas mylib = new Rutinas();

	public static void main(String[] args) {
		SolRDB solrConn = new SolRDB();
		
        String filePropertiesPath = "/usr/local/hadoop/conf/hadoop.properties";
        String HBProperty = "ecoprod01";
        String collection = "collgrabdata";

        try {
        	solrConn.setConfig(filePropertiesPath, HBProperty);
        	solrConn.open(collection);
        	
        	if (solrConn.isConnected()) {
        		System.out.println("Connected...");
        		
        		Map<String, String> filters = new HashMap<>();
        		
        		filters.put("q", "'claro hogar' AND 'carlos'");
        		filters.put("rows", "10");
        		filters.put("fl", "id");
        		filters.put("key", "id");
        		
        		Map<String, String> mapResponse = new HashMap<>(solrConn.getMapQuery(filters));
        		
        		for (Map.Entry<String, String> entry : mapResponse.entrySet()) {
        			System.out.println(entry.getKey());
        		}
        		
        		System.out.println(mylib.serializeObjectToJSon(mapResponse, true));
        		
        		JSONObject jo = new JSONObject(solrConn.getMapQuery(filters));
        		
        		System.out.println(jo.toString());
        		
        	}
        	solrConn.close();
        } catch (Exception e) {
        	System.out.println("Error: "+e.getMessage());
        }
        

	}

}
