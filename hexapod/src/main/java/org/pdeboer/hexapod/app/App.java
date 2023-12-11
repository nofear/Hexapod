package org.pdeboer.hexapod.app;

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
	private final static int HEIGHT = 768;
	private final static int MID_X = WIDTH / 2;
	private final static int MID_Y = HEIGHT / 2;
	private final static int FRAME_RATE = 30;

	private float ra = -PI / 4;
	private float rb = PI / 8;

	private float mx = 0;
	private float my = 0;

	private final int backColor = color(250, 250, 250);

	private Hexapod hexapod;

	// ************************* GLOBAL VARIABLES **************************

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}

	public void setup() {
		background(backColor);
		smooth();
		frameRate(FRAME_RATE);

		hexapod = new Hexapod();
	}

	private void handle_input() {
		if (mousePressed) {
			mx = (float) (mouseX - MID_X) / WIDTH;
			my = (float) (mouseY - MID_Y) / HEIGHT;

			ra = mx * PI;
			rb = my * PI;
		}

		if (!keyPressed) {
			return;
		}

		int legIdx = 0;

		switch (key) {
		case '0':
			hexapod.init();
			break;

		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
			legIdx = key - '1';
			break;
		case 'w':
			hexapod.execute(Action.FORWARD);
			break;
		case 's':
			hexapod.execute(Action.BACKWARD);
			break;
		case 'a':
			hexapod.execute(Action.LEFT);
			break;
		case 'd':
			hexapod.execute(Action.RIGHT);
			break;
		case 'q':
			hexapod.execute(Action.UP);
			break;
		case 'e':
			hexapod.execute(Action.DOWN);
			break;

		case 'j':
			hexapod.execute(Action.ROLL_PLUS);
			break;
		case 'l':
			hexapod.execute(Action.ROLL_MIN);
			break;

		case 'i':
			hexapod.execute(Action.YAW_PLUS);
			break;
		case 'k':
			hexapod.execute(Action.YAW_MIN);
			break;

		case 'u':
			hexapod.execute(Action.PITCH_MIN);
			break;
		case 'o':
			hexapod.execute(Action.PITCH_PLUS);
			break;

		case ' ':
			stabilise();
			break;

		}
	}

	@Override
	public void draw() {
		handle_input();

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
			for (int i = 0; i < Hexapod.LEG_COUNT; ++i) {
				if (lc.touchGround(i)) {
					hexapod.getLeg(i).updateInverse(hexapod.rotation());
				}
			}
		}
	}

	private void draw(Hexapod hexapod) {
		Vector3d center = hexapod.getCenter();
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
		float leg_d = 150;
		for (int i = 0; i < Hexapod.LEG_COUNT; ++i) {
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
		rotateX(-rb + PI / 2);
		rotateZ(-ra);

		fill(100, 150, 100);
		rect(-400, -400, 800, 800);

		var ds = new DrawHexapod(hexapod);
		ds.draw(this);
		//ds.drawPlane(this);
		ds.drawLegFrame(this);
	}

	private static String fmt(final double v) {
		return String.format("%.1f", v);
	}

	private static String fmtAngle(final double v) {
		return String.format("%.1f", v * 180 / PI);
	}
}