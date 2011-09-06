package com.fearke.hexapod;

import com.fearke.util.Plane2d;
import com.fearke.util.Plane3d;
import com.fearke.util.Vector2d;
import com.fearke.util.Vector3d;

public class LegConfig {
	private Body body;

	public int[] index;
	public boolean inside;
	public double[] distance;
	public boolean[] ground;
	public boolean distanceNeg;
	public Plane3d plane;

	public LegConfig(Body body, int[] index) {
		this.body = body;
		this.index = index;
	}

	public void update() {
		Vector3d p1 = getP4(index[0]);
		Vector3d p2 = getP4(index[1]);
		Vector3d p3 = getP4(index[2]);
		plane = new Plane3d(p1, p2, p3);

		distanceNeg = false;
		distance = new double[Body.count];
		ground = new boolean[Body.count];
		for (int l = 0; l < Body.count; ++l) {
			distance[l] = plane.distance(getP4(l));
			distanceNeg |= (distance[l] < -1E-06);
			ground[l] = Math.abs(distance[l]) <= 1E-06;
		}

		Vector2d a = new Vector2d(p1.x, p1.y);
		Vector2d b = new Vector2d(p2.x, p2.y);
		Vector2d c = new Vector2d(p3.x, p3.y);

		Plane2d p2d = new Plane2d(a, b, c);
		Vector3d planec = plane.project(body.getCenter());
		Vector2d z = new Vector2d(planec.x, planec.y);
		inside = p2d.inside(z);
	}

	private Vector3d getP4(int idx) {
		return body.getLeg(idx).p4;
	}
}