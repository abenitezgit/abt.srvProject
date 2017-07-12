package eco.hbase.appClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import abt.srvProject.srvRutinas.Rutinas;
import eco.hbase.dataAccess.HBaseDB;
import eco.hbase.dataAccess.SolRDB;

public class AppSolr {
	static Rutinas mylib = new Rutinas();

	public static void main(String[] args) {
		SolRDB solrConn = new SolRDB();
		HBaseDB hbConn = new HBaseDB();
		
        String filePropertiesPath = "/usr/local/hadoop/conf/hadoop.properties";
        String HBProperty = "ecoprod01";
        String collection = "collgrabdata";

        try {
        	solrConn.setConfig(filePropertiesPath, HBProperty);
        	solrConn.open(collection);
        	
        	if (solrConn.isConnected()) {
        		System.out.println("Connected...");
        		
        		Map<String, String> filters = new HashMap<>();
        		
        		filters.put("q", "*:*");
        		filters.put("fq", "fecini:[20161201 TO 20170101] AND org:2 AND suborg:1");
        		filters.put("rows", "2876");
//        		filters.put("fl", "id");
        		filters.put("key", "id");
//        		filters.put("facet", "true");
//        		filters.put("facet.field", "suborg");
        		
        		Map<String, String> mapResponse = new HashMap<>(solrConn.getMapQuery(filters));
        		
        		hbConn.setConfig(filePropertiesPath, HBProperty, "grabdata");
        		List<String> key = new ArrayList<>();
        		int deleted=0;
        		
        		for (Map.Entry<String, String> entry : mapResponse.entrySet()) {
        			//System.out.println(entry.getKey());
        			key = new ArrayList<>();
        			key.add(entry.getKey());
        			hbConn.deleteKeys(key);
        			deleted++;
        		}
        		
        		//System.out.println(mylib.serializeObjectToJSon(mapResponse, true));
        		System.out.println("Se han borrado: "+deleted+" filas");
        		
        		//JSONObject jo = new JSONObject(solrConn.getMapQuery(filters));
        		
        		//System.out.println(jo.toString());
        		
        	}
        	solrConn.close();
        } catch (Exception e) {
        	System.out.println("Error: "+e.getMessage());
        }
        

	}

}
