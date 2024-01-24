package org.pdeboer.hexapod;

import org.pdeboer.*;
import org.pdeboer.util.*;

import java.util.*;

import static java.lang.Math.*;
import static org.pdeboer.hexapod.Hexapod.*;

@SuppressWarnings("WeakerAccess")
public class Leg {

	static final double GROUND_EPSILON = 1E-06;

	public enum Id {
		RIGHT_FRONT, RIGHT_MID, RIGHT_BACK,
		LEFT_BACK, LEFT_MID, LEFT_FRONT
	}

	final static int STEP_COUNT = 10;
	private final static int LENGTH_COXA = 27;
	private final static int LENGTH_FEMUR = 77;
	private final static int LENGTH_TIBIA = 107;

	private final Terrain terrain;

	private final Id id;

	private final double lengthCoxa;
	private final double lengthFemur;
	private final double lengthTibia;

	private Vector3d offset;

	public Vector3d p1;
	public Vector3d p2;
	public Vector3d p3;
	public Vector3d p4;

	private double ra = PI / 2;
	private double rb = 0;
	private double rc = 0;

	private final double[][] angleRange;

	private boolean isMoving;
	private int moveIndex;
	private Vector3d moveStart;
	private Vector3d moveEnd;

	Leg(
			final Id id,
			final Terrain terrain) {
		this(id, terrain, LENGTH_COXA, LENGTH_FEMUR, LENGTH_TIBIA);
	}

	Leg(
			final Id id,
			final double lengthCoxa,
			final double lengthFemur,
			final double lengthTibia) {
		this(id, (x, y) -> 0, lengthCoxa, lengthFemur, lengthTibia);
	}

	Leg(
			final Id id,
			final Terrain terrain,
			final double lengthCoxa,
			final double lengthFemur,
			final double lengthTibia) {
		this.id = id;
		this.terrain = terrain;
		this.lengthCoxa = lengthCoxa;
		this.lengthFemur = lengthFemur;
		this.lengthTibia = lengthTibia;
		this.isMoving = false;
		this.moveIndex = 0;

		this.angleRange = new double[][] { { -2 * PI, 2 * PI },
										   { -2 * PI, 2 * PI },
										   { -2 * PI, 2 * PI } };

		p1 = new Vector3d();
		p2 = new Vector3d();
		p3 = new Vector3d();
		p4 = new Vector3d();

		moveStart = p4;
		moveEnd = p4;
	}

	Id id() {
		return id;
	}

	public boolean touchGround() {
		double distance = p4.z() - terrain.height(p4.x(), p4.y());
		return distance <= GROUND_EPSILON;
	}

	public Vector3d moveStart() {
		return moveStart;
	}

	public Vector3d moveEnd() {
		return moveEnd;
	}

	public boolean isMoving() {
		return isMoving;
	}

	void startMoving(final Vector3d dst) {
		moveIndex = 0;
		moveStart = new Vector3d(p4);
		moveEnd = dst;

		isMoving = true;
	}

