package org.pdeboer.genetic.app.leg;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.algorithm.Crossover.*;
import org.pdeboer.genetic.model.*;
import org.pdeboer.util.*;
import processing.core.*;

/**
 * Genetic leg
 */
public class App extends PApplet {

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private DrawLeg draw;

	public static Vector2d p0 = new Vector2d(25, 100);


	@Override
	public void settings() {
		size(800, 600);
	}

	@Override
	public void setup() {
		IPhenotypeFactory<Leg> factory = new LegFactory();
		ICrossover crossover = new Crossover(Type.Uniform, 0.80);
		IMutate mutate = new Mutate(0.02, 5);

		var algorithm = new Algorithm<Leg>(factory, crossover, mutate, null);
		algorithm.init(new int[] { 12, 16 });
		algorithm.start();

		draw = new DrawLeg(this, algorithm);

		background(0);
	}

	public void draw() {
		if (mousePressed) {
			p0 = new Vector2d(mouseX, mouseY);
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