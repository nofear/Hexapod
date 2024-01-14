package org.pdeboer.hexapod;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.pdeboer.util.*;

import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.pdeboer.hexapod.Hexapod.*;
import static org.pdeboer.hexapod.LegTest.*;

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

	@ParameterizedTest
	@MethodSource("test_move_provider")
	void test_move(final Vector3d speed) {
		var hexapod = new Hexapod((x, y) -> 0);
		hexapod.setSpeed(speed);

		for (int i = 0; i < LEG_COUNT * Leg.STEP_COUNT; ++i) {
			hexapod.update();
		}

		assertLegsOnGround(hexapod);

		Vector3d offset = speed.multiply(LEG_COUNT * Leg.STEP_COUNT);

		var hexapod0 = new Hexapod((x, y) -> 0);
		assertEqualsVector3D(hexapod0.center().add(offset),
							 hexapod.center());
		for (int i = 0; i < LEG_COUNT; ++i) {
			assertEqualsVector3D(hexapod0.getLeg(i).p4.add(offset),
								 hexapod.getLeg(i).p4);
		}
	}

	static Stream<Arguments> test_move_provider() {
		return Stream.of(
				arguments(new Vector3d(0.5, 0.0, 0)),
				arguments(new Vector3d(0.0, 0.5, 0)),
				arguments(new Vector3d(0.25, 0.25, 0))
		);
	}

	private static void assertLegsOnGround(final Hexapod hexapod) {
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);
			assertEquals(0, leg.p4.z(), Leg.GROUND_EPSILON, leg.id().name());
		}
	}
}