	void update() {
		if (!isMoving) {
			return;
		}

		var mid = Vector3d.lerp(moveStart, moveEnd, 0.5);
		mid = new Vector3d(mid.x(), mid.y(), mid.z() + 40);

		moveIndex++;
		double t = (double) moveIndex / STEP_COUNT;

		var p1 = Vector3d.lerp(moveStart, mid, t);
		var p2 = Vector3d.lerp(mid, moveEnd, t);

		p4 = Vector3d.lerp(p1, p2, t);

		isMoving = !touchGround();

		// System.out.println(String.format("leg=%s, p4=%s", id, p4));
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

	public void setOffset(final Vector3d offset) {
		this.offset = offset;
	}

	public void setAngelRange(
			final int idx,
			final double min,
			final double max) {
		angleRange[idx][0] = Math.toRadians(min);
		angleRange[idx][1] = Math.toRadians(max);
	}

	public void init(final Vector3d v) {
		p1 = new Vector3d(v);

		double x1 = v.x() + offset.x();
		double y1 = v.y() + offset.y();
		p4 = new Vector3d(x1, y1, terrain.height(x1, y1));

		double[] rotation = { 0, 0, 0 };
		updateInverseIK(rotation);
		update(rotation);

		moveStart = p4;
		moveEnd = p4;
	}

	public void initTest(
			final Vector3d v,
			final double x,
			final double y) {
		setOffset(Vector3d.of(x, y, 0));
		init(v);
	}

	void setAngles2(final double[] r) {
		this.ra = r[0];
		this.rb = r[1];
		this.rc = r[2];
	}

	void setAngles(final double[] r) {
		this.ra = limitRange(0, r[0]);
		this.rb = limitRange(1, r[1]);
		this.rc = limitRange(2, r[2]);
	}

	private double limitRange(
			final int idx,
			final double angle) {
		return Math.min(Math.max(angle, angleRange[idx][0]), angleRange[idx][1]);
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
	private void updateInverseIK(double[] bodyRotation) {

		double dx = p4.x() - p1.x();
		double dy = p4.y() - p1.y();
		double dz = p4.z() - p1.z();

		var c1 = Math.atan2(dx, dy);

		var rotation = Rotation3D.of(bodyRotation[ROLL], bodyRotation[PITCH], bodyRotation[YAW]);

		var coxa_t = rotation.rotateZ(-c1).apply(new Vector3d(0, lengthCoxa, 0));

		var vz = rotation.apply(new Vector3d(0, 0, 1));
		var v2 = new Vector3d(dx, dy, 0);

		double rco = -(vz.angle(v2) - PI / 2);
		double m = (dz + coxa_t.z());

		double dy_cy = dy - coxa_t.y();
		double dx_cx = dx - coxa_t.x();
		double k = Math.sqrt(dx_cx * dx_cx + dy_cy * dy_cy);

		double l = Math.sqrt(k * k + m * m);

		double l2 = l * l;
		double lf2 = lengthFemur * lengthFemur;
		double lt2 = lengthTibia * lengthTibia;

		double a1 = Math.atan2(k, -m);
		double a2 = Math.acos((lf2 + l2 - lt2) / (2 * lengthFemur * l));
		double b1 = Math.acos((lf2 + lt2 - l2) / (2 * lengthFemur * lengthTibia));

		// System.out.printf("a1=%f, a2=%f, b1=%f, k_t=%f, m=%f%n", a1, a2, b1, k_t, m);

		ra = c1 - bodyRotation[YAW];
		rb = PI - (a1 + a2 - rco);
		rc = PI - b1;

		// System.out.printf("ra=%f, rb=%f, rc=%f%n", ra, rb, rc);
	}

	void updateInverse(double[] rotation) {
		var dst = new Vector3d(p4);

		update(rotation);

		var temp = 0.1;
		var rnd = new Random();

		var angles = getAngles();

		int count = 0;
		double distance = p4.distanceSquared(dst);

		while (distance > 1E-12 && temp >= 1E-12) {
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
			if (count % 200 == 0) {
				temp /= 2;
			}
		}

		// System.out.printf("ra=%f, rb=%f, rc=%f, count=%d%n", ra, rb, rc, count);
	}

	public void update(final double[] bodyRotation) {

		var r = Rotation3D.of(-bodyRotation[ROLL], -bodyRotation[PITCH], -bodyRotation[YAW]);

		r = r.rotateZ(-ra);
		p2 = new Vector3d(0, lengthCoxa, 0);
		p2 = r.apply(p2);
		p2 = p2.add(p1);

		r = r.rotateX(-rb);
		p3 = new Vector3d(0, 0, lengthFemur);
		p3 = r.apply(p3);
		p3 = p3.add(p2);

		r = r.rotateX(-rc);
		p4 = new Vector3d(0, 0, lengthTibia);
		p4 = r.apply(p4);
		p4 = p4.add(p3);
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
