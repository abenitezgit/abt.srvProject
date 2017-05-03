package abt.srvProject.solrClient;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class App3 {

	public static void main(String[] args) throws SolrServerException, IOException {
		// TODO Auto-generated method stub


		@SuppressWarnings("deprecation")
		CloudSolrServer server = new CloudSolrServer("hwk23:2181/solr");
		server.setDefaultCollection("coll-grab");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField( "id", "r50");
		doc.addField( "firstname_s", "A lovely summer holiday");
		server.add(doc);
		server.commit();
	}

}
