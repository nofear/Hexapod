package org.pdeboer.util;

public class Line2d {

	private final Vector2d a;
	private final Vector2d b;

	public Line2d(
			Vector2d a,
			Vector2d b) {
		super();
		this.a = a;
		this.b = b;
	}

	public Vector2d[] getPoints() {
		return new Vector2d[] { a, b };
	}

	public Vector2d project(final Vector2d p) {
		Vector2d d = b.sub(a);
		double u = p.sub(a).dot(d) / d.lengthSquared();

		Vector2d r = a.add(d.multiply(u));

		return r;
	}

	/**
	 * Distance between the line and a point
	 *
	 * @param p point
	 * @return distance
	 */
	public double distance(final Vector2d p) {
		double u = Math.abs((b.x() - a.x()) * (a.y() - p.y()) - (a.x() - p.x()) * (b.y() - a.y()));
		u /= a.distance(b);
		return u;
	}

	// orientation(): tests if a point is Left|On|Right of an infinite line.
	// Input: three points P0, P1, and P2
	// Return: >0 for P2 left of the line through P0 and P1
	// =0 for P2 on the line
	// <0 for P2 right of the line
	// See: the January 2001 Algorithm
	// "Area of 2D and 3D Triangles and Polygons"
	private double orientation(final Vector2d p) {
		return ((b.x() - a.x()) * (p.y() - a.y()) - (p.x() - a.x()) * (b.y() - a.y()));
	}

	public boolean onSegment(final Vector2d p) {
		if (orientation(p) > 1e-08) {
			return false;
		} else {
			if (a.x() != b.x()) {
				return (Math.min(a.x(), b.x()) <= p.x()) && (p.x() <= Math.max(a.x(), b.x()));
			} else {
				return (Math.min(a.y(), b.y()) <= p.y()) && (p.y() <= Math.max(a.y(), b.y()));
			}
		}
	}
}
