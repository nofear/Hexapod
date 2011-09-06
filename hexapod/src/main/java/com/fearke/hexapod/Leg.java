package com.fearke.hexapod;

import com.fearke.util.Matrix;
import com.fearke.util.Vector3d;

public class Leg {

	public final static int h = 27;
	public final static int t = 107;
	public final static int f = 77;

	public Vector3d p1;
	public Vector3d p2;
	public Vector3d p3;
	public Vector3d p4;

	private double ra = Math.PI / 2;
	private double rb = 0;
	private double rc = 0;

	public Leg() {
		p1 = new Vector3d(0, 0, 0);
		p2 = new Vector3d(0, 0, 0);
		p3 = new Vector3d(0, 0, 0);
		p4 = new Vector3d(0, 0, 0);
	}

	public void init(Vector3d v, double x, double y, double z) {
		p1 = new Vector3d(v);
		p4 = new Vector3d(v);
		p4.x += x;
		p4.y += y;
		p4.z = z;
		updateInverse(0);
	}

	public void setP1(double x, double y, double z) {
		setP1(new Vector3d(x, y, z));
	}

	public void setP1(Vector3d p) {
		this.p1 = p;
	}

	public void setP4(Vector3d p) {
		this.p4 = p;
	}

	public void setR(double[] r) {
		this.ra = r[0];
		this.rb = r[1];
		this.rc = r[2];
	}

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

	public void updateInverse(double roll) {
		double dx = p4.x - p1.x;
		double dy = p4.y - p1.y;
		double dz = p4.z - p1.z;

		// rotation of the first servo
		ra = Math.atan2(dy, dx);

		// calculate length of rotated y.
		dy /= Math.sin(ra);

		// offset to p2
		dy -= Math.cos(roll) * h;
		dz -= Math.sin(roll) * h;

		double dyz2 = dy * dy + dz * dz;
		double d2 = Math.sqrt(dyz2);
		double c1 = (f * f - t * t + dyz2) / (2 * d2);
		// float c2 = d2 - c1;

		double rb1 = Math.acos(c1 / f);
		double rb2 = Math.asin(dz / d2);
		rb = rb1 + rb2;
		rc = -Math.acos((-f * f - t * t + dyz2) / (2 * f * t));

		// need to subtract the roll
		rb -= roll;
	}

	public void update(Matrix m) {
		Matrix r = m.rotateZ(ra);
		p2 = new Vector3d(h, 0, 0);
		p2 = r.multiply(p2);
		p2.add(p1);

		r = r.rotateY(2 * Math.PI - rb);
		p3 = new Vector3d(f, 0, 0);
		p3 = r.multiply(p3);
		p3.add(p2);

		r = r.rotateY(2 * Math.PI - rc);
		p4 = new Vector3d(t, 0, 0);
		p4 = r.multiply(p4);
		p4.add(p3);
	}

}
