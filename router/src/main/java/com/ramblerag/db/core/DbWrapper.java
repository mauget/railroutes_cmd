package com.ramblerag.db.core;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import com.ramblerag.domain.Domain;
import com.ramblerag.domain.DomainConstants;
import com.ramblerag.domain.Lnk;
import com.ramblerag.domain.Nod;

public class DbWrapper {

	public static final String DB_PATH = "graphDb";
	private static Logger log = Logger.getLogger(DbWrapper.class);
	
	private GraphDatabaseFactory graphDatabaseFactory;
	private GraphDatabaseService graphDb;
	
	Node nodsReferenceNode;

	// Index of all nodes
	private Index<Node> nodeIndex;

	public void removeAll() throws ApplicationException {

		log.info("Removing all nodes and references.");

		Transaction tx = graphDb.beginTx();
		try {
			GlobalGraphOperations ops = GlobalGraphOperations.at(graphDb);

			for (Relationship relationship : ops.getAllRelationships()) {
				relationship.delete();
			}
			for (Node node : ops.getAllNodes()) {
				node.delete();
			}

			tx.success();
			log.info("Deleted all relationships and nodes");

		} catch (Exception e) {
			log.error(e.toString());
			tx.failure();
			throw new ApplicationException(e);
		} finally {
			tx.finish();
		}
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public GraphDatabaseService startDb() throws ApplicationException {
		
		graphDb = getGraphDatabaseFactory().newEmbeddedDatabase(DbWrapper.DB_PATH);
		
		nodeIndex = graphDb.index().forNodes(DomainConstants.INDEX_NAME);
		registerShutdownHook(graphDb);

		// initRefs();
		return graphDb;
	}

	public void initRefs() throws ApplicationException {
		Transaction tx = graphDb.beginTx();
		try {
			// Create railroad Nods sub reference node
			nodsReferenceNode = graphDb.createNode();

			// Node rn = graphDb.getReferenceNode();
			nodsReferenceNode.createRelationshipTo(nodsReferenceNode,
					DomainConstants.RelTypes.NODS_REFERENCE);
		} catch (Exception e) {
			log.error(e.toString());
			tx.failure();
			throw new ApplicationException(e);
		} finally {
			tx.finish();
		}

	}

	public void shutdownDb() {
		graphDb.shutdown();
	}

	public void insert(Domain obj) throws ApplicationException {
		if (obj instanceof Nod) {
			createAndIndexNode((Nod) obj);
		} else if (obj instanceof Lnk) {
			createLink((Lnk) obj);
		}
	}

	private Node createAndIndexNode(final Nod domainNode)
			throws ApplicationException {
		Transaction tx = graphDb.beginTx();
		Node node = null;
		long nodID = -1;
		try {
			node = graphDb.createNode();

			nodID = Long.parseLong(domainNode.getNodeId().trim());
			
			double lat = Double.parseDouble(domainNode.getLatitude().trim())
					* DomainConstants.SCALE_1E_6;
			double lon = Double.parseDouble(domainNode.getLongitude().trim())
					* DomainConstants.SCALE_1E_6;
			
			String railroad = domainNode.getDescription().trim();
			String stFips = domainNode.getStFIPS();

			node.setProperty(DomainConstants.PROP_NODE_ID, nodID);
			node.setProperty(DomainConstants.PROP_LATITUDE, lat);
			node.setProperty(DomainConstants.PROP_LONGITUDE, lon);
			node.setProperty(DomainConstants.PROP_RAILROAD, railroad);
			node.setProperty(DomainConstants.PROP_STFIPS, stFips);

			nodeIndex.add(node, DomainConstants.PROP_NODE_ID, nodID);

			tx.success();

		} catch (Exception e) {
			log.error(e.toString());
			tx.failure();
			throw new ApplicationException(e);
		} finally {
			tx.finish();
		}

		return node;
	}

	private void createLink(final Lnk domainLink) throws ApplicationException {
		Transaction tx = graphDb.beginTx();
		Node nodeA = null;
		Node nodeB = null;
		try {
			long keyValueA = Long.parseLong(domainLink.getaNode().trim());
			long keyValueB = Long.parseLong(domainLink.getbNode().trim());

			nodeA = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueA)
					.getSingle();
			nodeB = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueB)
					.getSingle();

			if (null == nodeA || null == nodeB) {
				throw new ApplicationException(
						String.format(
								"Link referenced %s A-Node with key %d, and the link referenced %s B-Node with key %d",
								nodeA, keyValueA, nodeB, keyValueB));
			}

			log.info(String
					.format("Link inserted from  %s A-Node with key %d, to the link referenced %s B-Node with key %d",
							nodeA, keyValueA, nodeB, keyValueB));

			nodeA.createRelationshipTo(nodeB,
					DomainConstants.RelTypes.DOMAIN_LINK);

			tx.success();

		} catch (Exception e) {
			log.error(e.toString());
			tx.failure();
			throw new ApplicationException(e);
		} finally {
			tx.finish();
		}
	}

	public GraphDatabaseFactory getGraphDatabaseFactory() {
		return graphDatabaseFactory;
	}

	public void setGraphDatabaseFactory(GraphDatabaseFactory graphDatabaseFactory) {
		this.graphDatabaseFactory = graphDatabaseFactory;
	}

	public GraphDatabaseService getGraphDb() {
		return graphDb;
	}

	public void setGraphDb(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}

	public Index<Node> getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(Index<Node> nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

}
