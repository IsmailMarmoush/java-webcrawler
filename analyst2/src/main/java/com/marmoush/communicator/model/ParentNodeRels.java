package com.marmoush.communicator.model;

import org.neo4j.graphdb.RelationshipType;

public enum ParentNodeRels implements RelationshipType {
	HAS_DOMAINS, HAS_EMAILS, HAS_MAILSERVERS, HAS_URLS
}
