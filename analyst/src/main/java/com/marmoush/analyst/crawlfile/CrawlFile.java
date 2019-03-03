package com.marmoush.analyst.crawlfile;

import java.net.MalformedURLException;
import java.util.TreeSet;

import com.marmoush.analyst.Utils.FilterUtils;
import com.marmoush.analyst.Utils.StringUtils;

public class CrawlFile {
	private TreeSet<String> lines = new TreeSet<String>();
	private TreeSet<String> emails = new TreeSet<String>();
	private TreeSet<String> links = new TreeSet<String>();
	private TreeSet<String> domains = new TreeSet<String>();

	public CrawlFile() {

	}

	public TreeSet<String> getDomains() {
		return domains;
	}

	public TreeSet<String> getEmails() {
		return emails;
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
		lines = FilterUtils.getDoNotContainAny(lines, FilterUtils.PERSONAL_EMAIL_KEYWORDS);
		lines = StringUtils.toLower(lines);
		emails = FilterUtils.filter(lines, FilterUtils.EMAIL_Regex);
		links = FilterUtils.filter(lines, FilterUtils.LINK_REGEX);

		domains = StringUtils.emailToDomainsTree(emails);
		domains.addAll(StringUtils.linkToDomainTree(links));
	}

	public void run(String filePath, String dbpath) throws MalformedURLException {
		this.readAndFilter(filePath);
		// Database connection
		// graphdb = NeoUtils.connectToGDB(dbpath);
		// NeoUtils.registerShutdownHook(graphdb);
		// Transaction tx = graphdb.beginTx();
		// try {
		// Index<Node> indexNode = EmailsGroup.createIndex(graphdb);
		// Node parentNode = EmailsGroup.createParentNode(graphdb);
		// this.emailsGroup = EmailsGroup.insertEmails(graphdb, parentNode,
		// indexNode, getEmails());
		//
		// Index<Node> domainIndexNode = DomainsGroup.createIndex(graphdb);
		// Node domainParentNode = DomainsGroup.createParentNode(graphdb);
		// this.domainsGroup = DomainsGroup.insertDomains(graphdb,
		// domainParentNode, domainIndexNode, getDomains());
		//
		// // Connect domains to emails
		// NeoUtils.connectSimilarValues(domainsGroup.getNodes(),
		// emailsGroup.getNodes(),
		// Group.NODE_KEY, ParentNodeRels.HAS_EMAILS);
		// tx.success();
		// } finally {
		// tx.finish();
		// }
	}
}
