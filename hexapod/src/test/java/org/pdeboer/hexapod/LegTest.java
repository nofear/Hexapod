package org.pdeboer.hexapod;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.pdeboer.util.*;

import java.util.stream.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.pdeboer.hexapod.Leg.Id.*;

class LegTest {

	@ParameterizedTest
	@ValueSource(doubles = { -50, 0, 50 })
	void test_update_inverse_right_leg(final double offsetZ) {
		double lengthCoxa = 100;
		double lengthFemur = 300;
		double lengthTibia = 400;

		double x0 = 0;

		var leg = new Leg(RIGHT_FRONT, lengthCoxa, lengthFemur, lengthTibia);
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

		var leg = new Leg(LEFT_FRONT, lengthCoxa, lengthFemur, lengthTibia);
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

		var leg = new Leg(RIGHT_FRONT, 0, 100, 100);
		leg.init(new Vector3d(x0, 0, 0), 0, y1, 0);

		assertEqualsVector3D(new Vector3d(x0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(x0, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -45, -30, -10, 0, 10, 30, 45 })
	void test_update_inverse_offsetX(final double offsetX) {
		var y1 = sqrt(100 * 100 * 2);

		var leg = new Leg(RIGHT_FRONT, 0, 100, 100);
		leg.init(new Vector3d(0, 0, 0), offsetX, y1, 0);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(offsetX, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -PI / 4, -0.05, 0, 0.05, PI / 4 })
	void test_update_inverse_roll(final double roll) {
		var y1 = sqrt(100 * 100 * 2) + 100;

		var leg = new Leg(RIGHT_FRONT, 100, 100, 100);
		leg.init(new Vector3d(0, 0, 0), 0, y1, 0);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);

		double[] r0 = { roll, 0, 0 };
		leg.updateInverse(r0);
		leg.update(r0);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -PI / 4, -0.05, 0, 0.05, PI / 4 })
	void test_update_inverse_pitch(final double pitch) {
		var y1 = sqrt(100 * 100 * 2) + 100;
		var x1 = 1;

		var leg = new Leg(RIGHT_FRONT, 100, 100, 100);
		leg.init(new Vector3d(0, 0, 0), x1, y1, 0);
		assertEqualsVector3D(new Vector3d(x1, y1, 0), leg.p4);

		double[] r0 = { 0, pitch, 0 };
		leg.updateInverse(r0);
		leg.update(r0);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(x1, y1, 0), leg.p4);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -0.20, -0.05, 0, 0.05, 0.20 })
	void test_update_inverse_yaw(final double yaw) {
		var r = Math.sqrt(2) / 2 * 100;

		var y1 = sqrt(r * r * 2) + 100;

		var leg = new Leg(RIGHT_FRONT, r, r, r);
		leg.init(new Vector3d(0, 0, 0), 0, y1, 0);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);

		double[] r0 = { 0, 0, yaw };
		leg.updateInverse(r0);
		leg.update(r0);

		assertEqualsVector3D(new Vector3d(0, 0, 0), leg.p1);
		assertEqualsVector3D(new Vector3d(0, y1, 0), leg.p4);
	}

	@MethodSource("test_update_max_extend_provider")
	@ParameterizedTest
	void test_update_max_extend(
			final double[] rotation,
			final Vector3d expectedEndPoint) {

		var leg = new Leg(RIGHT_FRONT, 100, 200, 300);
		leg.setRotation(rotation);

		double[] r0 = { 0, 0, 0 };
		leg.update(r0);

		assertEqualsVector3D(expectedEndPoint, leg.p4);

		leg.updateInverse(r0);
		assertArrayEquals(rotation, leg.getRotation(), 1E-09);
	}

	static Stream<Arguments> test_update_max_extend_provider() {
		return Stream.of(
				arguments(new double[] { 0, PI / 2, 0 }, new Vector3d(0, 600, 0)),
				arguments(new double[] { 0, 0, PI / 2 }, new Vector3d(0, 400, 200)),
				arguments(new double[] { 0, 0, 0 }, new Vector3d(0, 100, 500))
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