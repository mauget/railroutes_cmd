package com.ramblerag.db.util;

import static org.easymock.EasyMock.expect;

import java.io.File;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FSDirectory.class, IndexReader.class})
public class IndexDumperTest {
	
	private IndexDumper target;

	@Before
	public void setUp() throws Exception {
		target = new IndexDumper();
	}

	@Test
	public void testDump() throws Exception {

		PowerMock.mockStatic(FSDirectory.class);
		PowerMock.mockStatic(IndexReader.class);
		FSDirectory fsDirectory = PowerMock.createNiceMock(FSDirectory.class);
		expect(FSDirectory.open(new File("."))).andReturn(fsDirectory);
		IndexReader reader = PowerMock.createNiceMock(IndexReader.class);
		expect(IndexReader.open(fsDirectory, true)).andReturn(reader);
		
		PowerMock.replayAll();
		target.dump(".");
	}

}
