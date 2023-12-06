package org.pdeboer.hexapod;

import org.pdeboer.util.*;

/**
 * Helper class to calculate if a particular leg configuration is stable.
 */
public class LegConfig {

	/**
	 * rounding error.
	 */
	private static final double EPSILON = 1E-06;

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

	/**
	 * ground plane.
	 */
	private Plane3d plane;

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

		plane = new Plane3d(p1, p2, p3);

		distanceNeg = false;
		distance = new double[Hexapod.LEG_COUNT];
		ground = new boolean[Hexapod.LEG_COUNT];
		for (int l = 0; l < Hexapod.LEG_COUNT; ++l) {
			distance[l] = plane.distance(getP4(l));
			distanceNeg |= (distance[l] < -EPSILON);
			ground[l] = Math.abs(distance[l]) <= EPSILON;
		}

		Vector2d a = new Vector2d(p1.x, p1.y);
		Vector2d b = new Vector2d(p2.x, p2.y);
		Vector2d c = new Vector2d(p3.x, p3.y);

		Plane2d p2d = new Plane2d(a, b, c);
		Vector3d planec = plane.project(hexapod.getCenter());
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

	/**
	 * @param idx leg index
	 * @return distance of leg end point to ground plane.
	 */
	public double getDistance(int idx) {
		return distance[idx];
	}

	/**
	 * @return ground plane
	 */
	public Plane3d getPlane() {
		return plane;
	}

	/**
	 * @param idx leg index
	 * @return leg end point
	 */
	private Vector3d getP4(int idx) {
		return hexapod.getLeg(idx).p4;
	}
}