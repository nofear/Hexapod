package com.fearke.util;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class MatrixTest {

	@Test
	public void testRandom() {
		Random rnd = new Random(0);
		for (int i = 0; i < 1000; ++i) {
			Vector3d v = new Vector3d(rnd.nextDouble() - 0.5, rnd.nextDouble() - 0.5, rnd.nextDouble() - 0.5);
			testV(v);
		}
	}

	@Test
	public void test() {
		testV(new Vector3d(1, 0, 0));
		testV(new Vector3d(0, 1, 0));
		testV(new Vector3d(0, 0, 1));
		testV(new Vector3d(1, 1, 0));
		testV(new Vector3d(1, 0, 1));
		testV(new Vector3d(0, 1, 1));
		testV(new Vector3d(1, 1, 1));

		testV(new Vector3d(-1, 0, 0));
		testV(new Vector3d(0, -1, 0));
		testV(new Vector3d(0, 0, -1));
		testV(new Vector3d(-1, -1, 0));
		testV(new Vector3d(-1, 0, -1));
		testV(new Vector3d(0, -1, -1));
		testV(new Vector3d(-1, -1, -1));
	}

	public void testV(Vector3d v) {
		v.normalize();
		Vector3d n = new Vector3d(v);
		Matrix m = Matrix.getMatrix(n);
		Vector3d r = m.multiply(new Vector3d(0, 0, 1));
		assertTrue(v.epsilonEquals(r, 1E-14));
	}
}
