package com.ramblerag.domain;

public abstract class Domain {
	
	public abstract String getRecType();

	public abstract String getVersion();

	public abstract String getRevision();

	public abstract String getModDate();
	
	public long getDomainId() {
		
		String idProp = "0";
		
		if (this instanceof Nod){
			idProp = ((Nod)this).getNodeId().trim();
		} else if (this instanceof Lnk){
			idProp = ((Lnk)this).getLinkId().trim();
		}
		return Long.parseLong(idProp);
	}
}
