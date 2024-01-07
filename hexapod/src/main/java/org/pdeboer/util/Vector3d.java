package org.pdeboer.util;

import org.apache.commons.math3.linear.*;

import java.io.*;
import java.util.*;

@SuppressWarnings({ "WeakerAccess", "unused" })
public class Vector3d implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final double x;

	private final double y;

	private final double z;

	public Vector3d() {
		this(0, 0, 0);
	}

	public Vector3d(
			final double arg0,
			final double arg1,
			final double arg2) {
		this.x = arg0;
		this.y = arg1;
		this.z = arg2;
	}

	public Vector3d(final Vector3d arg0) {
		this(arg0.x, arg0.y, arg0.z);
	}

	public static Vector3d of(
			final double arg0,
			final double arg1,
			final double arg2) {
		return new Vector3d(arg0, arg1, arg2);
	}

	public Vector3d neg() {
		return multiply(-1);
	}

	public double distanceSquared(final Vector3d v) {
		return sub(v).lengthSquared();
	}

	public final double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public Vector3d multiply(final double val) {
		return new Vector3d(x * val, y * val, z * val);
	}

	public double angle(final Vector3d v1) {
		double vDot = dot(v1) / (length() * v1.length());
		if (vDot < -1.0) {
			vDot = -1.0;
		}

		if (vDot > 1.0) {
			vDot = 1.0;
		}

		return Math.acos(vDot);
	}

	public static Vector3d cross(
			final Vector3d v1,
			final Vector3d v2) {
		double x = v1.y * v2.z - v1.z * v2.y;
		double y = v2.x * v1.z - v2.z * v1.x;
		var tz = v1.x * v2.y - v1.y * v2.x;
		return new Vector3d(x, y, tz);
	}

	public Vector3d add(final Vector3d v) {
		return new Vector3d(x + v.x, y + v.y, z + v.z);
	}

	public Vector3d addX(final double dx) {
		return new Vector3d(x + dx, y, z);
	}

	public Vector3d addY(final double dy) {
		return new Vector3d(x, y + dy, z);
	}

	public Vector3d addZ(final double dz) {
		return new Vector3d(x, y, z + dz);
	}

	public Vector3d sub(final Vector3d v) {
		return new Vector3d(x - v.x, y - v.y, z - v.z);
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}

	double dot(final Vector3d v1) {
		return this.x * v1.x + this.y * v1.y + this.z * v1.z;
	}

	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Vector3d vector3d = (Vector3d) o;
		return Double.compare(x, vector3d.x) == 0
				&& Double.compare(y, vector3d.y) == 0
				&& Double.compare(z, vector3d.z) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	public Vector3d normalize() {
		return multiply(1.0 / length());
	}

	public RealVector realVector() {
		return MatrixUtils.createRealVector(new double[] { x, y, z });
	}

}
