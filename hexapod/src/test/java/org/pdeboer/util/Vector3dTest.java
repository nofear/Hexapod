package org.pdeboer.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Vector3dTest {

	@Test
	void test_empty() {
		var v = new Vector3d();
		assertEquals(0, v.x());
		assertEquals(0, v.y());
		assertEquals(0, v.z());
	}

}