package org.pdeboer.util;

import org.apache.commons.geometry.euclidean.threed.*;
import org.apache.commons.geometry.euclidean.threed.rotation.*;

import static org.apache.commons.geometry.euclidean.threed.rotation.QuaternionRotation.*;

public record Rotation3D(
		// clock-wise rotations
		QuaternionRotation rotation
) {

	public static Rotation3D of(
			final double roll,
			final double pitch,
			final double yaw) {
		var qx = fromAxisAngle(Vector3D.of(1, 0, 0), roll);
		var qy = fromAxisAngle(Vector3D.of(0, 1, 0), pitch);
		var qz = fromAxisAngle(Vector3D.of(0, 0, 1), yaw);
		var q = qx.multiply(qy).multiply(qz);
		return new Rotation3D(q);
	}

	// get clock-wise rotations
	public static double[] getAngles(final Vector3d p) {

		var n = p.normalize();

		double yaw = 0;
		double pitch = Math.atan2(n.x(), n.z());
		double roll = Math.asin(-n.y());
		return new double[] { roll, pitch, yaw };
	}

	// rotate clockwise around x-axis
	static Rotation3D getRotateX(final double a) {
		var q = fromAxisAngle(Vector3D.of(1, 0, 0), a);
		return new Rotation3D(q);
	}

	// rotate clockwise around y-axis
	static Rotation3D getRotateY(final double a) {
		var q = fromAxisAngle(Vector3D.of(0, 1, 0), a);
		return new Rotation3D(q);
	}

	// rotate clockwise around z-axis
	static Rotation3D getRotateZ(final double a) {
		var q = fromAxisAngle(Vector3D.of(0, 0, 1), a);
		return new Rotation3D(q);
	}

	public Vector3d apply(final Vector3d v) {
		Vector3D tmp = Vector3D.of(v.x(), v.y(), v.z());
		var r = rotation.apply(tmp);
		return new Vector3d(r.getX(), r.getY(), r.getZ());
	}

	private Rotation3D multiply(final Rotation3D m) {
		return new Rotation3D(rotation.multiply(m.rotation));
	}

	public Rotation3D rotateX(final double a) {
		return multiply(Rotation3D.getRotateX(a));
	}

	public Rotation3D rotateY(final double a) {
		return multiply(Rotation3D.getRotateY(a));
	}

	public Rotation3D rotateZ(final double a) {
		return multiply(Rotation3D.getRotateZ(a));
	}
};
