package org.pdeboer.hexapod;

import org.pdeboer.util.*;

import java.util.*;

public class Hexapod {

	public static final double EPSILON = 1E-012;

	// roll: x-axis, pitch: y-axis, yaw: z-axis
	public static final int ROLL = 0;
	public static final int PITCH = 1;
	public static final int YAW = 2;

	public enum Action {
		FORWARD, BACKWARD,
		LEFT, RIGHT,
		UP, DOWN,
		PITCH_PLUS, PITCH_MIN,
		YAW_PLUS, YAW_MIN,
		ROLL_PLUS, ROLL_MIN
	}

	public final static int length = 200;
	public final static int width = 100;
	public final static int height = 50;
	private final static int widthMiddle = 150;
	public final static int LEG_COUNT = 6;

	public final static Vector3d[] offset;

	static {
		offset = new Vector3d[LEG_COUNT];
		offset[0] = new Vector3d(length / 2, +width / 2, -height / 2);
		offset[1] = new Vector3d(0, +widthMiddle / 2, -height / 2);
		offset[2] = new Vector3d(-length / 2, +width / 2, -height / 2);
		offset[3] = new Vector3d(-length / 2, -width / 2, -height / 2);
		offset[4] = new Vector3d(0, -widthMiddle / 2, -height / 2);
		offset[5] = new Vector3d(length / 2, -width / 2, -height / 2);
	}

	private Vector3d center;
	private double[] rotation;

	private final Leg[] legs;

	public Hexapod() {
		legs = new Leg[LEG_COUNT];
		for (int i = 0; i < legs.length; ++i) {
			legs[i] = new Leg();
		}

		init();
	}

	public void init() {
		center = new Vector3d(0, 0, 75);
		rotation = new double[] { 0, 0, 0 };

		initLeg();
		stabilise();
		// update();
	}

	public void execute(final Action action) {
		switch (action) {
		case FORWARD -> center.x++;
		case BACKWARD -> center.x--;
		case LEFT -> center.y--;
		case RIGHT -> center.y++;
		case UP -> center.z++;
		case DOWN -> center.z--;
		case ROLL_MIN -> rotation[ROLL] -= 0.01;
		case ROLL_PLUS -> rotation[ROLL] += 0.01;
		case PITCH_MIN -> rotation[PITCH] -= 0.01;
		case PITCH_PLUS -> rotation[PITCH] += 0.01;
		case YAW_MIN -> rotation[YAW] -= 0.01;
		case YAW_PLUS -> rotation[YAW] += 0.01;
		}

		updateInverse();
	}

	public void setRotation(double[] r) {
		this.rotation = r;
	}

	public double[] rotation() {
		return rotation.clone();
	}

	public double[] getConfig() {
		double[] config = new double[3 * 6];
		int idx = 0;
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = legs[i];
			config[idx++] = leg.getRa();
			config[idx++] = leg.getRb();
			config[idx++] = leg.getRc();
		}
		return config;
	}

	public void setConfig(double[] config) {
		int idx = 0;
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = legs[i];
			leg.setRotation(Arrays.copyOfRange(config, idx, idx + 3));
			idx += 3;
		}
	}

	public Vector3d getCenter() {
		return center;
	}

	public void setCenter(final Vector3d center) {
		this.center = center;
	}

	public Leg getLeg(int index) {
		return legs[index];
	}

	private void initLeg() {

		int x = 0;
		int y = 100;
		int z = 0;

		int off = 50;
		double[][] o = new double[][] {
				{ x + off, y },
				{ x, y },
				{ x - 2 * off, y },
				{ x - 2 * off, -y },
				{ x, -y },
				{ x + off, -y } };
		for (int i = 0; i < LEG_COUNT; ++i) {
			var p1 = new Vector3d(center);
			p1.add(offset[i]);

			legs[i].init(p1, o[i][0], o[i][1], z);
		}
	}

	public void update() {
		Matrix m = updateP1();

		for (Leg leg : legs) {
			leg.update(m);
		}
	}

	public Matrix updateP1() {
		Matrix m = Matrix.getMatrix(rotation);
		for (int i = 0; i < LEG_COUNT; ++i) {
			Vector3d p = m.multiply(offset[i]);
			p.add(center);

			legs[i].p1 = p;
		}
		return m;
	}

	public void stabilise() {
		var legConfig = calculateLegConfig();

		Plane3d p = legConfig.getPlane();
		double distance = p.distance(center);
		double[] r = Matrix.getRotation(p.n);

		center = new Vector3d(center.x, center.y, distance);
		rotation = new double[] { -r[ROLL], -r[PITCH], -r[YAW] };
	}

	public void updateInverse() {
		updateP1();

		for (Leg leg : legs) {
			leg.updateInverse(rotation);
		}

		update();
	}

	/**
	 * @return most stable leg configuration.
	 */
	public LegConfig calculateLegConfig() {
		int[][] indices = {

				{ 0, 1, 3 }, { 0, 1, 4 }, { 0, 1, 5 },

				{ 0, 2, 3 }, { 0, 2, 4 }, { 0, 2, 5 },

				{ 1, 2, 3 }, { 1, 2, 4 }, { 1, 2, 5 },

				{ 0, 3, 4 }, { 0, 3, 5 }, { 0, 4, 5 },

				{ 1, 3, 4 }, { 1, 3, 5 }, { 1, 4, 5 },

				{ 2, 3, 4 }, { 2, 3, 5 }, { 2, 4, 5 } };

		for (int[] index : indices) {
			LegConfig lc = new LegConfig(this, index);
			lc.update();
			if (!lc.isStable()) {
				continue;
			}
			return lc;
		}

		throw new RuntimeException();
	}
}
