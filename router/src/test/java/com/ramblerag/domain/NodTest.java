package com.ramblerag.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl;

public class NodTest {

	private static final String record = "N01         0         1    132812-122266884  49002640                                     ";
	private FixedFormatManager manager;  

	@Before
	public void setUp() throws Exception {
		manager = new FixedFormatManagerImpl();
	}

	@Test
	public void test() {
		Nod nod = manager.load(Nod.class, record);

		assertEquals(1L, nod.getDomainId());
		
		assertEquals("N", nod.getRecType());
		assertEquals("01", nod.getVersion());
		assertEquals("", nod.getRevision());
		assertEquals("       0", nod.getModDate());
		assertEquals("         1", nod.getNodeId());
		assertEquals("    132812", nod.getFeatureId());
		assertEquals("-122266884", nod.getLongitude());
		assertEquals("  49002640", nod.getLatitude());
		assertEquals("", nod.getDescription());
		assertEquals("", nod.getStFIPS());
	}

}
