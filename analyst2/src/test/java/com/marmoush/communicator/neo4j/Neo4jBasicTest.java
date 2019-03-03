package com.marmoush.communicator.neo4j;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * An example of unit testing with Neo4j.
 */
public class Neo4jBasicTest {
	protected GraphDatabaseService graphDb;

	/**
	 * Shutdown the database.
	 */
	@After
	public void destroyTestDatabase() {
		graphDb.shutdown();
	}

	/**
	 * Create temporary database for each unit test.
	 */
	@Before
	public void prepareTestDatabase() {
		graphDb = new TestGraphDatabaseFactory()
				.newEmbeddedDatabase("db/testdb");
		// .newImpermanentDatabaseBuilder().newGraphDatabase();
	}

	@Test
	public void shouldCreateNode() {
		Transaction tx = graphDb.beginTx();

		Node n = null;
		try {
			n = graphDb.createNode();
			n.setProperty("name", "Nancy");
			tx.success();
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.finish();
		}

		// The node should have an id greater than 0, which is the id of the
		// reference node.
		assertThat(n.getId(), is(greaterThan(0l)));

		// Retrieve a node by using the id of the created node. The id's and
		// property should match.
		Node foundNode = graphDb.getNodeById(n.getId());
		assertThat(foundNode.getId(), is(n.getId()));
		assertThat((String) foundNode.getProperty("name"), is("Nancy"));
	}

	@Test
	public void startWithConfiguration() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("neostore.nodestore.db.mapped_memory", "10M");
		config.put("string_block_size", "60");
		config.put("array_block_size", "300");
		GraphDatabaseService db = new ImpermanentGraphDatabase(config);
		db.shutdown();
	}
}