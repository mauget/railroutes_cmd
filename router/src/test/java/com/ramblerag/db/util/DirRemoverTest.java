package com.ramblerag.db.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class DirRemoverTest {
	
	private DirRemover target;

	@Before
	public void setUp() throws Exception {
		target = new DirRemover();
	}

	@Test
	public void test() {
		
		PowerMock.replayAll();
		
		// Non-existent dir
		target.removeDirIfExists("__$$dir$not$$exist__");
		target.removeDirIfExists(null);
		target.removeDirIfExists("");
		target.removeDirIfExists("            ");
	}

}
