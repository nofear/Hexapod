package org.pdeboer.hexapod;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.pdeboer.util.*;

import java.util.stream.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class LegTest {

	@ParameterizedTest
	@ValueSource(doubles = { -50, 0, 50 })
	void test_update_inverse_right_leg(final double offsetZ) {
		double lengthCoxa = 100;
		double lengthFemur = 300;
		double lengthTibia = 400;

		double x0 = -10;

		var leg = new Leg(lengthCoxa, lengthFemur, lengthTibia);
		double y1 = sqrt(lengthTibia * lengthTibia + lengthFemur * lengthFemur) + lengthCoxa;
		leg.init(new Vector3d(x0, 0, offsetZ), 0, y1, offsetZ);

		assertEqualsVector3D(new Vector3d(x0, lengthCoxa, offsetZ), leg.p2);
		assertEqualsVector3D(new Vector3d(x0, 280, 240 + offsetZ), leg.p3);
		assertEqualsVector3D(new Vector3d(x0, y1, offsetZ), leg.p4);

		assertArrayEquals(new double[] { 0, 0.6435011087932843, PI / 2 }, leg.getRotation());
	}

	@ParameterizedTest
	@ValueSource(doubles = { -50, 0, 50 })
	void test_update_inverse_left_leg(final double offsetZ) {
		double lengthCoxa = 100;
		double lengthFemur = 300;
		double lengthTibia = 400;

		double x0 = -10;

		var leg = new Leg(lengthCoxa, lengthFemur, lengthTibia);
		double y1 = sqrt(lengthTibia * lengthTibia + lengthFemur * lengthFemur) + lengthCoxa;
		leg.init(new Vector3d(x0, 0, offsetZ), 0, -y1, offsetZ);

		assertEqualsVector3D(new Vector3d(x0, -lengthCoxa, offsetZ), leg.p2);
		assertEqualsVector3D(new Vector3d(x0, -280, 240 + offsetZ), leg.p3);
		assertEqualsVector3D(new Vector3d(x0, -y1, offsetZ), leg.p4);

		assertArrayEquals(new double[] { PI, 0.6435011087932843, PI / 2 }, leg.getRotation());
	}

	@ParameterizedTest
	@ValueSource(doubles = { -141, -20, -10, 0, 10, 20, 58 })
	void test_update_inverse_offsetY(final double offsetY) {

		var x0 = 10;
		var y1 = sqrt(100 * 100 * 2) + offsetY;

		var leg = new Leg(0, 100, 100);
		leg.init(new Vector3d(x0, 0, 0), 0, y1, 0);

		assertEqualsVector3D(new Vector3d(x0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(x0, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -45, -30, -10, 0, 10, 30, 45 })
	void test_update_inverse_offsetX(final double offsetX) {
		var y1 = sqrt(100 * 100 * 2);

		var leg = new Leg(0, 100, 100);
		leg.init(new Vector3d(0, 0, 0), offsetX, y1, 0);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(offsetX, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -0.01, 0, 0.01 })
	void test_update_inverse_rotateA(final double rotateA) {
		var y1 = sqrt(100 * 100 * 2);

		var leg = new Leg(0, 100, 100);
		leg.init(new Vector3d(0, 0, 0), 0, y1, 0);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);

		double[] r0 = { rotateA, 0, 0 };
		leg.updateInverse(r0);

		var m = Matrix.getMatrix(r0);
		leg.update(m);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);
	}

	@MethodSource("test_update_max_extend_provider")
	@ParameterizedTest
	void test_update_max_extend(
			final double[] rotation,
			final Vector3d expectedEndPoint) {

		var leg = new Leg(100, 100, 100);
		double[] r0 = { 0, 0, 0 };
		var m = Matrix.getMatrix(r0);
		leg.setRotation(rotation);
		leg.update(m);
		assertEqualsVector3D(expectedEndPoint, leg.p4);

		leg.updateInverse(r0);
		assertArrayEquals(rotation, leg.getRotation(), 1E-09);
	}

	static Stream<Arguments> test_update_max_extend_provider() {
		return Stream.of(
				arguments(new double[] { 0, PI / 2, 0 }, new Vector3d(0, 300, 0)),
				arguments(new double[] { 0, PI / 2, PI / 2 }, new Vector3d(0, 200, -100)),
				arguments(new double[] { 0, 0, PI / 2 }, new Vector3d(0, 200, 100)),
				arguments(new double[] { 0, 0, PI }, new Vector3d(0, 100, 0)),
				arguments(new double[] { 0, PI / 4, PI / 2 },
						  new Vector3d(0, 100 + Math.sqrt(100 * 100 + 100 * 100), 0))
		);
	}

	static void assertEqualsVector3D(
			final Vector3d v1,
			final Vector3d v2) {
		assertEquals(v1.x, v2.x, 1E-9, "x");
		assertEquals(v1.y, v2.y, 1E-9, "y");
		assertEquals(v1.z, v2.z, 1E-9, "z");
	}

}