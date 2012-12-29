package com.ramblerag.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * <pre>
 * Detailed_Description:
 * 	  Entity_Type:
 * 	    Entity_Type_label:  RAIL100K.NOD
 * 	    Entity_Type_Definition:  Node Attribute Table
 * 	    Entity_Type_Definition_Source:
 * 
 * 	Field  Field   Field Field  Beg End Field
 * 	Number Name    Type  Length Pos Pos Description
 * 	  1    RECTYPE   C     1     1   1  Record Type: always 'N'
 * 	  2    VERSION   C     2     2   3  File version number
 * 	  3    REVISION  C     2     4   5  Record revision number
 * 	  4    MODDATE   C     8     6  13  Record modification date
 * 	  5    NODEID    I    10    14  23  Unique sequential node 
 * 	                                    identification
 * 	  6    FEATURID  I    10    24  33  Unique node identifier
 * 	  7    LONGITUD  I    10    34  43  Longitude (6 implied 
 * 	                                    decimal places)
 * 	  8    LATITUDE  I    10    44  53  Latitude (6 implied 
 * 	                                    decimal places)
 * 	  9    DESCRIPT  C    35    54  88  Name or identification 
 * 	                                    for the node
 * 	 10    STFIPS    C     2    89  90  State FIPS Code
 * </pre>
 */
@Record
public class Nod extends Domain {
	
	private String recType;
	private String version;
	private String revision;
	private String modDate;
	private String nodeId;
	private String featureId;
	private String longitude;
	private String latitude;
	private String description;
	private String stFIPS;
	
	@Override
	@Field(offset = 1, length = 1)
	public String getRecType() {
		return recType;
	}
	@Override
	@Field(offset = 2, length = 2)
	public String getVersion() {
		return version;
	}
	@Override
	@Field(offset = 4, length = 2)
	public String getRevision() {
		return revision;
	}
	@Override
	@Field(offset = 6, length = 8)
	public String getModDate() {
		return modDate;
	}
	@Field(offset = 14, length = 10)
	public String getNodeId() {
		return nodeId;
	}
	@Field(offset = 24, length = 10)
	public String getFeatureId() {
		return featureId;
	}
	@Field(offset = 34, length = 10)
	public String getLongitude() {
		return longitude;
	}
	@Field(offset = 44, length = 10)
	public String getLatitude() {
		return latitude;
	}
	@Field(offset = 54, length = 35)
	public String getDescription() {
		return description;
	}
	@Field(offset = 89, length = 2)
	public String getStFIPS() {
		return stFIPS;
	}
	public void setRecType(String recType) {
		this.recType = recType;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public void setModDate(String modDate) {
		this.modDate = modDate;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStFIPS(String stFIPS) {
		this.stFIPS = stFIPS;
	}
	
	public String toString() {
	    return ToStringBuilder.reflectionToString(this);
	}
}
