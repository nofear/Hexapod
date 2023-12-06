package org.pdeboer.util;

import java.io.*;

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

	@Override
	public String toString() {
		return "(" + fmt(x) + "," + fmt(y) + "," + fmt(z) + ")";
	}

	public static String fmt(final double v) {
		return String.format("%.4f", v);
	}

}
