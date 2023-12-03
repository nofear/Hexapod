package org.pdeboer.hexapod;

import org.pdeboer.util.*;

public class Leg {

	public final static int LENGTH_COXA = 27;
	public final static int LENGTH_TIBIA = 107;
	public final static int LENGTH_FEMUR = 77;

	public Vector3d p1;
	public Vector3d p2;
	public Vector3d p3;
	public Vector3d p4;

	private double ra = Math.PI / 2;
	private double rb = 0;
	private double rc = 0;

	/**
	 * Constructor.
	 */
	public Leg() {
		p1 = new Vector3d();
		p2 = new Vector3d();
		p3 = new Vector3d();
		p4 = new Vector3d();
	}

	/**
	 * Initialize leg start/end points.
	 *
	 * @param v start point
	 * @param x x offset for end point
	 * @param y y offset for end point
	 * @param z z for end point
	 */
	public void init(
			final Vector3d v,
			final double x,
			final double y,
			final double z) {
		p1 = new Vector3d(v);
		p4 = new Vector3d(v);
		p4.x += x;
		p4.y += y;
		p4.z = z;

		updateInverse(0);
	}

	/**
	 * Set leg start point.
	 *
	 * @param p start point
	 */
	public void setP1(final Vector3d p) {
		this.p1 = p;
	}

	/**
	 * Set leg end point.
	 *
	 * @param p end point
	 */
	public void setP4(final Vector3d p) {
		this.p4 = p;
	}

	/**
	 * @param r angle array
	 */
	public void setR(final double[] r) {
		this.ra = r[0];
		this.rb = r[1];
		this.rc = r[2];
	}

	/**
	 * @return angle array
	 */
	public double[] getR() {
		return new double[] { ra, rb, rc };
	}

	public double getRa() {
		return ra;
	}

	public double getRb() {
		return rb;
	}

	public double getRc() {
		return rc;
	}

	/**
	 * Inverse kinematic calculation of the leg. Given the start and end point
	 * of the leg, calculate the angles of the three servos.
	 *
	 * @param roll roll angle of the body
	 */
	public void updateInverse(final double roll) {
		double dx = p4.x - p1.x;
		double dy = p4.y - p1.y;
		double dz = p4.z - p1.z;

		// rotation of the first servo
		ra = Math.atan2(dy, dx);

		// calculate length of rotated y.
		dy /= Math.sin(ra);

		// offset to p2
		dy -= Math.cos(roll) * LENGTH_COXA;
		dz -= Math.sin(roll) * LENGTH_COXA;

		double dyz2 = dy * dy + dz * dz;
		double d2 = Math.sqrt(dyz2);
		double c1 = (LENGTH_FEMUR * LENGTH_FEMUR - LENGTH_TIBIA * LENGTH_TIBIA + dyz2) / (2 * d2);
		// float c2 = d2 - c1;

		double rb1 = Math.acos(c1 / LENGTH_FEMUR);
		double rb2 = Math.asin(dz / d2);
		rb = rb1 + rb2;
		rc = -Math.acos((-LENGTH_FEMUR * LENGTH_FEMUR - LENGTH_TIBIA * LENGTH_TIBIA
				+ dyz2) / (2 * LENGTH_FEMUR * LENGTH_TIBIA));

		// need to subtract the roll
		rb -= roll;
	}

	public void update(final Matrix m) {
		Matrix r = m.rotateZ(ra);
		p2 = new Vector3d(LENGTH_COXA, 0, 0);
		p2 = r.multiply(p2);
		p2.add(p1);

		r = r.rotateY(2 * Math.PI - rb);
		p3 = new Vector3d(LENGTH_FEMUR, 0, 0);
		p3 = r.multiply(p3);
		p3.add(p2);

		r = r.rotateY(2 * Math.PI - rc);
		p4 = new Vector3d(LENGTH_TIBIA, 0, 0);
		p4 = r.multiply(p4);
		p4.add(p3);
	}

}
