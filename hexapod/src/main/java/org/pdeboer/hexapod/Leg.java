package org.pdeboer.hexapod;

import org.pdeboer.util.*;

import java.util.*;

import static java.lang.Math.*;
import static org.pdeboer.hexapod.Hexapod.*;

public class Leg {

	public enum Id {
		RIGHT_FRONT, RIGHT_MID, RIGHT_BACK,
		LEFT_BACK, LEFT_MID, LEFT_FRONT
	}

	public final static int STEP_COUNT = 10;
	private final static int LENGTH_COXA = 27;
	private final static int LENGTH_FEMUR = 77;
	private final static int LENGTH_TIBIA = 107;

	private final Id id;

	private final double lengthCoxa;
	private final double lengthFemur;
	private final double lengthTibia;

	public Vector3d p1;
	public Vector3d p2;
	public Vector3d p3;
	public Vector3d p4;

	private double ra = PI / 2;
	private double rb = 0;
	private double rc = 0;

	private boolean isMoving;
	private int moveIndex;
	private Vector3d moveSource;

	Leg(final Id id) {
		this(id, LENGTH_COXA, LENGTH_FEMUR, LENGTH_TIBIA);
	}

	Leg(
			final Id id,
			double lengthCoxa,
			double lengthFemur,
			double lengthTibia) {
		this.id = id;
		this.lengthCoxa = lengthCoxa;
		this.lengthFemur = lengthFemur;
		this.lengthTibia = lengthTibia;
		this.isMoving = false;
		this.moveIndex = 0;

		p1 = new Vector3d();
		p2 = new Vector3d();
		p3 = new Vector3d();
		p4 = new Vector3d();
	}

	public void startMoving(final Vector3d speed) {
		moveIndex = 0;
		moveSource = new Vector3d(p4);
		isMoving = true;
	}

	public void update(final Vector3d speed) {
		if (!isMoving) {
			return;
		}

		moveIndex++;
		isMoving = moveIndex < STEP_COUNT;

		p4.x += speed.x;
		p4.y += speed.y;
		p4.z = Math.sin(PI * moveIndex / STEP_COUNT) * 20;

		System.out.println(String.format("leg=%s, p4=%s", id, p4));
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
		updateInverseIK(rotation);
		update(rotation);
	}

	void setAngles(final double[] r) {
		this.ra = r[0];
		this.rb = r[1];
		this.rc = r[2];
	}

	double[] getAngles() {
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
	public void updateInverseIK(double[] rotation) {

		double dx = p4.x - p1.x;
		double dy = p4.y - p1.y;
		double dz = p4.z - p1.z;

		var c1 = Math.atan2(dx, dy);

		var matrix = Matrix.getMatrix(rotation[ROLL], rotation[PITCH], rotation[YAW]);

		var coxa_t = matrix.rotateZ(-c1)
				.multiply(new Vector3d(0, lengthCoxa, 0));

		var vz = matrix.multiply(new Vector3d(0, 0, 1));
		var v2 = new Vector3d(dx, dy, 0);

		double rco = -(vz.angle(v2) - PI / 2);
		double m = (dz + coxa_t.z);

		double dy_cy = dy - coxa_t.y;
		double dx_cx = dx - coxa_t.x;
		double k = Math.sqrt(dx_cx * dx_cx + dy_cy * dy_cy);

		double l = Math.sqrt(k * k + m * m);

		double l2 = l * l;
		double lf2 = lengthFemur * lengthFemur;
		double lt2 = lengthTibia * lengthTibia;

		double a1 = Math.atan2(k, -m);
		double a2 = Math.acos((lf2 + l2 - lt2) / (2 * lengthFemur * l));
		double b1 = Math.acos((lf2 + lt2 - l2) / (2 * lengthFemur * lengthTibia));

		// System.out.printf("a1=%f, a2=%f, b1=%f, k_t=%f, m=%f%n", a1, a2, b1, k_t, m);

		ra = c1 - rotation[YAW];
		rb = PI - (a1 + a2 - rco);
		rc = PI - b1;

		System.out.printf("ra=%f, rb=%f, rc=%f%n", ra, rb, rc);
	}

	public void updateInverse(double[] rotation) {
		var dst = new Vector3d(p4);

		update(rotation);

		var temp = 0.1;
		var rnd = new Random();

		var angles = getAngles();

		int count = 0;
		double distance = p4.distanceSquared(dst);

		while (distance > 1E-16) {
			int idx = rnd.nextInt(3);
			double[] anglesTmp = angles.clone();
			anglesTmp[idx] += rnd.nextDouble(-temp, temp);

			setAngles(anglesTmp);
			update(rotation);

			double distanceTmp = p4.distanceSquared(dst);
			if (distance > distanceTmp) {
				angles = anglesTmp;
				distance = distanceTmp;
			}

			count++;
			if (count % 256 == 0) {
				temp /= 2;
			}
		}

		// System.out.printf("ra=%f, rb=%f, rc=%f, count=%d%n", ra, rb, rc, count);
	}

	public void update(final double[] rotation) {

		Matrix m = Matrix.getMatrix(-rotation[ROLL], -rotation[PITCH], -rotation[YAW]);

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

	@Override
	public String toString() {
		return "Leg{" +
				"id=" + id +
				", p1=" + p1 +
				", p4=" + p4 +
				", ra=" + ra +
				", rb=" + rb +
				", rc=" + rc +
				'}';
	}
}
