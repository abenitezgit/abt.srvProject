package abt.srvProject.solrClient;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

@SuppressWarnings("deprecation")
public class App2 {

	public static void main(String[] args) throws SolrServerException, IOException {
		// TODO Auto-generated method stub

		String zkHostString = "hwk23.e-contact.cl:2181,hwk21.e-contact.cl:2181,hwk22.e-contact.cl:2181";
		SolrClient solr = new CloudSolrClient(zkHostString);
		
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/spellCheckCompRH");
		query.set("q", "*:*");
		
		QueryResponse response = solr.query(query);
		
	}

}
