package com.marmoush.communicator.model;

import java.util.Collection;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;

public abstract class Group {
	private Index<Node> indexNode;
	private List<Node> nodes;
	private Node parent;
	private List<Relationship> parentToNodesRelations;
	private GraphDatabaseService graphdb;
	private RelationshipType referenceNodeRelType;
	private RelationshipType parentNodeRelType;
	private String indexName;
	public static final String NODE_KEY = "name";

	public Group() {

	}

	public Group(GraphDatabaseService graphdb, Index<Node> indexNode,
			Node parentNode) {
		this.graphdb = graphdb;
		this.indexNode = indexNode;
		this.parent = parentNode;
	}

	public void setNodesProperty(String key, List<String> vals) {
		List<Node> nodes = getNodes();
		if (nodes.size() == vals.size()) {
			for (int j = 0; j < vals.size(); j++) {
				nodes.get(j).setProperty(key, vals.get(j));
			}
		}
	}

	public List<Relationship> connectParentToNodes() {
		return NeoUtils.connect(getParent(), getNodes(), parentNodeRelType);
	}

	public List<Relationship> connectParentToNodes(List<Node> nodes) {
		return NeoUtils.connect(getParent(), nodes, parentNodeRelType);
	}

	public List<Node> createNodes(Collection<String> values) {
		return NeoUtils.createNode(NODE_KEY, values, getGraphdb(),
				getIndexNode());
	}

	public GraphDatabaseService getGraphdb() {
		return graphdb;
	}

	public String getIndexName() {
		return indexName;
	}

	public Index<Node> getIndexNode() {
		return indexNode;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Node getParent() {
		return parent;
	}

	public RelationshipType getParentNodeRelType() {
		return parentNodeRelType;
	}

	public List<Relationship> getParentToNodesRelations() {
		return parentToNodesRelations;
	}

	public RelationshipType getReferenceNodeRelType() {
		return referenceNodeRelType;
	}

	public void setGraphdb(GraphDatabaseService graphdb) {
		this.graphdb = graphdb;
	}

	public void setIndexName(String groupIndex) {
		this.indexName = groupIndex;
	}

	public void setIndexNode(Index<Node> indexNode) {
		this.indexNode = indexNode;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void setParent(Node nodesParent) {
		this.parent = nodesParent;
	}

	public void setParentNodeRelType(RelationshipType parentNodeRelType) {
		this.parentNodeRelType = parentNodeRelType;
	}

	public void setParentToNodesRelations(
			List<Relationship> parentToNodesRelations) {
		this.parentToNodesRelations = parentToNodesRelations;
	}

	public void setReferenceNodeRelType(RelationshipType referenceNodeRelType) {
		this.referenceNodeRelType = referenceNodeRelType;
	}

}
