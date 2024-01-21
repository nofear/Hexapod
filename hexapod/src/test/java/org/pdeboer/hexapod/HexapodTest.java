package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.pdeboer.hexapod.Hexapod.*;

class HexapodTest {

	@Test
	void test_init() {
		var hexapod = new Hexapod((x, y) -> 0);
		assertLegsOnGround(hexapod);

		hexapod.updateForward();
		assertLegsOnGround(hexapod);
	}

	@ParameterizedTest
	@EnumSource(Action.class)
	void test_action(final Action action) {
		var hexapod = new Hexapod((x, y) -> 0);
		hexapod.execute(action);

		assertLegsOnGround(hexapod);

		hexapod.execute(action);

		assertLegsOnGround(hexapod);
	}

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);
			assertEquals(0, leg.p4.z(), Leg.GROUND_EPSILON, leg.id().name());
		}
	}
}