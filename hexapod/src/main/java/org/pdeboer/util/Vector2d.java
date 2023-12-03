package org.pdeboer.util;

import java.io.*;

public class Vector2d extends javax.vecmath.Vector2d {

	@Serial
	private static final long serialVersionUID = 1L;

	public Vector2d() {
		super();
	}

	public Vector2d(
			final double arg0,
			final double arg1) {
		super(arg0, arg1);
	}

	public Vector2d(final Vector2d arg0) {
		super(arg0);
	}

	public static double dot(
			final Vector2d v0,
			final Vector2d v1) {
		return v0.dot(v1);
	}

	public static Vector2d sub(
			final Vector2d v0,
			final Vector2d v1) {
		Vector2d v = new Vector2d(v0);
		v.sub(v1);
		return v;
	}

	public static Vector2d add(
			final Vector2d v0,
			final Vector2d v1) {
		Vector2d v = new Vector2d(v0);
		v.add(v1);
		return v;
	}

	public static Vector2d scale(
			final Vector2d v0,
			final double x) {
		Vector2d v = new Vector2d(v0);
		v.scale(x);
		return v;
	}

	public double distance(final Vector2d v) {
		Vector2d r = new Vector2d(this);
		r.sub(v);
		return r.length();
	}

	public double distanceSquared(final Vector2d v) {
		Vector2d r = new Vector2d(this);
		r.sub(v);
		return r.lengthSquared();
	}

	public Vector2d rotate(final double a) {
		Vector2d r = new Vector2d();
		r.x = Math.cos(a) * x + Math.sin(a) * y;
		r.y = -Math.sin(a) * x + Math.cos(a) * y;
		return r;
	}

	@Override
	public String toString() {
		return "(" + fmt(x) + "," + fmt(y) + ")";
	}

	public static String fmt(double v) {
		return String.format("%.4f", v);
	}
}
