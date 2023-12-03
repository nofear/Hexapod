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

	float leg_d = 12;
	float leg_h = 20;

	int t = 0;

	private final int backColor = color(250, 250, 250);

	private final static Random RANDOM = new Random();
	private double[] config = null;
	private int legIdx = 0;

	private boolean stabalize = false;

	// ************************* GLOBAL VARIABLES **************************

	public void settings() {
		size(WIDTH, HEIGHT, P3D);
	}

	public void setup() {
		background(backColor);
		smooth();
		frameRate(FRAME_RATE);

		Body body = new Body();
		body.init();
		config = body.getConfig();
	}

	private void handle_input(final Body body) {
		if (mousePressed) {
			mx = (float) (mouseX - MID_X) / WIDTH;
			my = (float) (mouseY - MID_Y) / HEIGHT;

			ra = mx * PI;
			rb = my * PI;
		}

		if (keyPressed) {
			Leg leg = body.getLeg(legIdx);
			double[] r = leg.getR();

			switch (key) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
				legIdx = key - '1';
				break;
			case '[':
				leg.p4.z += 1;
				leg.updateInverse(body.getRotation()[2]);
				r = leg.getR();
				break;
			case ']':
				leg.p4.z -= 1;
				leg.updateInverse(body.getRotation()[2]);
				r = leg.getR();
				break;
			case 'q':
				r[0] += 0.02;
				break;
			case 'w':
				r[0] -= 0.02;
				break;
			case 'a':
				r[1] += 0.02;
				break;
			case 's':
				r[1] -= 0.02;
				break;
			case 'z':
				r[2] += 0.02;
				break;
			case 'x':
				r[2] -= 0.02;
				break;
			case 'r':
				stabalize = true;
				break;
			case 't':
				stabalize = false;
				break;
			}

			leg.setR(r);
		}
	}

	@Override
	public void draw() {
		Body body = new Body();
		body.setConfig(config);
		body.update();

		handle_input(body);

		body.update();
		body.stabilise();

		if (stabalize) {
			double[] r = body.getRotation();
			if (r[2] != 0) {
				LegConfig lc = body.getLegConfig();

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

		config = body.getConfig();

		draw(body);
	}

	private void draw(Body body) {
		Vector3d center = body.getCenter();
		double[] r = body.getRotation();
		LegConfig lc = body.getLegConfig();

		lights();
		background(backColor);

		fill(Color.black.getRGB());

		textAlign(PApplet.LEFT);
		text("body", 0, 20);
		text("yaw:   " + fmt(r[0]), 0, 40);
		text("pitch: " + fmt(r[1]), 0, 60);
		text("roll:  " + fmt(r[2]), 0, 80);

		text("l: " + Arrays.toString(lc.getIndex()), 0, 120);
		text("c:  " + center.toString(), 0, 140);

		float leg_x0 = 250;
		// float leg_y0 = 120;
		float leg_d = 150;
		for (int i = 0; i < Body.LEG_COUNT; ++i) {
			Leg leg = body.getLeg(i);
			text("leg " + i, leg_x0 + i * leg_d, 20);
			text("ra " + fmt(leg.getRa()), leg_x0 + i * leg_d, 40);
			text("rb " + fmt(leg.getRb()), leg_x0 + i * leg_d, 60);
			text("rc " + fmt(leg.getRc()), leg_x0 + i * leg_d, 80);
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
		return String.format("%.4f", v);
	}
}