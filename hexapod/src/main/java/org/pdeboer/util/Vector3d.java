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

	public double distanceSquared(final Vector3d v) {
		var tmp = new Vector3d(this);
		tmp.sub(v);
		return tmp.lengthSquared();
	}

	public Vector3d addEx(final Vector3d v) {
		var tmp = new Vector3d(this);
		tmp.add(v);
		return tmp;
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}

}
