package org.pdeboer.hexapod;

import org.pdeboer.util.*;

import static org.pdeboer.hexapod.Hexapod.*;

public class Leg {

	private final static int LENGTH_COXA = 27;
	private final static int LENGTH_FEMUR = 77;
	private final static int LENGTH_TIBIA = 107;

	public Vector3d p1;
	public Vector3d p2;
	public Vector3d p3;
	public Vector3d p4;

	private final double lengthCoxa;
	private final double lengthFemur;
	private final double lengthTibia;

	private double ra = Math.PI / 2;
	private double rb = 0;
	private double rc = 0;

	public Leg() {
		this(LENGTH_COXA, LENGTH_FEMUR, LENGTH_TIBIA);
	}

	public Leg(
			double lengthCoxa,
			double lengthFemur,
			double lengthTibia) {
		this.lengthCoxa = lengthCoxa;
		this.lengthFemur = lengthFemur;
		this.lengthTibia = lengthTibia;

		p1 = new Vector3d();
		p2 = new Vector3d();
		p3 = new Vector3d();
		p4 = new Vector3d();
	}

	public double lengthCoxa() {
		return lengthCoxa;
	}

	public double lengthFemur() {
		return lengthFemur;
	}

	public double lengthTibia() {
		return lengthTibia;
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

		double[] rotation = { 0, 0, 0 };
		updateInverse(rotation);
		update(Matrix.getMatrix(rotation));
	}

	public void setP1(final Vector3d p) {
		this.p1 = p;
	}

	public void setP4(final Vector3d p) {
		this.p4 = p;
	}

	public void setRotation(final double[] r) {
		this.ra = r[0];
		this.rb = r[1];
		this.rc = r[2];
	}

	public double[] getRotation() {
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
	 */
	public void updateInverse(double[] rotation) {

		double dx = p4.x - p1.x;
		double dy = p4.y - p1.y;
		double dz = p4.z - p1.z;

		double rcoxa = -rotation[ROLL];
		double coxa_z = Math.sin(rcoxa) * lengthCoxa;
		double coxa_y = Math.cos(rcoxa) * lengthCoxa;

		var ra_t = Math.atan2(Math.abs(dx), Math.abs(dy));

		double coxa_t = coxa_y * Math.cos(ra_t);
		double m = dz + coxa_z;
		double k = (Math.abs(dy) - coxa_t) / Math.cos(ra_t);

		double l = Math.sqrt(k * k + m * m);

		double l2 = l * l;
		double lf2 = lengthFemur * lengthFemur;
		double lt2 = lengthTibia * lengthTibia;

		double a1 = Math.atan2(k, -m);
		double a2 = Math.acos((lf2 + l2 - lt2) / (2 * lengthFemur * l));
		double b1 = Math.acos((lf2 + lt2 - l2) / (2 * lengthFemur * lengthTibia));

		double rfemur = Math.PI - (a1 + a2);

		ra = Math.atan2(dx, dy) + rotation[YAW];
		rb = rfemur - rcoxa;
		rc = Math.PI - b1;

		System.out.println(String.format("ra=%f, rb=%f, rc=%f", ra, rb, rc));
	}

	public void update(final Matrix m) {
		Matrix r = m.rotateZ(-ra);
		p2 = new Vector3d(0, lengthCoxa, 0);
		p2 = r.multiply(p2);
		p2.add(p1);

		r = r.rotateX(-rb);
		p3 = new Vector3d(0, 0, lengthFemur);
		p3 = r.multiply(p3);
		p3.add(p2);

		r = r.rotateX(-rc);
		p4 = new Vector3d(0, 0, lengthTibia);
		p4 = r.multiply(p4);
		p4.add(p3);
	}

}
