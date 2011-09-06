package com.fearke.util;

public class Vector3d extends javax.vecmath.Vector3d {

	private static final long serialVersionUID = 1L;

	public Vector3d() {
		super();
	}

	public Vector3d(double arg0, double arg1, double arg2) {
		super(arg0, arg1, arg2);
	}

	public Vector3d(Vector3d arg0) {
		super(arg0);
	}

	@Override
	public String toString() {
		return "(" + fmt(x) + "," + fmt(y) + "," + fmt(z) + ")";
	}

	public static String fmt(double v) {
		return String.format("%.4f", v);
	}

}
