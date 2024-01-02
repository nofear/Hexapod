package org.pdeboer.util;

import org.junit.jupiter.api.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.pdeboer.hexapod.Hexapod.*;
import static org.pdeboer.util.Rotation3D.*;

class Rotation3DTest {

	@Test
	void test_multiply_rotateX() {
		assertEqualsVector3D(VECTOR_Y.neg(), getRotateX(PI / 2).apply(VECTOR_Z));
		assertEqualsVector3D(VECTOR_Z, getRotateX(PI / 2).apply(VECTOR_Y));

		assertEqualsVector3D(VECTOR_Y.neg(), getRotateX(PI).apply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateX(PI).apply(VECTOR_Z));
	}

	@Test
	void test_multiply_rotateY() {
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateY(PI / 2).apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X, getRotateY(PI / 2).apply(VECTOR_Z));

		assertEqualsVector3D(VECTOR_X.neg(), getRotateY(PI).apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Z.neg(), getRotateY(PI).apply(VECTOR_Z));
	}

	@Test
	void test_multiply_rotateZ() {
		assertEqualsVector3D(VECTOR_Y, getRotateZ(PI / 2).apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X.neg(), getRotateZ(PI / 2).apply(VECTOR_Y));

		assertEqualsVector3D(VECTOR_X.neg(), getRotateZ(PI).apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Y.neg(), getRotateZ(PI).apply(VECTOR_Y));
	}

	@Test
	void test_multiply_vector() {
		var m = Rotation3D.of(0, 0, 0);
		assertEquals(VECTOR_X, m.apply(VECTOR_X));
		assertEquals(VECTOR_Y, m.apply(VECTOR_Y));
		assertEquals(VECTOR_Z, m.apply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_roll() {
		var m = Rotation3D.of(PI / 2, 0, 0);
		assertEqualsVector3D(VECTOR_X, m.apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Z, m.apply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Y.neg(), m.apply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_pitch() {
		var m = Rotation3D.of(0, PI / 2, 0);
		assertEqualsVector3D(VECTOR_Z.neg(), m.apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_Y, m.apply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_X, m.apply(VECTOR_Z));
	}

	@Test
	void test_multiply_vector_yaw() {
		var m = Rotation3D.of(0, 0, PI / 2);
		assertEqualsVector3D(VECTOR_Y, m.apply(VECTOR_X));
		assertEqualsVector3D(VECTOR_X.neg(), m.apply(VECTOR_Y));
		assertEqualsVector3D(VECTOR_Z, m.apply(VECTOR_Z));
	}

	@Test
	void test_angle_roll() {
		var m = Rotation3D.of(0.4, 0, 0);
		var v = m.apply(VECTOR_Y);
		var a = v.angle(VECTOR_Y);
		assertEquals(0.4, a, EPSILON);
	}

	@Test
	void test_angle_pitch() {
		var m = Rotation3D.of(0.0, 0.4, 0);
		var v = m.apply(VECTOR_X);
		var a = v.angle(VECTOR_X);
		assertEquals(0.4, a, EPSILON);
	}

	@Test
	void test_rotate() {
		var m = Rotation3D.getRotateX(0.4);
		var vt = m.apply(VECTOR_Z);

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