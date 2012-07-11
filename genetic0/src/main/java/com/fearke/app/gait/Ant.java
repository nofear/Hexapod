package com.fearke.app.gait;

import java.util.Arrays;

import com.fearke.util.Polygon2d;
import com.fearke.util.Vector2d;

public class Ant {

	public static int count = 4;
	public static int size = count * 2;

	public static int width = 40;
	public static int height = 30;
	public static int length = 30;

	public double[] rotation;

	public Vector2d[] p0;
	public Vector2d[] p1;
	public boolean[] ground;

	public Polygon2d poly;

	public Ant() {
		rotation = new double[] { 0, 0, 0, 0 };
		ground = new boolean[] { true, true, true, true, };

		double w2 = width / 2;
		double h2 = height / 2;

		p0 = new Vector2d[count];
		p0[0] = new Vector2d(-w2, h2);
		p0[1] = new Vector2d(w2, h2);
		p0[2] = new Vector2d(w2, -h2);
		p0[3] = new Vector2d(-w2, -h2);

		update();
	}

	public int getGroundSet() {
		int g = 0;
		for (int i = 0; i < count; ++i) {
			if (ground[i]) {
				g |= 1 << i;
			}
		}
		return g;
	}

	public Polygon2d getGroundPlane(int set) {
		Polygon2d p = new Polygon2d();
		for (int i = 0; i < count; ++i) {
			if ((set & (1 << i)) != 0) {
				p.add(p1[i]);
			}
		}
		return p;
	}

	public int getGroundCount() {
		int c = 0;
		for (boolean g : ground) {
			if (g) {
				++c;
			}
		}
		return c;
	}

	public Polygon2d getPlane() {
		return poly;
	}

	public void set(double[] c) {
		rotation = Arrays.copyOfRange(c, 0, count);
		for (int i = 0; i < count; ++i) {
			ground[i] = c[i + count] != 0;
		}
	}

	public void update() {
		p1 = new Vector2d[count];
		for (int i = 0; i < count; ++i) {
			double r = rotation[i];
			double a = (i < count / 2 ? r : Math.PI - r);
			if (i < count / 2) {
				p1[i] = Vector2d.add(p0[i], new Vector2d(Math.sin(a) * length,
						length));
			} else {
				p1[i] = Vector2d.add(p0[i], new Vector2d(Math.sin(a) * length,
						-length));
			}
		}

		poly = new Polygon2d();
		for (int i = 0; i < count; ++i) {
			if (ground[i]) {
				poly.add(p1[i]);
			}
		}
	}

}
