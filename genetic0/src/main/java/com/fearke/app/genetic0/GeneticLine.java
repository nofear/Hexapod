package com.fearke.app.genetic0;

import processing.core.PApplet;

import com.fearke.app.line.DrawLine;
import com.fearke.app.line.Line;
import com.fearke.app.line.LineFactory;
import com.fearke.genetic.algorithm.Algorithm;
import com.fearke.genetic.model.IPhenotypeFactory;

/**
 * Genetic line
 */
public class GeneticLine extends PApplet {
	/** */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { GeneticLine.class.getName() });
	}

	private Algorithm<Line> algorithm;
	private DrawLine drawLine;

	public void setup() {

		frameRate(30);

		IPhenotypeFactory<Line> factory = new LineFactory();

		algorithm = new Algorithm<Line>(factory);
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
