package org.pdeboer.util;

public class Matrix {
	final static int size = 3;

	private double[][] elements;

	public Matrix() {
		elements = new double[size][size];
	}

	public double getElement(int row, int col) {
		return elements[row][col];
	}

	public Vector3d getRow(int idx) {
		return new Vector3d(elements[idx][0], elements[idx][1], elements[idx][2]);
	}

	public Vector3d getColumn(int idx) {
		return new Vector3d(elements[0][idx], elements[1][idx], elements[2][idx]);
	}

	public static Matrix getMatrix(double yaw, double pitch, double roll) {
		return Matrix.getRotateZ(yaw).rotateY(pitch).rotateX(roll);
	}

	public static Matrix getMatrix(double[] r) {
		return Matrix.getRotateZ(r[0]).rotateY(r[1]).rotateX(r[2]);
	}

	public static double[] getRotation(Vector3d p) {
		Vector3d n = new Vector3d(p);
		n.normalize();

		double yaw = 0;
		double pitch = Math.atan2(n.x, n.z);
		double roll = Math.asin(-n.y);
		return new double[] { yaw, pitch, roll };
	}

	public static Matrix getMatrix(Vector3d p) {
		return getMatrix(getRotation(p));
	}

	public static Matrix getRotateX(double a) {
		Matrix m = new Matrix();
		m.elements[0][0] = 1;
		m.elements[1][1] = Math.cos(a);
		m.elements[1][2] = -Math.sin(a);
		m.elements[2][1] = Math.sin(a);
		m.elements[2][2] = Math.cos(a);
		return m;
	}

	public static Matrix getRotateY(double a) {
		Matrix m = new Matrix();
		m.elements[0][0] = Math.cos(a);
		m.elements[0][2] = Math.sin(a);
		m.elements[1][1] = 1;
		m.elements[2][0] = -Math.sin(a);
		m.elements[2][2] = Math.cos(a);
		return m;
	}

	public static Matrix getRotateZ(double a) {
		Matrix m = new Matrix();
		m.elements[0][0] = Math.cos(a);
		m.elements[0][1] = -Math.sin(a);
		m.elements[1][0] = Math.sin(a);
		m.elements[1][1] = Math.cos(a);
		m.elements[2][2] = 1;
		return m;
	}

	public Vector3d multiply(Vector3d v) {
		Vector3d r = new Vector3d();
		r.x = elements[0][0] * v.x + elements[0][1] * v.y + elements[0][2] * v.z;
		r.y = elements[1][0] * v.x + elements[1][1] * v.y + elements[1][2] * v.z;
		r.z = elements[2][0] * v.x + elements[2][1] * v.y + elements[2][2] * v.z;
		return r;
	}

	public Matrix multiply(final Matrix m) {
		Matrix nm = new Matrix();
		for (int row = 0; row < size; ++row) {
			double[] er = elements[row];
			for (int col = 0; col < size; ++col) {
				double sum = 0;
				for (int idx = 0; idx < size; ++idx) {
					sum += er[idx] * m.elements[idx][col];
				}
				nm.elements[row][col] = sum;
			}
		}

		return nm;
	}

	public Matrix rotateX(double a) {
		return multiply(Matrix.getRotateX(a));
	}

	public Matrix rotateY(double a) {
		return multiply(Matrix.getRotateY(a));
	}

	public Matrix rotateZ(double a) {
		return multiply(Matrix.getRotateZ(a));
	}
};
