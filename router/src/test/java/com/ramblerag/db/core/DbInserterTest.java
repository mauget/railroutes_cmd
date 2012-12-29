package com.ramblerag.db.core;

import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class DbInserterTest {
	
	private DbInserter target;
	private BatchInserter batchInserter;
	private BatchInserterIndex nodeIndex;
	private BatchInserterIndexProvider indexProvider;

	@Before
	public void setUp() throws Exception {
		target = new DbInserter();
		batchInserter = PowerMock.createNiceMock(BatchInserter.class);
		target.setInserter(batchInserter);
		nodeIndex = PowerMock.createNiceMock(BatchInserterIndex.class);
		target.setNodeIndex(nodeIndex);
		indexProvider = PowerMock.createNiceMock(BatchInserterIndexProvider.class);
		target.setIndexProvider(indexProvider);
	}
	
	@Test
	public void testCreateAndIndexNode() throws Exception {
		Nod node = getFakeNod();
		PowerMock.replayAll();
		
		Whitebox.invokeMethod(target, "createAndIndexNode", node);
	}
	
	private Nod getFakeNod() {

		Nod n = new Nod();
		n.setDescription("description");
		n.setFeatureId("featureId");
		n.setLatitude("48.123456");
		n.setLongitude("-117.123456");
		n.setModDate("0");
		n.setNodeId("1");
		n.setRecType("N");
		n.setRevision("1");
		n.setStFIPS("53");
		n.setVersion("01");
		
		return n;
	}
	
	@Test
	public void testCreateLink() throws Exception {
		
		Lnk link = getFakeLnk();
		
		PowerMock.replayAll();
		
		//createLink(final Lnk domainLink) 
		Whitebox.invokeMethod(target, "createLink", link);
	}
	
	@SuppressWarnings("unchecked")
	private Lnk getFakeLnk() {
		Lnk l = new Lnk();
		l.setaNode("1");
		l.setbNode("2");
		l.setDescription("description");
		l.setFeatureId("featureId");
		l.setLinkId("1");
		l.setModDate("0");
		l.setRecType("L");
		l.setRevision("1");
		l.setStFIPS1("53");
		l.setStFIPS2("51");
		l.setVersion("1");
		
		IndexHits<Long> hits = PowerMock.createNiceMock(IndexHits.class);

		long keyValueA = 1;
		long keyValueB = 2;
		expect(nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueA)).andReturn(hits);
		expect(nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueB)).andReturn(hits);

		long nodeAId = 1;
		long nodeBId = 2;

		expect(hits.getSingle()).andReturn(nodeAId).times(1);
		expect(hits.getSingle()).andReturn(nodeBId).times(1);
		return l;
	}

	@Test
	public void testInsertBulk()  throws Exception {
		PowerMock.replayAll();
		target.insertBulk(new String[0]);
	}

	@Test
	public void testInsert()  throws Exception {
		
		Lnk link = getFakeLnk();
		Nod node = getFakeNod();
		
		PowerMock.replayAll();

		target.insert(null);
		target.insert(node);
		target.insert(link);
	}

	@Test
	public void testStartDbInserter() throws Exception {
		PowerMock.replayAll();
		target.startDbInserter(new String[0]);
	}

	@Test
	public void testShutdownDbInserter() throws Exception {
		PowerMock.replayAll();
		target.shutdownDbInserter();
	}

	@Test
	public void testFlushToIndex() throws Exception {
		PowerMock.replayAll();
		
		target.flushToIndex();
	}

}
