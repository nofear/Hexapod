package org.pdeboer.genetic.app.line;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.algorithm.Crossover.*;
import org.pdeboer.genetic.model.*;
import processing.core.*;

import java.util.*;

/**
 * Genetic line
 */
public class App extends PApplet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private Algorithm<Line> algorithm;

	private DrawLine drawLine;

	@Override
	public void settings() {
		size(800, 600);
	}

	@Override
	public void setup() {

		frameRate(30);

		IPhenotypeFactory<Line> factory = new LineFactory();
		ICrossover crossover = new Crossover(Type.OnePoint, 0.90);
		IMutate mutate = new Mutate(0.05, 5);

		ICondition condition = new ICondition() {
			public boolean check(
					List<Double> history,
					int generation) {
				return history.get(0) == 0 || generation > 10000;
			}
		};

		algorithm = new Algorithm<Line>(factory, crossover, mutate, condition);
		algorithm.init(new int[] { 16, 32, 64, 128 });
		algorithm.start();

		drawLine = new DrawLine(this, algorithm);

		background(0);
	}

	public void draw() {
		drawLine.draw();
	}
}
