package com.marmoush.communicator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import com.marmoush.communicator.validation.HeadRequestResponse;

public class DomainsGroup extends Group {

	public static final RelationshipType referenceNodeRelType = ReferenceNodeRels.HAS_DOMAINS_PARENT;
	public static final RelationshipType parentNodeRelType = ParentNodeRels.HAS_DOMAINS;
	public static final String indexName = "domainsindex";
	public static final String responseCodeKey = "respcodekey";
	public static final String responseMessageKey = "respmsgkey";

	public static Index<Node> createIndex(GraphDatabaseService gdb) {
		return gdb.index().forNodes(indexName);
	}

	public static Node createParentNode(GraphDatabaseService gdb) {
		Node parent = gdb.createNode();
		gdb.getReferenceNode().createRelationshipTo(parent, parentNodeRelType);
		return parent;
	}

	public static void deleteMailServers() {
		
	}

	public static List<HeadRequestResponse> getHeadReqResponse(DomainsGroup dg,
			int timeout) {
		int s = dg.getNodes().size();
		List<HeadRequestResponse> hrList = new ArrayList<HeadRequestResponse>(s);
		List<Node> domains = dg.getNodes();
		for (Node domain : domains) {
			HeadRequestResponse hrr = new HeadRequestResponse(timeout,
					(String) domain.getProperty(NODE_KEY));
			hrList.add(hrr);
		}
		return hrList;
	}

	public static List<Node> getMailServers(Node domain) {
		Traverser masTrav = getMailServersTraverser(domain);
		List<Node> mailserverList = new ArrayList<Node>();
		for (Node mailserver : masTrav.nodes()) {
			mailserverList.add(mailserver);
		}
		return mailserverList;
	}

	public static DomainsGroup insertDomains(GraphDatabaseService graphdb,
			Node parentNode, Index<Node> indexNode, Collection<String> col) {
		// Create Domain Nodes
		DomainsGroup dg = new DomainsGroup(graphdb, indexNode, parentNode);
		dg.setNodes(dg.createNodes(col));
		dg.connectParentToNodes();
		return dg;
	}

	public static MailServersGroup insertMailServers(
			Collection<MailServer> col, GraphDatabaseService graphdb,
			Node parentNode, Index<Node> indexNode) {
		MailServersGroup dg = new MailServersGroup(graphdb, indexNode,
				parentNode);
		
		dg.setNodes(MailServersGroup.createNode(col, graphdb, indexNode));
		dg.connectParentToNodes();
		return dg;
	}

	private static Traverser getMailServersTraverser(final Node domain) {
		TraversalDescription td = Traversal
				.description()
				.breadthFirst()
				.relationships(ParentNodeRels.HAS_MAILSERVERS,
						Direction.OUTGOING)
				.evaluator(Evaluators.excludeStartPosition());
		return td.traverse(domain);
	}

	public DomainsGroup() {

	}

	public DomainsGroup(GraphDatabaseService graphdb, Index<Node> indexNode,
			Node parentNode) {
		super(graphdb, indexNode, parentNode);
		setReferenceNodeRelType(referenceNodeRelType);
		setParentNodeRelType(parentNodeRelType);
		setIndexName(indexName);
	}
}
