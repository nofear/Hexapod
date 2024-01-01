package org.pdeboer.util;

import org.apache.commons.math3.linear.*;

public record Matrix(
		RealMatrix matrix
) {

	private final static int SIZE = 3;

	public Matrix() {
		this(new double[SIZE][SIZE]);
	}

	public Matrix(final double[][] elements) {
		this(MatrixUtils.createRealMatrix(elements));
	}

	public double getEntry(
			int row,
			int col) {
		return matrix.getEntry(row, col);
	}

	public Vector3d getRow(int idx) {
		double[] row = matrix.getRow(idx);
		return new Vector3d(row[0], row[1], row[2]);
	}

	public Vector3d getColumn(int idx) {
		double[] row = matrix.getColumn(idx);
		return new Vector3d(row[0], row[1], row[2]);
	}

	// clock-wise rotations
	public static Matrix getMatrix(
			final double roll,
			final double pitch,
			final double yaw) {
		return Matrix.getRotateX(roll).rotateY(pitch).rotateZ(yaw);
	}

	// get clock-wise rotations
	public static double[] getRotation(final Vector3d p) {
		Vector3d n = new Vector3d(p);
		n.normalize();

		double yaw = 0;
		double pitch = Math.atan2(n.x, n.z);
		double roll = Math.asin(-n.y);
		return new double[] { roll, pitch, yaw };
	}

	// rotate clockwise around x-axis
	public static Matrix getRotateX(final double a) {
		var elements = new double[SIZE][SIZE];
		elements[0][0] = 1;
		elements[1][1] = Math.cos(a);
		elements[1][2] = -Math.sin(a);
		elements[2][1] = Math.sin(a);
		elements[2][2] = Math.cos(a);
		return new Matrix(elements);
	}

	// rotate clockwise around y-axis
	public static Matrix getRotateY(final double a) {
		var elements = new double[SIZE][SIZE];
		elements[0][0] = Math.cos(a);
		elements[0][2] = Math.sin(a);
		elements[1][1] = 1;
		elements[2][0] = -Math.sin(a);
		elements[2][2] = Math.cos(a);
		return new Matrix(elements);
	}

	// rotate clockwise around z-axis
	public static Matrix getRotateZ(final double a) {
		var elements = new double[SIZE][SIZE];
		elements[0][0] = Math.cos(a);
		elements[0][1] = -Math.sin(a);
		elements[1][0] = Math.sin(a);
		elements[1][1] = Math.cos(a);
		elements[2][2] = 1;
		return new Matrix(elements);
	}

	public Vector3d multiply(final Vector3d v) {
		var elements = matrix.getData();

		Vector3d r = new Vector3d();
		r.x = elements[0][0] * v.x + elements[0][1] * v.y + elements[0][2] * v.z;
		r.y = elements[1][0] * v.x + elements[1][1] * v.y + elements[1][2] * v.z;
		r.z = elements[2][0] * v.x + elements[2][1] * v.y + elements[2][2] * v.z;
		return r;
	}

	public Matrix multiply(final Matrix m) {
		return new Matrix(matrix.multiply(m.matrix));
	}

	public Matrix rotateX(final double a) {
		return multiply(Matrix.getRotateX(a));
	}

	public Matrix rotateY(final double a) {
		return multiply(Matrix.getRotateY(a));
	}

	public Matrix rotateZ(final double a) {
		return multiply(Matrix.getRotateZ(a));
	}
};
