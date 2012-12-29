package com.ramblerag.db.util;

import java.io.File;

import org.apache.log4j.Logger;

public class DirRemover {

	private static Logger log = Logger.getLogger(DirRemover.class);
	
	public boolean removeDirIfExists(String dir){
		boolean wasDeleted = false;
		if (null != dir && dir.trim().length() > 0){
			File dbFolder = new File(dir);
			if (dbFolder.canWrite() && dbFolder.isDirectory()){
				
				wasDeleted = deleteDir(dbFolder);
			}
			log.info(String.format("Database at \"%s\" was%s deleted.", dir, (wasDeleted?"" : " not")));
		} else {
			log.info(String.format("Database at \"%s\" not valid.", dir));
		}
		return wasDeleted;
	}

	protected boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
}
