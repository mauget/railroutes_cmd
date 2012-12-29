package com.ramblerag.db.load;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ramblerag.db.core.DbInserter;
import com.ramblerag.db.core.DbWrapper;
import com.ramblerag.db.core.GlobalConstants;
import com.ramblerag.db.util.DirRemover;
import com.ramblerag.db.util.Loader;

@RunWith(PowerMockRunner.class)
public class LoaderTest {
	
	private Loader target;
	private DbInserter dbInserter;
	private DirRemover dirRemover;
	private DbWrapper dbWrapper;
	
	@Before
	public void setUp() throws Exception {
		target = new Loader();
		dbInserter = PowerMock.createNiceMock(DbInserter.class);
		target.setDbInserter(dbInserter);
		dirRemover = PowerMock.createNiceMock(DirRemover.class);
		target.setDirRemover(dirRemover);
		dbWrapper = PowerMock.createNiceMock(DbWrapper.class);
		target.setDbWrapper(dbWrapper);
	}

	@Test
	public void testLoad() throws Exception {
		
		PowerMock.replayAll();
		
		target.load(GlobalConstants.TEST_NOD, GlobalConstants.TEST_LNK);
	}

}
