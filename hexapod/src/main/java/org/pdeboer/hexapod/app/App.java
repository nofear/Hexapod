package org.pdeboer.hexapod.app;

import org.pdeboer.hexapod.*;
import org.pdeboer.util.*;
import processing.core.*;

import java.awt.*;
import java.util.*;

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

	private Body body;

	// ************************* GLOBAL VARIABLES **************************

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}

	public void setup() {
		background(backColor);
		smooth();
		frameRate(FRAME_RATE);

		body = new Body();
		body.init();
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
			body.setCenter(new Vector3d(0, 0, 50));
			body.updateInverse();
			break;

		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
			legIdx = key - '1';
			break;
		case '[':
			body.getCenter().z -= 1;
			body.updateInverse();
			break;
		case ']':
			body.getCenter().z += 1;
			body.updateInverse();
			break;
		case ' ':
			stabilise();
			break;

		}
	}

	@Override
	public void draw() {
		handle_input();

		draw(body);
	}

	private void stabilise() {
		double[] r = body.getRotation();
		if (r[2] != 0) {
			LegConfig lc = body.calculateLegConfig();

			double diff = 0.0000001;
			if (r[2] < -diff) {
				r[2] += diff;
			} else if (r[2] > diff) {
				r[2] -= diff;
			}

			body.setRotation(r);

			body.updateP1();
			for (int i = 0; i < Body.LEG_COUNT; ++i) {
				if (lc.touchGround(i)) {
					body.getLeg(i).updateInverse(r[2]);
				}
			}
		}
	}

	private void draw(Body body) {
		Vector3d center = body.getCenter();
		double[] r = body.getRotation();
		LegConfig lc = body.calculateLegConfig();

		lights();
		background(backColor);

		fill(Color.black.getRGB());

		textAlign(PApplet.LEFT);
		text("body", 0, 20);
		text("yaw:   " + fmtAngle(r[0]), 0, 40);
		text("pitch: " + fmtAngle(r[1]), 0, 60);
		text("roll:  " + fmtAngle(r[2]), 0, 80);

		text("l: " + Arrays.toString(lc.getIndex()), 0, 120);
		text("c:  " + center.toString(), 0, 140);

		float leg_x0 = 250;
		// float leg_y0 = 120;
		float leg_d = 150;
		for (int i = 0; i < Body.LEG_COUNT; ++i) {
			Leg leg = body.getLeg(i);
			text("leg " + i, leg_x0 + i * leg_d, 20);
			text("ra " + fmtAngle(leg.getRa()), leg_x0 + i * leg_d, 40);
			text("rb " + fmtAngle(leg.getRb()), leg_x0 + i * leg_d, 60);
			text("rc " + fmtAngle(leg.getRc()), leg_x0 + i * leg_d, 80);
			text("d  " + fmt(lc.getDistance(i)), leg_x0 + i * leg_d, 100);
		}

		translate(MID_X, HEIGHT / 2 + 50);
		rotateX(-rb + PI / 2);
		rotateZ(-ra);

		fill(100, 150, 100);
		rect(-400, -400, 800, 800);

		DrawBody ds = new DrawBody(body);
		ds.draw(this);
		ds.drawPlane(this);
		ds.drawLegFrame(this);
	}

	private static String fmt(double v) {
		return String.format("%.1f", v);
	}

	private static String fmtAngle(final double v) {
		return String.format("%.1f", v * 180 / PI);
	}
}