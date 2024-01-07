package org.pdeboer.hexapod;

import org.pdeboer.*;
import org.pdeboer.hexapod.Leg.*;
import org.pdeboer.util.*;

import java.util.stream.*;

import static java.util.Comparator.*;

public class Hexapod {

	public static final double EPSILON = 1E-12;

	// roll: x-axis, pitch: y-axis, yaw: z-axis
	public static final int ROLL = 0;
	public static final int PITCH = 1;
	public static final int YAW = 2;

	public enum State {
		STOP,
		WALK
	}

	public enum Action {
		STOP,

		MOVE_FORWARD, MOVE_BACKWARD,
		MOVE_LEFT, MOVE_RIGHT,

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
	public final static int LEG_COUNT = Id.values().length;

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

	// anti-clockwise rotation.
	private double[] rotation;

	private final Leg[] legs;

	private State state;
	private Vector3d speed;

	private int legIndex;

	private int stepIndex;

	private final Terrain terrain;

	public Hexapod(final Terrain terrain) {
		this.terrain = terrain;

		this.legs = Stream.of(Leg.Id.values())
				.map(Leg::new)
				.toArray(Leg[]::new);

		this.legIndex = -1;
		this.stepIndex = 0;

		init();
	}

	public void init() {
		this.center = new Vector3d(-50, 0, 75);
		this.rotation = new double[] { 0, 0, 0 };

		this.state = State.STOP;
		this.speed = new Vector3d();

		initLeg();
	}

	public void startMoving(final Vector3d speed) {
		this.state = State.WALK;
		this.speed = speed;
		this.legIndex = 0;

	}

	public void update() {
		switch (state) {
		case STOP -> updateIdle();
		case WALK -> updateWalk();
		}
	}

	private void updateIdle() {

		boolean update = false;
		for (Leg leg : legs) {

			double x = leg.p4.x();
			double y = leg.p4.y();
			double z = terrain.height(x, y);

			if (Math.abs(leg.p4.z() - z) < 0.1) {
				leg.p4 = new Vector3d(leg.p4.x(), leg.p4.y(), z);
				continue;
			}

			if (leg.p4.z() < z) {
				leg.p4 = leg.p4.add(new Vector3d(0, 0, 0.1));
			}

			if (leg.p4.z() > z) {
				leg.p4 = leg.p4.sub(new Vector3d(0, 0, 0.1));
			}

			update = true;
		}

		if (update) {
			updateInverse();
		}
	}

	private void updateWalk() {
		stepIndex++;

		stepIndex %= Leg.STEP_COUNT * 6;

		if (stepIndex % Leg.STEP_COUNT == 0) {
			int legIndex = stepIndex / Leg.STEP_COUNT;

			var leg = getLeg(legIndex);
			leg.startMoving(speed);
		}

		center = center.add(speed);

		updateP1();

		for (Leg leg : legs) {
			leg.update(speed.multiply(6));
		}

		updateInverse();
	}

	public void execute(final Action action) {
		switch (action) {
		case STOP -> setSpeed(new Vector3d());
		case MOVE_FORWARD -> setSpeed(speed.add(new Vector3d(0.1, 0, 0)));
		case MOVE_BACKWARD -> setSpeed(speed.add(new Vector3d(-0.1, 0, 0)));
		case MOVE_RIGHT -> setSpeed(speed.add(new Vector3d(0, 0.1, 0)));
		case MOVE_LEFT -> setSpeed(speed.add(new Vector3d(0, -0.1, 0)));

		case FORWARD -> center = new Vector3d(center.x() + 1, center.y(), center.z());
		case BACKWARD -> center = new Vector3d(center.x() - 1, center.y(), center.z());
		case LEFT -> center = new Vector3d(center.x(), center.y() + 1, center.z());
		case RIGHT -> center = new Vector3d(center.x(), center.y() - 1, center.z());
		case UP -> center = new Vector3d(center.x(), center.y(), center.z() + 1);
		case DOWN -> center = new Vector3d(center.x(), center.y(), center.z() - 1);

		case ROLL_MIN -> rotation[ROLL] -= 0.01;
		case ROLL_PLUS -> rotation[ROLL] += 0.01;
		case PITCH_MIN -> rotation[PITCH] -= 0.01;
		case PITCH_PLUS -> rotation[PITCH] += 0.01;
		case YAW_MIN -> rotation[YAW] -= 0.01;
		case YAW_PLUS -> rotation[YAW] += 0.01;
		}

		updateInverse();
	}

	public void setCenter(final Vector3d center) {
		this.center = center;
	}

	public Vector3d center() {
		return center;
	}

	public void setSpeed(final Vector3d speed) {
		this.speed = speed;
		this.state = speed.lengthSquared() > 0
					 ? State.WALK
					 : State.STOP;
	}

	public Vector3d speed() {
		return speed;
	}

	public void setRotation(double[] r) {
		this.rotation = r;
	}

	public double[] rotation() {
		return rotation.clone();
	}

	public Rotation3D rotationMatrix() {
		return Rotation3D.of(-rotation[ROLL],
							 -rotation[PITCH],
							 -rotation[YAW]);
	}

	public Leg getLeg(int index) {
		return legs[index];
	}

	private void initLeg() {

		int x = 0;
		int y = 80;

		int off = 50;
		double[][] o = new double[][] {
				{ x + off, y },
				{ x, y * 1.25 },
				{ x - off, y },
				{ x - off, -y },
				{ x, -y * 1.25 },
				{ x + off, -y } };
		for (int i = 0; i < LEG_COUNT; ++i) {
			var p1 = center.add(offset[i]);
			double x1 = o[i][0];
			double y1 = o[i][1];
			legs[i].init(p1, x1, y1, terrain.height(x1, y1));
		}
	}

	public void updateForward() {
		updateP1();

		for (Leg leg : legs) {
			leg.update(rotation);
		}
	}

	public void updateP1() {
		var rotation = rotationMatrix();
		for (int i = 0; i < LEG_COUNT; ++i) {
			Vector3d p = rotation.apply(offset[i]);
			legs[i].p1 = center.add(p);
		}
	}

	public void stabilise() {
		var legConfig = calculateLegConfig();

		Plane3d p = legConfig.getGroundPlane();
		double distance = p.distance(center);
		double[] r = Rotation3D.getAngles(p.n);

		center = new Vector3d(center.x(), center.y(), distance);
		rotation = new double[] { -r[ROLL], -r[PITCH], -r[YAW] };
	}

	public void updateInverse() {
		updateP1();

		Stream.of(legs).parallel()
				.forEach(leg -> leg.updateInverse(rotation));

		updateForward();
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

		return Stream.of(indices)
				.map(index -> new LegConfig(this, index))
				.max(comparing(LegConfig::countStable))
				.orElseThrow(() -> new IllegalStateException("no stable leg configuration found"));

	}
}
