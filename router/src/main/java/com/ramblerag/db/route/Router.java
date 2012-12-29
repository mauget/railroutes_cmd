package com.ramblerag.db.route;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.graphalgo.CommonEvaluators;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.EstimateEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.Traversal;

import com.ramblerag.db.core.ApplicationException;
import com.ramblerag.db.core.DbWrapper;
import com.ramblerag.domain.DomainConstants;

/**
 * Dump route to KML, suitable for feeding to Google Earth

 * @author mauget
 *
 */
public class Router {
	
	public static final String SYS_PROP_JAVA_IO_TMPDIR = "java.io.tmpdir";

	private static final String SYS_PROP_OS_NAME = "os.name";

	private static Logger log = Logger.getLogger(Router.class);
	
	// Injected
	private DbWrapper dbWrapper;

	// Routing helper functions
	protected final EstimateEvaluator<Double> estimateEval = CommonEvaluators.geoEstimateEvaluator(
			DomainConstants.PROP_LATITUDE, DomainConstants.PROP_LONGITUDE );

	protected final CostEvaluator<Double> costEval = new CostEvaluator<Double>() {

    	// Constant cost. Could be a function of lading type, location, fuel surcharge, ..
		public Double getCost(Relationship relationship, Direction direction) {
			return 1d;
		}};

/*	public static void main(String[] args) {

		try {
			ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {GlobalConstants.APPLICATION_CONTEXT_XML});
		
			Router router = appContext.getBean(Router.class);
			
			// May dump to System.out PrintStream, but we can supply temp PrintStream
			
			String dir = System.getProperty(SYS_PROP_JAVA_IO_TMPDIR);
			String kmlPath = String.format("%srouteTmp.kml", dir);
			PrintStream ps = new PrintStream(kmlPath);
			
			router.getShortestRoute(ps, 1, 133752);
//			router.getShortestRoute(ps, 123,  12345);
//			router.getShortestRoute(ps, 13000, 123);
//			router.getShortestRoute(ps, 1, 2000);
//			router.getShortestRoute(ps, 1000, 8000);
//			router.getShortestRoute(ps, 4321, 110678);
//			router.getShortestRoute(ps, 11000, 2400);
//			router.getShortestRoute(ps, 8000, 2000);
			
			router.startGoogleEarth(kmlPath);
//		} catch (ApplicationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	private JSONObject osCmdMap  ;
	
	protected String getStartProcessCmd() throws JSONException {
		
		if (osCmdMap == null) { //
			
			String osCmdJSON = "{" + //
					"\"AIX\" : \"open\"," + //
					"\"Digital Unix\" : \"open\"," + //
					"\"FreeBSD\" : \"open\"," + //
					"\"HP UX\" : \"open\"," + //
					"\"Irix\" : \"open\"," + //
					"\"Linux\" : \"open\"," + //
					"\"Mac OS\" : \"open\"," + // //?
					"\"Mac OS X\" : \"open\"," + //
					"\"MPE/iX\" : \"open\"," + //
					"\"Netware 4.11\" : \"open\"," + // //?
					"\"OS/2\" : \"exec\"," + //
					"\"Solaris\" : \"open\"," + //
					"\"Windows 2000\" : \"exec\"," + //
					"\"Windows 95\" : \"exec\"," + //
					"\"Windows 98\" : \"exec\"," + //
					"\"Windows NT\" : \"exec\"," + //
					"\"Windows Vista\" : \"exec\"," + //
					"\"Windows XP\" : \"exec\"," + //
					"\"Windows 7\" : \"exec\"," + //
					"\"Windows 8\" : \"exec\"," + //
					"\"Windows Server 2008\" : \"exec\"," + //
					"\"Windows Server 2008 R2\" : \"exec\"" + //
					"}"; //
			
			 osCmdMap = new JSONObject(osCmdJSON);
		}
		
		String osName = System.getProperty(SYS_PROP_OS_NAME);
		return (String) osCmdMap.get(osName);
	}
	
	protected void startGoogleEarth(String kmlPath) {
		try {
			
			// Create ProcessBuilder instance for OS to open Google Earth.app
			String osName = System.getProperty(SYS_PROP_OS_NAME);
			String processCmd = getStartProcessCmd();

			log.info(String.format("Operating system is \"%s\"", osName));
			log.info(String.format("... OS process spawn command is \"%s\"", processCmd));

			ProcessBuilder processBuilder = new ProcessBuilder(processCmd, kmlPath);
			processBuilder.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void getShortestRoute(PrintStream ps, long keyValueA, long keyValueB)
			throws ApplicationException {
		
		log.info(String.format("Finding least-expensive route from node %d to node %d", keyValueA, keyValueB));
		
		GraphDatabaseService graphDb = getDbWrapper().startDb();

		Index<Node> nodeIndex = graphDb.index().forNodes(DomainConstants.INDEX_NAME);

		Node nodeA = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueA)
				.getSingle();
		
		Node nodeB = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueB)
				.getSingle();

		Transaction tx = graphDb.beginTx();
		try {
			Expander relExpander = Traversal.expanderForTypes(
					DomainConstants.RelTypes.DOMAIN_LINK, Direction.BOTH);

			relExpander.add(DomainConstants.RelTypes.DOMAIN_LINK, Direction.BOTH);

			PathFinder<WeightedPath> shortestPath = GraphAlgoFactory.aStar(relExpander,
					costEval, estimateEval);

			ps.println(KMLConstants.KML_LINE_START);
			
			emitCoordinate(ps, shortestPath, nodeA, nodeB);
			
			ps.println(KMLConstants.KML_LINE_END);
			
			tx.success();
			
		} finally {
			tx.finish();
			getDbWrapper().shutdownDb();
		}
	}

	private void emitCoordinate(PrintStream printSteam, PathFinder<WeightedPath> shortestPath, Node nodeA, Node nodeB) {

		WeightedPath path = shortestPath.findSinglePath(nodeA, nodeB);
		
		if (null != path){
			for (Node node : path.nodes()) {
				
				double lat = (Double) node.getProperty(DomainConstants.PROP_LATITUDE);
				double lon = (Double) node.getProperty(DomainConstants.PROP_LONGITUDE);
				
				printSteam.println(String.format("%f,%f,2300", lon, lat));
			}
		}
	}

	public DbWrapper getDbWrapper() {
		return dbWrapper;
	}

	public void setDbWrapper(DbWrapper dbWrapper) {
		this.dbWrapper = dbWrapper;
	}

}
