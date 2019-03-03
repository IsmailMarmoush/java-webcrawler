package com.marmoush.communicator;

import java.net.MalformedURLException;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import com.marmoush.communicator.crawlfile.CrawlFile;
import com.marmoush.communicator.crawlfile.FilePath;
import com.marmoush.communicator.model.DomainsGroup;
import com.marmoush.communicator.model.MailServer;
import com.marmoush.communicator.model.MailServersGroup;
import com.marmoush.communicator.model.NeoUtils;

public class App {
	public static void main(String[] args) {
		CrawlFile cf = new CrawlFile();
		try {
			NeoUtils.clearDb(FilePath.DB);
			cf.run(FilePath.RAWDATA_TEST, FilePath.DB);
			System.out.println("Emails And Domains inserted");
			// Start crawling for mail servers
			List<Node> domainList = cf.getDomainsGroup().getNodes();
			Index<Node> msIndex = MailServersGroup.createIndex(cf.getGraphdb());
			Transaction tx = cf.getGraphdb().beginTx();
			try {
				// Add mail servers nodes for each domain
				for (Node domain : domainList) {
					List<MailServer> msList = MailServer
							.getMailServers(domain);
					DomainsGroup.insertMailServers(msList, cf.getGraphdb(),
							domain, msIndex);
				}
				System.out.println("MailServers inserted for each domain");

				// add head request response

				tx.success();
			} finally {
				tx.finish();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// Insert all domains found in a file #done
		// insert all emails #done
		// start checking all and write response property
		// delete bad responses
	}
}
