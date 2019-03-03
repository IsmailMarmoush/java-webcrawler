package com.marmoush.communicator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;


public class MailServersGroup extends Group {
	public static final RelationshipType referenceNodeRelType = ReferenceNodeRels.HAS_MAILSERVERS_PARENT;
	public static final RelationshipType parentNodeRelType = ParentNodeRels.HAS_MAILSERVERS;
	public static final String indexName = "mailserversindex";

	public static Index<Node> createIndex(GraphDatabaseService graphdb) {
		return graphdb.index().forNodes(indexName);
	}

	public static List<Node> createNode(Collection<MailServer> msList,
			GraphDatabaseService graphdb, Index<Node> indexNode) {
		List<Node> nodes = new ArrayList<Node>();
		for (MailServer ms : msList) {
			Node node = NeoUtils.createNode(Group.NODE_KEY, ms.getUrl(),
					graphdb, indexNode);
			node.setProperty(MailServer.PRIORITY_KEY, ms.getPriority());
			nodes.add(node);
		}
		return nodes;
	}

	public static Node createParentNode(GraphDatabaseService graphDb) {
		Node parent = graphDb.createNode();
		graphDb.getReferenceNode().createRelationshipTo(parent,
				parentNodeRelType);
		return parent;
	}

	public MailServersGroup() {

	}

	public MailServersGroup(GraphDatabaseService graphdb,
			Index<Node> indexNode, Node parentNode) {
		super(graphdb, indexNode, parentNode);
		setReferenceNodeRelType(referenceNodeRelType);
		setParentNodeRelType(parentNodeRelType);
		setIndexName(indexName);
	}
}
