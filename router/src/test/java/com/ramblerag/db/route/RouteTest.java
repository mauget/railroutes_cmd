package com.ramblerag.db.route;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.Traversal;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ramblerag.db.core.DbWrapper;
import com.ramblerag.domain.DomainConstants;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GraphAlgoFactory.class, Traversal.class})
public class RouteTest {

	private Router target;

	@Before
	public void setUp() throws Exception {
		target = new Router();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRoute() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);

		DbWrapper dbWrapper = PowerMock.createNiceMock(DbWrapper.class);
		target.setDbWrapper(dbWrapper);

		GraphDatabaseService graphDb = PowerMock.createNiceMock(GraphDatabaseService.class);
		expect(target.getDbWrapper().startDb()).andReturn(graphDb);
		
		//Index<Node> nodeIndex = graphDb.index().forNodes(DomainConstants.INDEX_NAME);
		
		IndexManager indexManager = PowerMock.createNiceMock(IndexManager.class);
		expect(graphDb.index()).andReturn(indexManager).times(10);
		
		Index<Node> nodeIndex = PowerMock.createNiceMock(Index.class);
		expect(indexManager.forNodes(DomainConstants.INDEX_NAME)).andReturn(nodeIndex);
		
		IndexHits<Node> hits = PowerMock.createNiceMock(IndexHits.class);
		
		long keyValueA = 1;
		long keyValueB = 2;
		expect(nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueA)).andReturn(hits);
		expect(nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueB)).andReturn(hits);

		Node nodeA = PowerMock.createNiceMock(Node.class);
		Node nodeB = PowerMock.createNiceMock(Node.class);

		expect(hits.getSingle()).andReturn(nodeA).times(2);
		expect(hits.getSingle()).andReturn(nodeB).times(2);
		
		Transaction tx = PowerMock.createNiceMock(Transaction.class);
		expect(graphDb.beginTx()).andReturn(tx);
		
		//Expander relExpander = Traversal.expanderForTypes(DomainConstants.RelTypes.DOMAIN_LINK, Direction.BOTH);
		Expander relExpander = PowerMock.createNiceMock(Expander.class);
		PowerMock.mockStatic(Traversal.class);
		expect(Traversal.expanderForTypes(DomainConstants.RelTypes.DOMAIN_LINK, Direction.BOTH)).andReturn(relExpander);
		
		// PathFinder<WeightedPath> shortestPath = GraphAlgoFactory.aStar(relExpander,	costEval, estimateEval);
		PowerMock.mockStatic(GraphAlgoFactory.class);
		PathFinder<WeightedPath> shortestPath  = PowerMock.createNiceMock(PathFinder.class);
		expect(GraphAlgoFactory.aStar(relExpander,	target.costEval, target.estimateEval)).andReturn(shortestPath);
		
		//Path path = shortestPath.findSinglePath(nodeA, nodeB);
		WeightedPath path = PowerMock.createNiceMock(WeightedPath.class);
		expect(shortestPath.findSinglePath(nodeA, nodeB)).andReturn(path);
		
		//Iterable<Node> org.neo4j.graphdb.Path.nodes()
		Iterable<Node> iterable = PowerMock.createNiceMock(Iterable.class);
		java.util.Iterator<Node> iter = PowerMock.createNiceMock(java.util.Iterator.class);
		
		expect(iterable.iterator()).andReturn(iter);
		expect(iter.hasNext()).andReturn(true);
		Node node = PowerMock.createNiceMock(Node.class);
		expect(iter.next()).andReturn(node);

		PowerMock.replayAll();

		target.getShortestRoute(ps, keyValueA, keyValueB);
		String route = new String(bos.toByteArray());
		
		assertTrue(route.length() > 0);
		assertTrue(route.contains("<kml "));
		assertTrue(route.contains("<coordinates>"));
		assertTrue(route.endsWith("</kml>\n"));
		
		//System.out.println(route);
	}

}
