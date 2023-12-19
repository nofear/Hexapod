package org.pdeboer.hexapod;

import org.pdeboer.util.*;

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

	/**
	 * distance of each of the legs to the ground plane.
	 */
	private double[] distance;

	/**
	 * true for each leg that touches the ground plane.
	 */
	private boolean[] ground;

	/**
	 * true for each leg that is below the ground plane.
	 */
	private boolean distanceNeg;

	private Plane3d groundPlane;

	public LegConfig(
			final Hexapod hexapod,
			final int[] index) {
		this.hexapod = hexapod;
		this.index = index;
	}

	/**
	 * Update configuration.
	 */
	public void update() {
		assert (index.length == 3);

		Vector3d p1 = getP4(index[0]);
		Vector3d p2 = getP4(index[1]);
		Vector3d p3 = getP4(index[2]);

		groundPlane = new Plane3d(p1, p2, p3);

		distanceNeg = false;
		distance = new double[LEG_COUNT];
		ground = new boolean[LEG_COUNT];
		for (int l = 0; l < LEG_COUNT; ++l) {
			distance[l] = groundPlane.distance(getP4(l));
			distanceNeg |= (distance[l] < -GROUND_EPSILON);
			ground[l] = Math.abs(distance[l]) <= GROUND_EPSILON;
		}

		Vector2d a = new Vector2d(p1.x, p1.y);
		Vector2d b = new Vector2d(p2.x, p2.y);
		Vector2d c = new Vector2d(p3.x, p3.y);

		Plane2d p2d = new Plane2d(a, b, c);
		Vector3d planec = groundPlane.project(hexapod.getCenter());
		Vector2d z = new Vector2d(planec.x, planec.y);

		inside = p2d.inside(z);
	}

	public boolean isStable() {
		return inside && !distanceNeg;
	}

	public int[] getIndex() {
		return index;
	}

	public boolean touchGround(int idx) {
		return ground[idx];
	}

	public double getDistance(int idx) {
		return distance[idx];
	}

	public Plane3d getGroundPlane() {
		return groundPlane;
	}

	private Vector3d getP4(int idx) {
		return hexapod.getLeg(idx).p4;
	}
}