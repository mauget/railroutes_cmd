package com.ramblerag.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

public class LnkTest {

	private static final String record = "L01         0        13 530000013        19        30BN                                 53  ";
	private FixedFormatManager manager;  

	@Before
	public void setUp() throws Exception {
		manager = new FixedFormatManagerImpl();
	}

	@Test
	public void test() {
		Lnk lnk = manager.load(Lnk.class, record);
		
		assertEquals(13L, lnk.getDomainId());
		
		assertEquals("L", lnk.getRecType());
		assertEquals("01", lnk.getVersion());
		assertEquals("", lnk.getRevision());
		assertEquals(" 530000013", lnk.getFeatureId());
		assertEquals("        13", lnk.getLinkId());
		assertEquals("        19", lnk.getaNode());
		assertEquals("        30", lnk.getbNode());
		assertEquals("BN", lnk.getDescription());
		assertEquals("53", lnk.getStFIPS1());
		assertEquals("", lnk.getStFIPS2());
	}

}
