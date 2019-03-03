package com.marmoush.communicator.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.impl.util.FileUtils;

public class NeoUtils {
	public static final String DB_PATH = "db/db1";
	public static final String URL_KEY = "url";
	public static final String EMAIL_KEY = "email";
	public static final String EMAILS_INDEX = "emailsindex";
	public static final String MAILSERVER_INDEX = "mailserversindex";

	public static void clearDb(String DB_PATH) {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Relationship> connect(List<Node> nodeList, Node node,
			RelationshipType relType) {
		List<Relationship> rels = new ArrayList<Relationship>();
		for (Node otherNode : nodeList) {
			rels.add(otherNode.createRelationshipTo(node, relType));
		}
		return rels;
	}

	public static List<Relationship> connect(Node node, List<Node> nodeList,
			RelationshipType relType) {
		List<Relationship> rels = new ArrayList<Relationship>();
		for (Node otherNode : nodeList) {
			rels.add(node.createRelationshipTo(otherNode, relType));
		}
		return rels;
	}

	public static void connectEqualValues(Collection<Node> srcNodes,
			Collection<Node> destNodes, String matchingKey,
			RelationshipType relType) {
		for (Node src : srcNodes) {
			String pval = (String) src.getProperty(matchingKey);
			for (Node dest : destNodes) {
				String cval = (String) dest.getProperty(matchingKey);
				if (pval.equals(cval)) {
					src.createRelationshipTo(dest, relType);
				}
			}
		}
	}

	public static void connectSimilarValues(Collection<Node> srcNodes,
			Collection<Node> destNodes, String matchingKey,
			RelationshipType relType) {
		for (Node src : srcNodes) {
			String srcVal = (String) src.getProperty(matchingKey);
			for (Node dest : destNodes) {
				String destVal = (String) dest.getProperty(matchingKey);
				if (srcVal.contains(destVal) || destVal.contains(srcVal)) {
					src.createRelationshipTo(dest, relType);
				}
			}
		}
	}

	public static GraphDatabaseService connectToGDB(String DB_PATH) {
		return new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
	}

	public static List<Node> createNode(String key, Collection<String> values,
			GraphDatabaseService graphdb, Index<Node> index) {
		List<Node> nodes = new ArrayList<Node>(values.size());
		for (String str : values) {
			nodes.add(createNode(key, str, graphdb, index));
		}
		return nodes;
	}

	public static Node createNode(String key, String value,
			GraphDatabaseService graphdb, Index<Node> index) {
		Node node = graphdb.createNode();
		node.setProperty(key, value);
		index.add(node, key, value);
		return node;
	}

	public static void deleteNode(Node node) {
		for (Relationship rel : node.getRelationships()) {
			rel.delete();
		}
		node.delete();
	}

	public static void deleteNode(String key, String value, Index<Node> index) {
		IndexHits<Node> hits = index.get(key, value);
		for (Node node : hits) {
			deleteNode(node);
		}
	}

	public static List<Node> findList(String key, String value,
			Index<Node> index) {
		List<Node> list = new ArrayList<Node>();
		IndexHits<Node> hits = index.get(key, value);
		for (Node node : hits) {
			list.add(node);
		}
		return list;
	}

	public static List<Node> findChildren(Node parent) {
		List<Node> list = new ArrayList<Node>();
		for (Relationship rel : parent.getRelationships()) {
			list.add(rel.getOtherNode(parent));
		}
		return list;
	}

	public static Node findOne(String key, String value, Index<Node> index) {
		IndexHits<Node> hits = index.get(key, value);
		Node node = null;
		if (hits.hasNext()) {
			node = hits.next();
			hits.close();
		}
		return node;
	}

	public static Index<Node> getIndex(GraphDatabaseService graphdb,
			String indexName) {
		return graphdb.index().forNodes(indexName);
	}

	public static List<String> nodeToString(Collection<Node> nodes, String key) {
		List<String> list = new ArrayList<String>();
		String str = null;
		for (Node node : nodes) {
			str = (String) node.getProperty(key);
			list.add(str);
		}
		return list;
	}

	public static List<URL> nodeToURL(Collection<Node> nodes, String URL_KEY)
			throws MalformedURLException {
		List<URL> list = new ArrayList<URL>();
		String str = null;
		for (Node node : nodes) {
			str = (String) node.getProperty(URL_KEY);
			list.add(new URL(str));
		}
		return null;
	}

	/**
	 * Registers a shutdown hook for the Neo4j instance so that it shuts down
	 * nicely when the VM exits (even if you "Ctrl-C" the running example before
	 * it's completed)
	 */
	public static void registerShutdownHook(final GraphDatabaseService graphdb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphdb.shutdown();
			}
		});
	}

}
