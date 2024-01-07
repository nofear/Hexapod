package org.pdeboer.hexapod.app;

import org.pdeboer.*;
import org.pdeboer.hexapod.*;
import org.pdeboer.util.*;
import processing.core.*;

import java.awt.*;
import java.util.*;

import static org.pdeboer.hexapod.Hexapod.*;

public class App extends PApplet {

	public static void main(String[] args) {
		PApplet.main(new String[] { App.class.getName() });
	}

	// ************************* DRAWING VARIABLES **************************

	private final static int WIDTH = 1280;
	private final static int HEIGHT = 800;
	private final static int MID_X = WIDTH / 2;
	private final static int MID_Y = HEIGHT / 2;
	private final static int FRAME_RATE = 60;

	private double ra = -PI / 4;
	private double rb = PI / 8;

	private boolean controlFrame = false;
	private boolean controlBody = false;
	private boolean controlLeg = false;
	private int controlLegIndex = 0;

	private double ra0 = 0;
	private double rb0 = 0;

	private double mx0 = 0;
	private double my0 = 0;

	private final int backColor = color(250, 250, 250);

	private Hexapod hexapod;

	private Terrain terrain;

	// ************************* GLOBAL VARIABLES **************************

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}

	public void setup() {
		background(backColor);
		smooth();
		frameRate(FRAME_RATE);

		hexapod = new Hexapod();
		terrain = new Terrain(0.01);
	}

	@Override
	public void mousePressed() {

		if (keyCode == SHIFT) {
			if (!controlFrame) {
				controlLeg = true;

				mx0 = mouseX;
				my0 = mouseY;
				ra0 = hexapod.getLeg(controlLegIndex).p4.x();
				rb0 = hexapod.getLeg(controlLegIndex).p4.y();
			}

			return;
		}

		if (keyCode == ALT) {
			if (!controlBody) {
				controlBody = true;

				mx0 = mouseX;
				my0 = mouseY;

				ra0 = hexapod.rotation()[ROLL];
				rb0 = hexapod.rotation()[PITCH];
			}

			return;
		}

		if (!controlFrame) {
			controlFrame = true;

			mx0 = mouseX;
			my0 = mouseY;

			ra0 = ra;
			rb0 = rb;
		}
	}

	@Override
	public void mouseDragged() {
		if (controlLeg) {
			int leg_x = (int) (ra0 + (mouseX - mx0));
			int leg_y = (int) (rb0 + (mouseY - my0));

			Leg leg = hexapod.getLeg(controlLegIndex);
			leg.p4 = new Vector3d(leg_x, leg_y, leg.p4.z());

			hexapod.updateInverse();

			return;
		}

		if (controlBody) {
			double roll = ra0 + ((mouseX - mx0) / WIDTH * PI);
			double pitch = rb0 + ((mouseY - my0) / HEIGHT * PI);

			hexapod.setRotation(new double[] { roll, pitch, hexapod.rotation()[YAW] });
			hexapod.updateInverse();
		}

		if (controlFrame) {
			ra = ra0 + ((mouseX - mx0) / WIDTH * PI);
			rb = rb0 + ((mouseY - my0) / HEIGHT * PI);
		}

	}

	@Override
	public void mouseReleased() {
		controlFrame = false;
		controlBody = false;
		controlLeg = false;
	}

	public void checkKeyPressed() {
		if (!keyPressed) {
			return;
		}

		switch (keyCode) {
		case UP -> hexapod.execute(Action.MOVE_FORWARD);
		case DOWN -> hexapod.execute(Action.MOVE_BACKWARD);
		case RIGHT -> hexapod.execute(Action.MOVE_RIGHT);
		case LEFT -> hexapod.execute(Action.MOVE_LEFT);
		}

		switch (key) {
		case '.' -> hexapod.execute(Action.STOP);
		case '0' -> hexapod.init();
		case '1', '2', '3', '4', '5', '6' -> controlLegIndex = key - '1';
		case 'w' -> hexapod.execute(Action.FORWARD);
		case 's' -> hexapod.execute(Action.BACKWARD);
		case 'a' -> hexapod.execute(Action.LEFT);
		case 'd' -> hexapod.execute(Action.RIGHT);
		case 'q' -> hexapod.execute(Action.UP);
		case 'e' -> hexapod.execute(Action.DOWN);
		case 'j' -> hexapod.execute(Action.ROLL_PLUS);
		case 'l' -> hexapod.execute(Action.ROLL_MIN);
		case 'i' -> hexapod.execute(Action.YAW_PLUS);
		case 'k' -> hexapod.execute(Action.YAW_MIN);
		case 'u' -> hexapod.execute(Action.PITCH_MIN);
		case 'o' -> hexapod.execute(Action.PITCH_PLUS);
		}
	}

	@Override
	public void draw() {
		checkKeyPressed();

		hexapod.update();

		draw(hexapod);
	}

	private void stabilise() {
		double[] r = hexapod.rotation();
		if (r[2] != 0) {
			LegConfig lc = hexapod.calculateLegConfig();

			double diff = 0.0000001;
			if (r[2] < -diff) {
				r[2] += diff;
			} else if (r[2] > diff) {
				r[2] -= diff;
			}

			hexapod.setRotation(r);

			hexapod.updateP1();
			for (int i = 0; i < LEG_COUNT; ++i) {
				if (lc.touchGround(i)) {
					hexapod.getLeg(i).updateInverse(hexapod.rotation());
				}
			}
		}
	}

	private void draw(final Hexapod hexapod) {
		Vector3d center = hexapod.center();
		double[] r = hexapod.rotation();
		LegConfig lc = hexapod.calculateLegConfig();

		lights();
		background(backColor);

		fill(Color.black.getRGB());

		int x0 = 10;
		textAlign(PApplet.LEFT);
		text("body", 10, 20);
		text("roll:  " + fmtAngle(r[ROLL]), x0, 40);
		text("pitch: " + fmtAngle(r[PITCH]), x0, 60);
		text("yaw:   " + fmtAngle(r[YAW]), x0, 80);

		text("l: " + Arrays.toString(lc.getIndex()), x0, 120);
		text("c:  " + center.toString(), x0, 140);
		text("r: " + Arrays.toString(hexapod.rotation()), x0, 160);

		float leg_x0 = 250;
		// float leg_y0 = 120;
		float leg_d = 180;
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);
			text("leg " + i, leg_x0 + i * leg_d, 20);
			text("ra " + fmtAngle(leg.getRa()), leg_x0 + i * leg_d, 40);
			text("rb " + fmtAngle(leg.getRb()), leg_x0 + i * leg_d, 60);
			text("rc " + fmtAngle(leg.getRc()), leg_x0 + i * leg_d, 80);
			text("d  " + fmt(lc.getDistance(i)), leg_x0 + i * leg_d, 100);
			text("p1 " + leg.p1.toString(), leg_x0 + i * leg_d, 120);
			text("p4 " + leg.p4.toString(), leg_x0 + i * leg_d, 140);
		}

		translate(MID_X, HEIGHT / 2 + 50);
		rotateX((float) -rb + PI / 2);
		rotateZ((float) -ra);

		var dt = new DrawTerrain(terrain);
		dt.draw(this);

		// fill(100, 150, 100);
		// rect(-400, -400, 800, 800);

		var ds = new DrawHexapod(hexapod);
		ds.draw(this);
		// ds.drawPlane(this);
		ds.drawLegFrame(this);
	}

	private static String fmt(final double v) {
		return String.format("%.1f", v);
	}

	private static String fmtAngle(final double v) {
		return String.format("%.1f", v * 180 / PI);
	}
}