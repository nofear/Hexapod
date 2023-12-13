package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.pdeboer.hexapod.Hexapod.*;

class HexapodTest {

	@Test
	void test_init() {
		var hexapod = new Hexapod();
		assertLegsOnGround(hexapod);

		hexapod.update();
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

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < 6; ++i) {
			assertEquals(0, hexapod.getLeg(i).p4.z, EPSILON);
		}
	}
}