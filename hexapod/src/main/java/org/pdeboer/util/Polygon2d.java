package org.pdeboer.util;

import java.util.*;

public class Polygon2d {

	private final List<Vector2d> points;
	private boolean updateArea;
	private double area;
	private Vector2d centroid;

	public Polygon2d() {
		this(new ArrayList<Vector2d>());
	}

	public Polygon2d(final Vector2d[] points) {
		this(Arrays.asList(points));
	}

	public Polygon2d(final List<Vector2d> points) {
		super();
		this.points = points;
		this.updateArea = true;
		this.area = 0;
		this.centroid = null;
	}

	public Vector2d[] getPoints() {
		return points.toArray(new Vector2d[points.size()]);
	}

	public Line2d getEdge(int idx) {
		Vector2d p0 = points.get(idx);
		Vector2d p1 = points.get((idx + 1) % points.size());
		return new Line2d(p0, p1);
	}

	public void add(Vector2d v) {
		points.add(v);
	}

	public Vector2d project(final Vector2d p) {
		double distance = Double.MAX_VALUE;
		Vector2d r = null;
		for (int i = 0; i < points.size(); ++i) {
			Line2d l = getEdge(i);
			Vector2d p2 = l.project(p);

			double d;
			if (l.onSegment(p2)) {
				d = p2.distance(p);
			} else {
				Vector2d p0 = points.get(i);
				d = p0.distance(p);
				p2 = p0;
			}

			if (d < distance) {
				distance = d;
				r = p2;
			}
		}

		return r;
	}

	// cn_PnPoly(): crossing number test for a point in a polygon
	// Input: P = a point,
	// V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
	// Return: 0 = outside, 1 = inside
	// This code is patterned after [Franklin, 2000]
	public boolean inside(final Vector2d P) {
		// the crossing number counter
		int cn = 0;
		// loop through all edges of the polygon
		int size = points.size();
		for (int i = 0; i < size; ++i) { // edge from V[i] to V[i+1]
			Vector2d p0 = points.get(i);
			Vector2d p1 = points.get((i + 1) % size);
			// an upward crossing or a downward crossing
			if (((p0.y <= P.y) && (p1.y > P.y)) || ((p0.y > P.y) && (p1.y <= P.y))) {
				// compute the actual edge-ray intersect x-coordinate
				float vt = (float) ((P.y - p0.y) / (p1.y - p0.y));
				// P.x < intersect
				if (P.x < p0.x + vt * (p1.x - p0.x)) {
					// a valid crossing of y=P.y right of P.x
					++cn;
				}
			}
		}

		return (cn & 1) == 1; // 0 if even (out), and 1 if odd (in)
	}

	public double area() {
		if (updateArea) {
			area = 0;
			int size = points.size();
			for (int i = 0; i < size; ++i) {
				Vector2d p0 = points.get(i);
				Vector2d p1 = points.get((i + 1) % size);

				area += (p0.x * p1.y - p1.x * p0.y);
			}
			area /= 2;
			updateArea = false;
		}

		return area;
	}

	public Vector2d centroid() {
		if (centroid == null) {
			double x = 0;
			double y = 0;
			int size = points.size();
			for (int i = 0; i < size; ++i) {
				Vector2d p0 = points.get(i);
				Vector2d p1 = points.get((i + 1) % size);

				double d = (p0.x * p1.y) - (p1.x * p0.y);
				x += (p0.x + p1.x) * d;
				y += (p0.y + p1.y) * d;
			}

			double area = 1.0 / (6.0 * area());
			x *= area;
			y *= area;

			centroid = new Vector2d(x, y);
		}

		return centroid;
	}
}
