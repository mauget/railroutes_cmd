package com.ramblerag.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
<pre>

Detailed_Description:
	  Entity_Type:
	    Entity_Type_label:  RAIL100K.LNK   
	    Entity_Type_Definition:  Line Attribute Table
	    Entity_Type_Definition_Source:

	Field  Field   Field Field  Beg End Field
	Number Name    Type  Length Pos Pos Description
	 1     RECTYPE   C     1     1   1  Record Type: always 'L'
	 2     VERSION   C     2     2   3  File version number
	 3     REVISION  C     2     4   5  Record revision number
	 4     MODDATE   C     8     6  13  Record modification date
	 5     LINKID    I    10    14  23  Unique sequential line 
	                                    identification
	 6     FEATURID  I    10    24  33  Unique line identifier
	 7     ANODE     I    10    34  43  Node identification for
	                                    the beginning node of 
	                                    the line
	 8     BNODE     I    10    44  54  Node identification for 
	                                    the ending node of the 
	                                    line
	 9     DESCRIPT  C    35    55  88  Name or identification 
	                                    for the line feature
	10     STFIPS1   C     2    89  90  Primary State FIPS Code
	11     STFIPS2   C     2    91  92  Secondary State FIPS 
	                                    Code
</pre>
 */

@Record
public class Lnk extends Domain {

	private String recType;
	private String version;
	private String revision;
	private String modDate;
	private String linkId;
	private String featureId;
	private String aNode;
	private String bNode;
	private String description;
	private String stFIPS1;
	private String stFIPS2;

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
	public String getLinkId() {
		return linkId;
	}
	@Field(offset = 24, length = 10)
	public String getFeatureId() {
		return featureId;
	}
	@Field(offset = 34, length = 10)
	public String getaNode() {
		return aNode;
	}
	@Field(offset = 44, length = 10)
	public String getbNode() {
		return bNode;
	}
	@Field(offset = 54, length = 35)
	public String getDescription() {
		return description;
	}
	@Field(offset = 89, length = 2)
	public String getStFIPS1() {
		return stFIPS1;
	}
	@Field(offset = 91, length = 2)
	public String getStFIPS2() {
		return stFIPS2;
	}
	
	public void setbNode(String bNode) {
		this.bNode = bNode;
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
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}
	public void setaNode(String aNode) {
		this.aNode = aNode;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setStFIPS1(String stFIPS1) {
		this.stFIPS1 = stFIPS1;
	}
	public void setStFIPS2(String stFIPS2) {
		this.stFIPS2 = stFIPS2;
	}

	public String toString() {
	    return ToStringBuilder.reflectionToString(this);
	}

}
