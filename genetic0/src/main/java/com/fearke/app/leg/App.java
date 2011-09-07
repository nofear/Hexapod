package com.fearke.app.leg;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.model.IPhenotypeFactory;
import com.fearke.util.Vector2d;

/**
 * Genetic leg
 */
public class App extends PApplet {

	/** */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private Algorithm<Leg> algorithm;
	private DrawLeg draw;

	public static Vector2d p0 = new Vector2d(25, 100);

	public void setup() {
		IPhenotypeFactory<Leg> factory = new LegFactory();

		algorithm = new Algorithm<Leg>(factory);
		algorithm.init(new int[] { 12, 16, 24 });
		algorithm.start();

		draw = new DrawLeg(this, algorithm);

		size(1024, 800);
		background(0);
	}

	public void draw() {
		if (mousePressed) {
			p0.x = mouseX;
			p0.y = mouseY;
		}

		if (keyPressed) {
			switch (key) {
			case ']':
				Leg.force += 500;
				break;
			case '[':
				Leg.force -= 500;
				break;
			}
		}

		draw.draw();
	}
}