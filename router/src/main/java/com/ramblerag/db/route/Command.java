package com.ramblerag.db.route;

import java.io.PrintStream;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ramblerag.db.core.GlobalConstants;

public class Command {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Command().exec(args);
	}

	public void exec(String[] args) {
		
		final long defaultRange[] = new long[]{1L, 133752L};
		
		if (args.length == 2){
			long nodeAId = Long.parseLong(args[0]);
			long nodeBId = Long.parseLong(args[1]);
			
			try {
				exec(nodeAId, nodeBId);
			} catch (Exception e) {
				System.out.println(e);
				blurb(defaultRange);
			}
			
		} else {
			blurb(defaultRange);
		}
	}
	
	public void exec(long nodeAId, long nodeBId){
		try {
			ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
		        new String[] {GlobalConstants.APPLICATION_CONTEXT_XML});
		
			Router router = appContext.getBean(Router.class);
			
			// May dump to System.out PrintStream, but we can supply temp PrintStream
			
			String dir = System.getProperty(Router.SYS_PROP_JAVA_IO_TMPDIR);
			String kmlPath = String.format("%srouteTmp.kml", dir);
			PrintStream ps = new PrintStream(kmlPath);
			
			router.getShortestRoute(ps, nodeAId, nodeBId);
			router.startGoogleEarth(kmlPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void blurb(long[] defaultRange) {
		System.out.println("Display a shortest route between two rail stations denoted by a pair of integer station numbers in range {1..133752");
		System.out.println(String.format("E.g. %s %d %d", "<command>", defaultRange[0], defaultRange[1]));
		
	}

}
