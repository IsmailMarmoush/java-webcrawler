package com.marmoush.communicator.model;

import org.neo4j.graphdb.RelationshipType;

public enum ReferenceNodeRels implements RelationshipType {
	HAS_DOMAINS_PARENT, HAS_EMAILS_PARENT, HAS_MAILSERVERS_PARENT, HAS_URLS_PARENT

}
