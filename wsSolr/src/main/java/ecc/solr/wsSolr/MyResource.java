package ecc.solr.wsSolr;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws IOException 
     * @throws SolrServerException 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt()  {
    	try {
			CloudSolrServer server = new CloudSolrServer("cloudera1:2181,cloudera3:2181,cloudera2:2181/solr");
			
			server.setDefaultCollection("grabaciones_nuevo");
			SolrQuery solrQuery = new SolrQuery("*.*");
			
			ModifiableSolrParams parameters = new ModifiableSolrParams();
		      parameters.set("q", "codigoservicio:CAAB0750");
		      //parameters.set("fq", "connid,agente");
		      parameters.set("fl", "id, connid, agente");
		      parameters.set("start", 0);
		      parameters.set("rows", 100);
			
			QueryResponse response1 = server.query(parameters);
			
			
			SolrDocumentList dList = response1.getResults();
			for (int i = 0; i < dList.getNumFound(); i++) 
			{
			  for (Map.Entry mE : dList.get(i).entrySet()) 
			    {
			      System.out.println(mE.getKey() + ":" + mE.getValue());
			  }
			}
    	} catch (SolrServerException| IOException e) {
    		System.out.println("Error ("+e.getMessage());
    	} catch (IndexOutOfBoundsException ex) {
    		System.out.println("no error");
    	}
        return "Got it!";
    }
}
