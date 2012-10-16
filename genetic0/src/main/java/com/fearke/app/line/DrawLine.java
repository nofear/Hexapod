package com.fearke.app.line;

import java.text.DecimalFormat;

import processing.core.PApplet;

import com.fearke.app.base.DrawPhenotype;
import com.fearke.genetic.model.IAlgorithm;
import com.fearke.genetic.model.IChromosome;

public class DrawLine extends DrawPhenotype<Line> {

	public DrawLine(PApplet g, IAlgorithm<Line> algorithm) {
		super(g, algorithm);

		this.width = 55;
		this.height = 50;
	}

	protected void draw(Line phenotype) {
		g.noFill();
		g.stroke(255);
		g.rect(0, 0, width, height);

		IChromosome chromosome = phenotype.getChromosome();
		int count = chromosome.getCount();
		for (int i = 0; i < count - 1; ++i) {
			double p0 = chromosome.getGene(i);
			double p1 = chromosome.getGene(i + 1);

			g.line((float) i, 25 - (float) p0, (float) i + 1, 25 - (float) p1);
		}

		g.fill(255);
		g.textAlign(PApplet.RIGHT);
		DecimalFormat df = new DecimalFormat("##.#####");
		g.text(df.format(phenotype.getFitness()), 0, 25 + 23);
	}
}
