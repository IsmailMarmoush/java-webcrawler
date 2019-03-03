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
import org.neo4j.graphdb.index.Index;

import com.marmoush.communicator.model.NeoUtils;

public class EmbeddedNeo4jWithIndexing {
	private static enum RelTypes implements RelationshipType {
		USERS_REFERENCE, USER
	}

	private static final String DB_PATH = "db/db1";
	private static final String USERNAME_KEY = "username";
	private static GraphDatabaseService graphDb;
	private static Index<Node> nodeIndex;

	public static void main(final String[] args) {
		// startDb
		NeoUtils.clearDb(DB_PATH);
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		nodeIndex = graphDb.index().forNodes("nodes");
		NeoUtils.registerShutdownHook(graphDb);

		// addUsers
		Transaction tx = graphDb.beginTx();
		try {
			// Create users sub reference node
			Node usersReferenceNode = graphDb.createNode();
			graphDb.getReferenceNode().createRelationshipTo(usersReferenceNode,
					RelTypes.USERS_REFERENCE);
			// Create some users and index their names with the IndexService
			for (int id = 0; id < 100; id++) {
				Node userNode = createAndIndexUser(idToUserName(id));
				usersReferenceNode
						.createRelationshipTo(userNode, RelTypes.USER);
			}

			System.out.println("Users created");
			// Find a user through the search index
			// findUser
			// int idToFind = 45;
			// Node foundUser = nodeIndex
			// .get(USERNAME_KEY, idToUserName(idToFind)).getSingle();
			// System.out.println("The username of user " + idToFind + " is "
			// + foundUser.getProperty(USERNAME_KEY));
			tx.success();
		} finally {
			tx.finish();
		}

		// ExecutionEngine engine = new ExecutionEngine(graphDb);
		// ExecutionResult result = engine.execute("start n=node(*) return n");
		// System.out.println(result.dumpToString());

		System.out.println("Shutting down database ...");
		shutdown();
	}

	private static Node createAndIndexUser(final String username) {
		Node node = graphDb.createNode();
		node.setProperty(USERNAME_KEY, username);
		nodeIndex.add(node, USERNAME_KEY, username);
		return node;
	}

	// START SNIPPET: helperMethods
	private static String idToUserName(final int id) {
		return "user" + id + "@neo4j.org";
	}

	private static void shutdown() {
		graphDb.shutdown();
	}

	public void delete(Node usersReferenceNode) {
		// Delete the persons and remove them from the index
		for (Relationship relationship : usersReferenceNode.getRelationships(
				RelTypes.USER, Direction.OUTGOING)) {
			Node user = relationship.getEndNode();
			nodeIndex
					.remove(user, USERNAME_KEY, user.getProperty(USERNAME_KEY));
			user.delete();
			relationship.delete();
		}
		usersReferenceNode.getSingleRelationship(RelTypes.USERS_REFERENCE,
				Direction.INCOMING).delete();
		usersReferenceNode.delete();
	}
}