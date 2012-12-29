package com.ramblerag.db.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.ramblerag.domain.DomainConstants;
import com.ramblerag.domain.Lnk;
import com.ramblerag.domain.Nod;

@RunWith(PowerMockRunner.class)
public class DbWrapperTest {
	
	private DbWrapper target;
	private GraphDatabaseFactory graphDatabaseFactory;
	private GraphDatabaseService graphDb;
	private Index<Node> nodeIndex;

	@Before
	public void setUp() throws Exception {
		target = new DbWrapper();
		
		graphDatabaseFactory = PowerMock.createNiceMock(GraphDatabaseFactory.class);
		graphDb = PowerMock.createNiceMock(GraphDatabaseService.class);
		nodeIndex = PowerMock.createNiceMock(Index.class);
		
		target.setGraphDatabaseFactory(graphDatabaseFactory);
		target.setGraphDb(graphDb);
		target.setNodeIndex(nodeIndex);
	}

	@Test
	public void testRemoveAll() {
		PowerMock.replayAll();
		
	}

	@Test
	public void testStartDb() {
		PowerMock.replayAll();
		
	}

	@Test
	public void testInitRefs() {
		PowerMock.replayAll();
		
	}

	@Test
	public void testShutdownDb() {
		PowerMock.replayAll();
		target.shutdownDb();
	}

	@Test
	public void testInsert() {
		PowerMock.replayAll();
		
	}

	@Test
	public void testGetters() {
		PowerMock.replayAll();

		assertNotNull(target.getGraphDatabaseFactory());
		assertNotNull(target.getGraphDb());
		assertNotNull(target.getNodeIndex());
	}


}
