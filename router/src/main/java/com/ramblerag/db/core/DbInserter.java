package com.ramblerag.db.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.neo4j.unsafe.batchinsert.LuceneBatchInserterIndexProvider;

import com.ramblerag.domain.Domain;
import com.ramblerag.domain.DomainConstants;
import com.ramblerag.domain.Lnk;
import com.ramblerag.domain.Nod;

public class DbInserter {

	private static Logger log = Logger.getLogger(DbInserter.class);
	
	private BatchInserter inserter;
	
	// Index of all nodes
	private BatchInserterIndexProvider indexProvider;
	private BatchInserterIndex nodeIndex;
	
	public void insertBulk(String[] args) {
		if (args.length == 0){
			args = new String[]{DbWrapper.DB_PATH};
		}
		inserter = startDbInserter(args);
		shutdownDbInserter();
	}
	
	public void insert(Domain obj) throws ApplicationException {
		if (obj instanceof Nod) {
			createAndIndexNode((Nod) obj);
		} else if (obj instanceof Lnk) {
			createLink((Lnk) obj);
		}
	}

	public BatchInserter startDbInserter(String[] args) {
		
		if (null == getInserter()){
			inserter = BatchInserters.inserter(DbWrapper.DB_PATH);
		}
		if (null == getIndexProvider()){
			indexProvider = new LuceneBatchInserterIndexProvider(inserter);
		}
		if (null == getNodeIndex()){
			nodeIndex = indexProvider.nodeIndex(DomainConstants.INDEX_NAME, MapUtil.stringMap( "type", "exact" ) );
		}
		nodeIndex.setCacheCapacity(DomainConstants.INDEX_NAME, 134000);
		
		return inserter;
	}
	
	public void shutdownDbInserter() {
		indexProvider.shutdown();
		inserter.shutdown();
	}

	private Node createAndIndexNode(final Nod domainNode)
			throws ApplicationException {
		
		Node node = null;
		
		long nodID = -1;
		try {
			// Read the node from the flat file.
			nodID = Long.parseLong(domainNode.getNodeId().trim());
			double lat = Double.parseDouble(domainNode.getLatitude().trim())
					* DomainConstants.SCALE_1E_6;
			double lon = Double.parseDouble(domainNode.getLongitude().trim())
					* DomainConstants.SCALE_1E_6;
			String railroad = domainNode.getDescription().trim();
			String stFips = domainNode.getStFIPS();

			// Store the node in the database
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(DomainConstants.PROP_NODE_ID, nodID);
			properties.put(DomainConstants.PROP_LATITUDE, lat);
			properties.put(DomainConstants.PROP_LONGITUDE, lon);
			properties.put(DomainConstants.PROP_RAILROAD, railroad);
			properties.put(DomainConstants.PROP_STFIPS, stFips);
			
			long nativeNodeId = inserter.createNode( properties );

			// Store the index of the node in the ... index
			properties.clear();
			properties.put(DomainConstants.PROP_NODE_ID, nodID);
			nodeIndex.add(nativeNodeId, properties);

		} catch (Exception e) {
			log.error(e.toString());
			throw new ApplicationException(e);
		}

		return node;
	}

	private void createLink(final Lnk domainLink) throws ApplicationException {
		
		try {
			// Get domain IDs (not native Neo4j IDs) from the domain link read from flat file
			long keyValueA = Long.parseLong(domainLink.getaNode().trim());
			long keyValueB = Long.parseLong(domainLink.getbNode().trim());

			// Get node stored under domain ID property -- from index.
			long nodeIdA = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueA).getSingle();
			long nodeIdB = nodeIndex.get(DomainConstants.PROP_NODE_ID, keyValueB).getSingle();

			Map<String, Object> props = new HashMap<String, Object>();
			props.put(DomainConstants.REL_PROP_STFIPS1, domainLink.getStFIPS1());
			props.put(DomainConstants.REL_PROP_STFIPS2, domainLink.getStFIPS2());
			
			inserter.createRelationship( nodeIdA, nodeIdB, DomainConstants.RelTypes.DOMAIN_LINK, props );

		} catch (Exception e) {
			log.error(e.toString());
			throw new ApplicationException(e);
		}
	}

	public void flushToIndex() {
		nodeIndex.flush();
	}

	public BatchInserter getInserter() {
		return inserter;
	}

	public void setInserter(BatchInserter inserter) {
		this.inserter = inserter;
	}

	public BatchInserterIndexProvider getIndexProvider() {
		return indexProvider;
	}

	public void setIndexProvider(BatchInserterIndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	public BatchInserterIndex getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(BatchInserterIndex nodeIndex) {
		this.nodeIndex = nodeIndex;
	}
}
