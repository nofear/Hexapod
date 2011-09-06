package com.fearke.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class Plane3dTest {

	@Test
	public void test() {
		Vector3d p1 = new Vector3d(-1, 0, 0);
		Vector3d p2 = new Vector3d(0, -1, 0);
		Vector3d p3 = new Vector3d(0, 0, -1);
		Plane3d p = new Plane3d(p1, p2, p3);

		assertEquals(new Vector3d(), p.getNormal());
	}

	@Test
	public void testProject() {
		Vector3d p1 = new Vector3d(-1, 0, 0);
		Vector3d p2 = new Vector3d(0, -1, 0);
		Vector3d p3 = new Vector3d(0, 0, -1);
		Plane3d p = new Plane3d(p1, p2, p3);

		Vector3d l = new Vector3d(1, 1, 1);
		Vector3d r = p.project(l);

		assertEquals(0, p.distance(r), 1e-15);
	}
}
