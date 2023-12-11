package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;

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

	@Test
	void test_move_up() {
		var hexapod = new Hexapod();
		hexapod.execute(Action.UP);

		assertLegsOnGround(hexapod);
	}

	@Test
	void test_yaw_plus() {
		var hexapod = new Hexapod();
		hexapod.execute(Action.YAW_PLUS);

		assertLegsOnGround(hexapod);
	}

	@Test
	void test_pitch_plus() {
		var hexapod = new Hexapod();
		hexapod.execute(Action.PITCH_PLUS);

		assertLegsOnGround(hexapod);
	}

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < 6; ++i) {
			assertEquals(0, hexapod.getLeg(i).p4.z, EPSILON);
		}
	}
}