package org.pdeboer.util;

public class Plane3d {

	private final Vector3d a;
	private final Vector3d b;
	private final Vector3d c;

	public Vector3d n;
	public double D;

	public Plane3d(Vector3d a, Vector3d b, Vector3d c) {
		this.a = a;
		this.b = b;
		this.c = c;

		Vector3d v2 = new Vector3d(b);
		Vector3d v3 = new Vector3d(c);
		v2.sub(a);
		v3.sub(a);
		this.n = new Vector3d();
		this.n.cross(v2, v3);
		this.n.normalize();
		this.D = n.dot(a);
	}

	public Vector3d[] getPoints() {
		return new Vector3d[] { a, b, c };
	}

	Vector3d getNormal() {
		return n;
	}

	public double distance(Vector3d p) {
		return n.dot(p) - D;
	}

	/**
	 * Determines the point on the plane when the given point is projected
	 * perpendicular onto the plane.
	 * 
	 * @param l
	 *            point to project
	 * @return point on the plane
	 */
	public Vector3d project(Vector3d l) {
		Vector3d p = new Vector3d(a);
		p.sub(l);
		double d = p.dot(n) / n.dot(n);

		Vector3d t = new Vector3d(n);
		t.scale(d);
		t.add(l);

		return t;
	}
}
