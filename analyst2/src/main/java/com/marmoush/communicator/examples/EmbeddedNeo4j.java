package com.marmoush.communicator.examples;

/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.marmoush.communicator.model.NeoUtils;

public class EmbeddedNeo4j {
	private static enum RelTypes implements RelationshipType {
		KNOWS
	}

	private static final String DB_PATH = "db/db1";

	public static void main(final String[] args) {
		EmbeddedNeo4j hello = new EmbeddedNeo4j();
		hello.createDb();
		// hello.removeData();
		hello.shutDown();
	}

	private String greeting;
	private GraphDatabaseService graphDb;
	private Node firstNode;
	private Node secondNode;
	private Relationship relationship;

	void createDb() {
		NeoUtils.clearDb(DB_PATH);
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		NeoUtils.registerShutdownHook(graphDb);
		Transaction tx = graphDb.beginTx();
		try {
			// Mutating operations go here
			firstNode = graphDb.createNode();
			firstNode.setProperty("message", "Hello, ");
			secondNode = graphDb.createNode();
			secondNode.setProperty("message", "World!");

			relationship = firstNode.createRelationshipTo(secondNode,
					RelTypes.KNOWS);
			relationship.setProperty("message", "brave Neo4j ");
			System.out.print(firstNode.getProperty("message"));
			System.out.print(relationship.getProperty("message"));
			System.out.print(secondNode.getProperty("message"));

			greeting = ((String) firstNode.getProperty("message"))
					+ ((String) relationship.getProperty("message"))
					+ ((String) secondNode.getProperty("message"));

			tx.success();
		} finally {
			tx.finish();
		}
	}

	void removeData() {
		Transaction tx = graphDb.beginTx();
		try {
			// let's remove the data
			firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING)
					.delete();
			firstNode.delete();
			secondNode.delete();

			tx.success();
		} finally {
			tx.finish();
		}
	}

	void shutDown() {
		System.out.println();
		System.out.println("Shutting down database ...");
		graphDb.shutdown();
	}
}