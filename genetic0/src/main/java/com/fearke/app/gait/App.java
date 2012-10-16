package com.fearke.app.gait;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotypeFactory;

/**
 * Genetic gait
 */
public class App extends PApplet {

	/** */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	private Algorithm<Gait> algorithm;
	private DrawGaitPhenotype draw;

	public void setup() {
		IPhenotypeFactory<Gait> factory = new GaitFactory();
		ICrossover crossover = new Crossover(Type.Uniform, 0.80);
		IMutate mutate = new MutateGait();

		algorithm = new Algorithm<Gait>(factory, crossover, mutate);
		algorithm.init(new int[] { 36 });
		algorithm.start();

		draw = new DrawGaitPhenotype(this, algorithm);

		size(1024, 800);
		background(0);
	}

	public void draw() {
		draw.draw();
	}
}