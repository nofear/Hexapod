package org.pdeboer.util;

import java.io.*;

@SuppressWarnings({ "WeakerAccess", "unused" })
public class Vector3d extends javax.vecmath.Vector3d {

	@Serial
	private static final long serialVersionUID = 1L;

	public Vector3d() {
		super();
	}

	public Vector3d(
			final double arg0,
			final double arg1,
			final double arg2) {
		super(arg0, arg1, arg2);
	}

	public Vector3d(final Vector3d arg0) {
		super(arg0);
	}

	public Vector3d neg() {
		return new Vector3d(-x, -y, -z);
	}

	public double mul(final Vector3d v) {
		return x * v.x + y * v.y + z * v.z;
	}

	@Override
	public String toString() {
		return String.format("(%.1f, %.1f, %.1f)", x, y, z);
	}

	public static String fmt(final double v) {
		return String.format("%.4f", v);
	}

}
