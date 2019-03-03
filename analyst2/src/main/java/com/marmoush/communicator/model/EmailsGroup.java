package com.marmoush.communicator.model;

import java.util.Collection;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

public class EmailsGroup extends Group {
	public static final RelationshipType referenceNodeRelType = ReferenceNodeRels.HAS_EMAILS_PARENT;
	public static final RelationshipType parentNodeRelType = ParentNodeRels.HAS_EMAILS;
	public static final String indexName = "emailsindex";

	public static Index<Node> createIndex(GraphDatabaseService graphdb) {
		return graphdb.index().forNodes(indexName);
	}

	public static Node createParentNode(GraphDatabaseService graphDb) {
		Node parent = graphDb.createNode();
		graphDb.getReferenceNode().createRelationshipTo(parent,
				parentNodeRelType);
		return parent;
	}

	public static EmailsGroup insertEmails(GraphDatabaseService graphdb,
			Node parentNode, Index<Node> indexNode, Collection<String> col) {
		// create Email Nodes
		EmailsGroup dg = new EmailsGroup(graphdb, indexNode, parentNode);
		dg.setNodes(dg.createNodes(col));
		dg.connectParentToNodes();
		return dg;
	}

	public EmailsGroup() {

	}

	public EmailsGroup(GraphDatabaseService graphdb, Index<Node> indexNode,
			Node parentNode) {
		super(graphdb, indexNode, parentNode);
		setReferenceNodeRelType(referenceNodeRelType);
		setParentNodeRelType(parentNodeRelType);
		setIndexName(indexName);
	}
}
