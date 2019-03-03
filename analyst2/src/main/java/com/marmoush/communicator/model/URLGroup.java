package com.marmoush.communicator.model;

import java.util.Collection;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

public class URLGroup extends Group {

	public static final RelationshipType referenceNodeRelType = ReferenceNodeRels.HAS_URLS_PARENT;
	public static final RelationshipType parentNodeRelType = ParentNodeRels.HAS_URLS;
	public static final String indexName = "urlindex";

	public static Index<Node> createIndex(GraphDatabaseService graphdb) {
		return graphdb.index().forNodes(indexName);
	}

	public static Node createParentNode(GraphDatabaseService graphDb) {
		Node parent = graphDb.createNode();
		graphDb.getReferenceNode().createRelationshipTo(parent,
				parentNodeRelType);
		return parent;
	}

	public static URLGroup insertURL(GraphDatabaseService graphdb,
			Node parentNode, Index<Node> indexNode, Collection<String> col) {
		// Create Domain Nodes
		URLGroup dg = new URLGroup(graphdb, indexNode, parentNode);
		dg.setNodes(dg.createNodes(col));
		dg.connectParentToNodes();
		return dg;
	}

	public URLGroup() {

	}

	public URLGroup(GraphDatabaseService graphdb, Index<Node> indexNode,
			Node parentNode) {
		super(graphdb, indexNode, parentNode);
		setReferenceNodeRelType(referenceNodeRelType);
		setParentNodeRelType(parentNodeRelType);
		setIndexName(indexName);
	}

}
