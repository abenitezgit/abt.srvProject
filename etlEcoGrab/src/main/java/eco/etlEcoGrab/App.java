package eco.etlEcoGrab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.dataAccess.HBaseDB;
import common.dataAccess.SolRDB;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
	        HBaseDB hbConn = new HBaseDB();
	        
	        hbConn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecoprod01", "grabdata");
	        
	        System.out.println("Leyendo keys a borrar");
	        List<String> keys = new ArrayList<>();
	        keys = hbConn.getKeys(1000);
	        
	        
	        System.out.println("Borrando de hbase");
	        hbConn.deleteKeys(keys);
	        
	        System.out.println("Fin");
	        
	        SolRDB solrConn = new SolRDB();
	        
	        solrConn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecoprod01");
	        solrConn.open("collgrabdata");
	        if (solrConn.isConnected()) {
	        	Map<String,String> filters = new HashMap<>();
	        	filters.put("q", "*:*");
	        	filters.put("fl", "id");
	        	filters.put("key", "id");
	        	filters.put("rows", "2000");
	        	
	        	keys = solrConn.getIds(filters);
	        	for (String key : keys) {
	        		System.out.println("key: "+key);
	        	}
	        	hbConn.deleteKeys(keys);
	        }
	        
	        System.out.println("Fin2");
	        solrConn.close();
	        
	        
	        
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
