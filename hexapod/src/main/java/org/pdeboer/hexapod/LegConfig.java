package org.pdeboer.hexapod;

import org.pdeboer.*;
import org.pdeboer.util.*;

import java.util.stream.*;

import static org.pdeboer.hexapod.Hexapod.*;

/**
 * Helper class to calculate if a particular leg configuration is stable.
 */
public class LegConfig {

	private static final double GROUND_EPSILON = 1E-06;

	private final Hexapod hexapod;

	private final Terrain terrain;

	/**
	 * assume these legs are our ground plane.
	 */
	private final int[] index;

	private boolean inside = true;

	public LegConfig(
			final Hexapod hexapod,
			final Terrain terrain,
			final int[] index) {
		this.hexapod = hexapod;
		this.terrain = terrain;
		this.index = index;

		assert (index.length == 3);

		var center = hexapod.center();

		inside = terrain.height(center.x(), center.y()) >= 25;
	}

	public int countStable() {
		if (!inside) {
			return 0;
		}

		boolean distanceNeg = IntStream.range(0, LEG_COUNT)
				.anyMatch(l -> getDistance(l) < -GROUND_EPSILON);
		if (distanceNeg) {
			return 0;
		}

		return (int) IntStream.range(0, LEG_COUNT).boxed()
				.filter(this::touchGround)
				.count();
	}

	public int[] getIndex() {
		return index;
	}

	public boolean touchGround(int idx) {
		double distance = getDistance(idx);
		return Math.abs(distance) <= GROUND_EPSILON;
	}

	public double getDistance(int idx) {
		Vector3d p4 = getP4(idx);
		return p4.z() - terrain.height(p4.x(), p4.y());
	}

	private Vector3d getP4(int idx) {
		return hexapod.getLeg(idx).p4;
	}
}