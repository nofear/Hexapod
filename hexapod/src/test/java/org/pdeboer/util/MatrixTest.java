package org.pdeboer.util;

import org.junit.jupiter.api.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.pdeboer.hexapod.Hexapod.*;
import static org.pdeboer.util.Matrix.*;

class MatrixTest {

	@Test
	void test_multiply_rotateX() {
		assertEqualsVector3D(VECTOR_Y.neg(), getRotateX(PI / 2).multiply(VECTOR_Z));
		assertEqualsVector3D(VECTOR_Z, getRotateX(PI / 2).multiply(VECTOR_Y));

		assertEqualsVector3D(VECTOR_Y.neg(), getRotateX(PI).multiply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateX(PI).multiply(VECTOR_Z));
	}

	@Test
	void test_multiply_rotateY() {
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateY(PI / 2).multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X, getRotateY(PI / 2).multiply(VECTOR_Z));

		assertEqualsVector3D(VECTOR_X.neg(), getRotateY(PI).multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateY(PI).multiply(VECTOR_Z));
	}

	@Test
	void test_multiply_rotateZ() {
		assertEqualsVector3D(VECTOR_Y, getRotateZ(PI / 2).multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X.neg(), getRotateZ(PI / 2).multiply(VECTOR_Y));

		assertEqualsVector3D(VECTOR_X.neg(), getRotateZ(PI).multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Y.neg(), getRotateZ(PI).multiply(VECTOR_Y));
	}

	@Test
	void test_multiply_vector() {
		var m = Matrix.getMatrix(0, 0, 0);
		assertEquals(VECTOR_X, m.multiply(VECTOR_X));
		assertEquals(VECTOR_Y, m.multiply(VECTOR_Y));
		assertEquals(VECTOR_Z, m.multiply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_roll() {
		var m = Matrix.getMatrix(PI / 2, 0, 0);
		assertEqualsVector3D(VECTOR_X, m.multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Z, m.multiply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Y.neg(), m.multiply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_pitch() {
		var m = Matrix.getMatrix(0, PI / 2, 0);
		assertEqualsVector3D(VECTOR_Z.neg(), m.multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Y, m.multiply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_X, m.multiply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_yaw() {
		var m = Matrix.getMatrix(0, 0, PI / 2);
		assertEqualsVector3D(VECTOR_Y, m.multiply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X.neg(), m.multiply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Z, m.multiply(VECTOR_Z));
	}

	@Test
	void test_angle_roll() {
		var m = Matrix.getMatrix(0.4, 0, 0);
		var v = m.multiply(VECTOR_Y);
		var a = v.angle(VECTOR_Y);
		assertEquals(0.4, a, EPSILON);
	}

	@Test
	void test_angle_pitch() {
		var m = Matrix.getMatrix(0.0, 0.4, 0);
		var v = m.multiply(VECTOR_X);
		var a = v.angle(VECTOR_X);
		assertEquals(0.4, a, EPSILON);
	}

	@Test
	void test_rotate() {
		var m = Matrix.getRotateX(0.4);
		var vt = m.multiply(VECTOR_Z);

		System.out.println(vt);
	}

	void assertEqualsVector3D(
			final Vector3d expected,
			final Vector3d v) {
		assertEquals(expected.x(), v.x(), EPSILON, "x");
		assertEquals(expected.y(), v.y(), EPSILON, "y");
		assertEquals(expected.z(), v.z(), EPSILON, "z");
	}

	private static final Vector3d VECTOR_X = new Vector3d(1, 0, 0);
	private static final Vector3d VECTOR_Y = new Vector3d(0, 1, 0);
	private static final Vector3d VECTOR_Z = new Vector3d(0, 0, 1);
}