package org.pdeboer.hexapod;

import org.pdeboer.util.*;

import java.util.stream.*;

import static org.pdeboer.hexapod.Hexapod.*;

/**
 * Helper class to calculate if a particular leg configuration is stable.
 */
public class LegConfig {

	private static final double GROUND_EPSILON = 1E-06;

	private final Hexapod hexapod;

	/**
	 * assume these legs are our ground plane.
	 */
	private final int[] index;

	/**
	 * true if the center of gravity is inside our ground plane.
	 */
	private boolean inside;

	private Plane3d groundPlane;

	public LegConfig(
			final Hexapod hexapod,
			final int[] index) {
		this.hexapod = hexapod;
		this.index = index;

		assert (index.length == 3);

		Vector3d p1 = getP4(index[0]);
		Vector3d p2 = getP4(index[1]);
		Vector3d p3 = getP4(index[2]);

		groundPlane = new Plane3d(p1, p2, p3);

		Vector2d a = new Vector2d(p1.x(), p1.y());
		Vector2d b = new Vector2d(p2.x(), p2.y());
		Vector2d c = new Vector2d(p3.x(), p3.y());

		Plane2d p2d = new Plane2d(a, b, c);
		Vector3d planec = groundPlane.project(hexapod.center());
		Vector2d z = new Vector2d(planec.x(), planec.y());

		inside = p2d.inside(z);
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
		return groundPlane.distance(getP4(idx));
	}

	public Plane3d getGroundPlane() {
		return groundPlane;
	}

	private Vector3d getP4(int idx) {
		return hexapod.getLeg(idx).p4;
	}
}