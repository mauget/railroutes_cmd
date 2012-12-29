package com.ramblerag.domain;

import org.neo4j.graphdb.RelationshipType;

public final class DomainConstants {

	private DomainConstants() {
	}

	public static final String PROP_NODE_ID = "prop_node_id";
	public static final String PROP_RAILROAD = "prop_railroad";
	public static final String PROP_LONGITUDE = "prop_longitude";
	public static final String PROP_LATITUDE = "prop_latitude";
	public static final String PROP_STFIPS = "prop_st_fips";
	public static final String INDEX_NAME = "nodes";
	public static final double SCALE_1E_6 = 1e-6;
	
	public static final String REL_PROP_STFIPS2 = "STFIPS2";
	public static final String REL_PROP_STFIPS1 = "STFIPS1";

	public static enum RelTypes implements RelationshipType {
		DOMAIN_NODE, DOMAIN_LINK, NODS_REFERENCE
	}
}
