package com.fearke.app.leg;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Mutate;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.model.IAlgorithm;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
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

	private IAlgorithm<Leg> algorithm;
	private DrawLeg draw;

	public static Vector2d p0 = new Vector2d(25, 100);

	public void setup() {
		IPhenotypeFactory<Leg> factory = new LegFactory();
		ICrossover crossover = new Crossover(Type.Uniform, 0.80);
		IMutate mutate = new Mutate(0.05, 0.025);

		algorithm = new Algorithm<Leg>(factory, crossover, mutate);
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