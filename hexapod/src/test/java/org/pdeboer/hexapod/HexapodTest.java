package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;
import org.pdeboer.hexapod.Hexapod.*;

import static org.junit.jupiter.api.Assertions.*;

class HexapodTest {

	@Test
	void test_init() {
		var hexapod = new Hexapod();
		assertLegsOnGround(hexapod);
	}

	@Test
	void test_move_up() {
		var hexapod = new Hexapod();
		hexapod.execute(Action.UP);

		assertLegsOnGround(hexapod);
	}

	@Test
	void test_roll_plus() {
		var hexapod = new Hexapod();
		hexapod.execute(Action.ROLL_PLUS);

		assertLegsOnGround(hexapod);
	}

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < 6; ++i) {
			assertEquals(0, hexapod.getLeg(i).p4.z, 1E-8);
		}
	}
}