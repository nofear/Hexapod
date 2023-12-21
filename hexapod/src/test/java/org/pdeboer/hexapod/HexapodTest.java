package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.pdeboer.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.pdeboer.hexapod.Hexapod.*;
import static org.pdeboer.hexapod.LegTest.*;

class HexapodTest {

	@Test
	void test_init() {
		var hexapod = new Hexapod();
		assertLegsOnGround(hexapod);

		hexapod.updateForward();
		assertLegsOnGround(hexapod);
	}

	@ParameterizedTest
	@EnumSource(Action.class)
	void test_action(final Action action) {
		var hexapod = new Hexapod();
		hexapod.execute(action);

		assertLegsOnGround(hexapod);

		hexapod.execute(action);

		assertLegsOnGround(hexapod);
	}

	@Test
	void test_walk() {
		var hexapod = new Hexapod();

		Vector3d speed = new Vector3d(0.5, 0, 0);
		hexapod.setSpeed(speed);

		for (int legIndex = 0; legIndex < LEG_COUNT; ++legIndex) {
			var leg = hexapod.getLeg(legIndex);
			leg.startMoving(speed);

			for (int i = 0; i < Leg.STEP_COUNT; ++i) {
				hexapod.update();
			}
		}

		assertLegsOnGround(hexapod);

		Vector3d offset = speed.mul(LEG_COUNT * Leg.STEP_COUNT);

		var hexapod0 = new Hexapod();
		assertEqualsVector3D(hexapod0.center().addEx(offset),
							 hexapod.center());
		for (int i = 0; i < LEG_COUNT; ++i) {
			assertEqualsVector3D(hexapod0.getLeg(i).p4.addEx(offset),
								 hexapod.getLeg(i).p4);
		}
	}

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < LEG_COUNT; ++i) {
			assertEquals(0, hexapod.getLeg(i).p4.z, 1E-7);
		}
	}
}