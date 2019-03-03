package com.marmoush.communicator.crawlfile;

import java.net.MalformedURLException;
import java.util.TreeSet;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import com.marmoush.communicator.Utils.FilterUtils;
import com.marmoush.communicator.Utils.StringUtils;
import com.marmoush.communicator.model.DomainsGroup;
import com.marmoush.communicator.model.EmailsGroup;
import com.marmoush.communicator.model.Group;
import com.marmoush.communicator.model.NeoUtils;
import com.marmoush.communicator.model.ParentNodeRels;

public class CrawlFile {
	private TreeSet<String> lines = new TreeSet<String>();
	private TreeSet<String> emails = new TreeSet<String>();
	private TreeSet<String> links = new TreeSet<String>();
	private TreeSet<String> domains = new TreeSet<String>();

	private GraphDatabaseService graphdb;

	private DomainsGroup domainsGroup;

	private EmailsGroup emailsGroup;

	public CrawlFile() {

	}

	public TreeSet<String> getDomains() {
		return domains;
	}

	public DomainsGroup getDomainsGroup() {
		return domainsGroup;
	}

	public TreeSet<String> getEmails() {
		return emails;
	}

	public EmailsGroup getEmailsGroup() {
		return emailsGroup;
	}

	public GraphDatabaseService getGraphdb() {
		return graphdb;
	}
	public TreeSet<String> getLines() {
		return lines;
	}

	public TreeSet<String> getLinks() {
		return links;
	}

	public void readAndFilter(String filePath) throws MalformedURLException {
		lines = FileUtils.readFileToTreeSet(filePath);
		lines = FilterUtils.removeNoise(lines);
		lines = FilterUtils.getDoNotContainAny(lines,
				FilterUtils.PERSONAL_EMAIL_KEYWORDS);
		lines = StringUtils.toLower(lines);
		emails = FilterUtils.filter(lines, FilterUtils.EMAIL_Regex);
		links = FilterUtils.filter(lines, FilterUtils.LINK_REGEX);

		domains = StringUtils.emailToDomainsTree(emails);
		domains.addAll(StringUtils.linkToDomainTree(links));
	}

	public void run(String filePath, String dbpath)
			throws MalformedURLException {
		this.readAndFilter(filePath);
		// Database connection
		graphdb = NeoUtils.connectToGDB(dbpath);
		NeoUtils.registerShutdownHook(graphdb);
		Transaction tx = graphdb.beginTx();
		try {
			Index<Node> indexNode = EmailsGroup.createIndex(graphdb);
			Node parentNode = EmailsGroup.createParentNode(graphdb);
			this.emailsGroup = EmailsGroup.insertEmails(graphdb, parentNode,
					indexNode, getEmails());

			Index<Node> domainIndexNode = DomainsGroup.createIndex(graphdb);
			Node domainParentNode = DomainsGroup.createParentNode(graphdb);
			this.domainsGroup = DomainsGroup.insertDomains(graphdb,
					domainParentNode, domainIndexNode, getDomains());

			// Connect domains to emails
			NeoUtils.connectSimilarValues(domainsGroup.getNodes(), emailsGroup.getNodes(),
					Group.NODE_KEY, ParentNodeRels.HAS_EMAILS);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public void setDomainsGroup(DomainsGroup dominsGroup) {
		this.domainsGroup = dominsGroup;
	}

	public void setEmailsGroup(EmailsGroup emailsGroup) {
		this.emailsGroup = emailsGroup;
	}

	public void setGraphdb(GraphDatabaseService graphdb) {
		this.graphdb = graphdb;
	}
}
