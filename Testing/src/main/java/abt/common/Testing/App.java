package abt.common.Testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import common.dataAccess.HBaseDB;
import common.dataAccess.SolRDB;
import common.rutinas.Rutinas;

/**
 * Hello world!
 *
 */
public class App {
	static Rutinas mylib = new Rutinas();
	static HBaseDB hbConn = new HBaseDB();
	static SolRDB srConn = new SolRDB();
	
    public static void main( String[] args ) throws Exception
    {
    	
    	/**
    	 * Configuracion de HBASE
    	 */
    	
    	mylib.console(2,"Mensaje");
    	
    	hbConn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecoprod01","grabdata");
    	
    	List<String> lst = new ArrayList<>();
    	
    	lst.add("+2+0+20160101_000006_00011008887674_78054792_TTR42-1451617206.2979113");

    	Map<String, Map<String, String>> rows = new HashMap<>();
    	List<String> keys = new ArrayList<>();
    	
    	rows = hbConn.getRows(lst);
    	keys = hbConn.getKeys(2);
    	
    	for (Entry<String, Map<String, String>> entry : rows.entrySet()) {
    		mylib.console("key: "+entry.getKey());
    	}
    	
    	for (String key : keys) {
    		mylib.console("key: "+key);
    	}
    	
    	String[] family = {"f0","f1"};
    	
    	rows = hbConn.scan(family, 1);
    	
    	for (Entry<String, Map<String, String>> entry : rows.entrySet()) {
    		mylib.console("key: "+entry.getKey());
    		for (Entry<String, String> subEntry : entry.getValue().entrySet()) {
    			mylib.console("cq: "+subEntry.getKey()+ " value: "+subEntry.getValue());
    		}
    	}
    	
    	/**
    	 * Configuración de solR
    	 */
    	srConn.setConfig("/usr/local/hadoop/conf/hadoop.properties", "ecoprod01");
    	srConn.open("collgrabdata");
    	
    	if (srConn.isConnected()) {
    	
	    	//Filtros
	    	Map<String, String> filtros = new HashMap<>();
	    	filtros.put("q", "*:*");
	    	filtros.put("rows", "3");
	    	filtros.put("key", "id");
	    	
	    	//Respuesta
	    	Map<String, String> respuesta = new HashMap<>();
	    	respuesta = srConn.getRows(filtros);
	    	
	    	for (Entry<String, String> entry : respuesta.entrySet()) {
	    		mylib.console("solr-key: "+ entry.getKey()+ " value: "+entry.getValue());
	    	}
	    	
	    	List<String> lstResp = new ArrayList<>();
	    	lstResp = srConn.getIds(filtros);
	    	
	    	for (String key : lstResp) {
	    		mylib.console("key2: "+key);
	    	}
	    	
	    	srConn.close();
	    	
    	}
    	
    	mylib.console("Fin");
        
    }
}
