package com.fearke.app.line;

import java.util.List;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Mutate;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.model.ICondition;
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
		ICrossover crossover = new Crossover(Type.OnePoint, 0.90);
		IMutate mutate = new Mutate(0.05, 5);

		ICondition condition = new ICondition() {
			@Override
			public boolean check(List<Double> history, int generation) {
				return history.get(0) == 0 || generation > 10000;
			}
		};

		algorithm = new Algorithm<Line>(factory, crossover, mutate, condition);
		algorithm.init(new int[] { 16, 32, 64, 128 });
		algorithm.start();

		drawLine = new DrawLine(this, algorithm);

		size(800, 600);
		background(0);
	}

	public void draw() {
		drawLine.draw();
	}
}
