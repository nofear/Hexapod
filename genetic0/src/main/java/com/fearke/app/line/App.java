package com.fearke.app.line;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Mutate;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotypeFactory;

/**
 * Genetic line
 */
public class App extends PApplet {
	/** */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private Algorithm<Line> algorithm;
	private DrawLine drawLine;

	public void setup() {

		frameRate(30);

		IPhenotypeFactory<Line> factory = new LineFactory();
		ICrossover crossover = new Crossover(Type.Uniform, 0.80);
		IMutate mutate = new Mutate(0.05, 0.05);

		algorithm = new Algorithm<Line>(factory, crossover, mutate);
		algorithm.init(new int[] { 16, 32, 64, 128, 256 });
		algorithm.start();

		drawLine = new DrawLine(this, algorithm);

		size(800, 600);
		background(0);
	}

	public void draw() {
		drawLine.draw();
	}
}
