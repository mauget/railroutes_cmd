package com.ramblerag.db.route;

public final class KMLConstants {

	private KMLConstants() {
	}
	
	//-----------
	// For proof-of-concept elsewhere, we're raw XML here instead of fancier XML object binding
	
	public static final String KML_LINE_START = 
	"<?xml version='1.0' encoding='UTF-8'?>" +
	"<kml xmlns='http://www.opengis.net/kml/2.2'>" +
	  "<Document>" +
	    "<name>Paths</name>" +
	    "<description>Route computed using Neo4j, a graph database that knows the Djkstra and A* shortest path algorithms.</description>" +
	    "<Style id='whiteLineGreenPoly'>" +
	      "<LineStyle>" +
	        "<color>ffffffff</color>" +
	        "<width>3</width>" +
	      "</LineStyle>" +
	      "<PolyStyle>" +
	        "<color>ffffffff</color>" +
	      "</PolyStyle>" +
	    "</Style>" +
	    "<Placemark>" +
	      "<name>Absolute Extruded</name>" +
	      "<description>Transparent green wall with yellow outlines</description>" +
	      "<styleUrl>whiteLineGreenPoly</styleUrl>" +
	      "<LineString>" +
	        "<extrude>0</extrude>" +
	        "<tessellate>1</tessellate>" +
	        "<altitudeMode>clampToGround</altitudeMode>" +
	        "<coordinates>";
	
	// Emit coordinate lon,lat,altitude triplets "here". e.g -115.0001,49.878,2300
	
	public static final String KML_LINE_END = 
		    "</coordinates>" +
		  "</LineString>" +
	    "</Placemark>" +
	  "</Document>" +
	"</kml>";
	
	
	//-----------
}
