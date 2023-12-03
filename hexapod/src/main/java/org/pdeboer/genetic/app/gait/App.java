package org.pdeboer.genetic.app.gait;

import org.pdeboer.genetic.algorithm.Algorithm;
import org.pdeboer.genetic.algorithm.Crossover;
import org.pdeboer.genetic.algorithm.Crossover.*;
import org.pdeboer.genetic.model.*;
import processing.core.*;

/**
 * Genetic gait
 */
public class App extends PApplet {

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private Algorithm<Gait> algorithm;
	private DrawGaitPhenotype draw;

	@Override
	public void settings() {
		size(1024, 800);
	}

	@Override
	public void setup() {
		IPhenotypeFactory<Gait> factory = new GaitFactory();
		ICrossover crossover = new Crossover(Type.Uniform, 0.80);
		IMutate mutate = new MutateGait();

		algorithm = new Algorithm<Gait>(factory, crossover, mutate, null);
		algorithm.init(new int[] { 36 });
		algorithm.start();

		draw = new DrawGaitPhenotype(this, algorithm);

		background(0);
	}

	public void draw() {
		draw.draw();
	}
}