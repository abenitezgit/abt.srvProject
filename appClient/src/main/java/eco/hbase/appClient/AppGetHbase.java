package eco.hbase.appClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;


import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.json.JSONObject;

import abt.srvProject.srvRutinas.Rutinas;

public class AppGetHbase {
	static Rutinas mylib = new Rutinas();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		SolrDocumentList idKeys = new SolrDocumentList();
		
    	try {
			//CloudSolrServer server = new CloudSolrServer("hwk23:2181,hwk24:2181,hwk25:2181");
    		
    		List<String> lstSolr = new ArrayList<>();
    		lstSolr.add("hwk23.e-contact.cl:2181/solr");
    		lstSolr.add("hwk24.e-contact.cl:2181/solr");
    		lstSolr.add("hwk25.e-contact.cl:2181/solr");
    		
			
			CloudSolrClient server = new CloudSolrClient(lstSolr.get(0));

			server.setDefaultCollection("collgrabdata");
			SolrPingResponse response = server.ping();
			System.out.println("status: "+response.getStatus());
			
			
			//System.exit(0);
			
			server.setDefaultCollection("collgrabdata");
			//SolrQuery solrQuery = new SolrQuery("*.*");
			
			ModifiableSolrParams parameters = buildSolrFilters();
			
			QueryResponse response1 = server.query(parameters);
			
			//System.out.println("Total keys: "+response1.getResults().size());
			idKeys = response1.getResults();
			
			server.close();

    	} catch (SolrServerException| IOException e) {
    		mylib.console(1,"Error en getSolrIds ("+e.getMessage());
    	} catch (IndexOutOfBoundsException ex) {
    		mylib.console(2,"Consulta limitada en respuesta.");
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			mylib.console(1,"Error en getSolrIds ("+e.getMessage());
		}
    	
    	mylib.console("Total ids: "+ idKeys.getNumFound());

    	int reads=0;
    	for (SolrDocument doc: idKeys) {
    		JSONObject jo = new JSONObject(doc);
			//String key = (String) doc.getFieldValue("ttext");
    		
    		List<String> lstArray = new ArrayList<>();
    		lstArray = (List<String>) doc.getFieldValue("ttext");
    		
    		
    		Object obj = doc.getFieldValues("ttext");
    		
    		System.out.println(obj.getClass().toString());
    		
    		List<String> lstArray2 = new ArrayList<>();
    		lstArray2 = (List<String>) obj;
    		
    		System.out.println("object: "+obj.toString());
    		System.out.println("array: "+lstArray2.get(0));
    		
    		
    		//System.out.println("> "+jo.toString());
    		
//    		for (int i=1; i<lst.size(); i++) {
//    			System.out.println("line "+i+": "+lst.get(i));
//    		}
//			System.out.println("  ");
//			System.out.println("  ");
    		reads++;
			
    	}
    	System.out.println("total reads: "+reads);

		
		
	}
	
	 static public ModifiableSolrParams buildSolrFilters() throws Exception {
	        ModifiableSolrParams parameters = new ModifiableSolrParams();
	        String q="buenas tardes";
	        parameters.set("q", q);
	        //parameters.set("fq", "[20161201 TO 20161231]");
	        parameters.set("fl", "ttext, id, ani");
	        parameters.set("start", 0);
	        parameters.set("rows", 100);
	        
	        //mylib.console("Filtro consulta q: "+q);
	        
	        return parameters;

	 }
	
}
