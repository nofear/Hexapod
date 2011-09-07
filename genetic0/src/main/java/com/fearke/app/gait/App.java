package com.fearke.app.gait;

import processing.core.PApplet;

import com.fearke.genetic.algorithm.Algorithm;
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
	private DrawGait draw;

	public void setup() {
		IPhenotypeFactory<Gait> factory = new GaitFactory();

		algorithm = new Algorithm<Gait>(factory);
		algorithm.init(new int[] { 24 });
		algorithm.start();

		draw = new DrawGait(this, algorithm);

		size(1024, 800);
		background(0);
	}

	public void draw() {
		draw.draw();
	}
}