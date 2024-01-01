package org.pdeboer.util;

import java.io.*;

public class Vector2d implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final double x;

	private final double y;

	public Vector2d() {
		this(0, 0);
	}

	public Vector2d(
			final double arg0,
			final double arg1) {
		this.x = arg0;
		this.y = arg1;
	}

	public Vector2d(final Vector2d arg0) {
		this(arg0.x, arg0.y);
	}

	@SuppressWarnings("WeakerAccess")
	public double dot(final Vector2d v1) {
		return this.x * v1.x + this.y * v1.y;
	}

	public Vector2d sub(final Vector2d v1) {
		return new Vector2d(x - v1.x, y - v1.y);
	}

	public Vector2d add(final Vector2d v1) {
		return new Vector2d(x + v1.x, y + v1.y);

	}

	public Vector2d multiply(final double val) {
		return new Vector2d(x * val, y * val);
	}

	public double distance(final Vector2d v) {
		return sub(v).length();
	}

	private double length() {
		return Math.sqrt(lengthSquared());
	}

	public double distanceSquared(final Vector2d v) {
		Vector2d r = new Vector2d(this).sub(v);
		return r.lengthSquared();
	}

	public Vector2d rotate(final double a) {
		Vector2d r = new Vector2d();
		var tx = Math.cos(a) * x + Math.sin(a) * y;
		var ty = -Math.sin(a) * x + Math.cos(a) * y;
		return new Vector2d(tx, ty);
	}

	@Override
	public String toString() {
		return "(" + fmt(x) + "," + fmt(y) + ")";
	}

	public static String fmt(double v) {
		return String.format("%.4f", v);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
}
