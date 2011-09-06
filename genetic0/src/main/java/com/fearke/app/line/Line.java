package com.fearke.app.line;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotype;

public class Line implements IPhenotype {

	private IChromosome chromosome;
	private double fitness;

	public Line(IChromosome chromosome) {
		super();
		this.chromosome = chromosome;
		this.fitness = 0;
	}

	public IChromosome getChromosome() {
		return chromosome;
	}

	public double getFitness() {
		return fitness;
	}

	public void update() {
		fitness = 0;
		int count = chromosome.getCount();
		double p0 = 0;
		for (int i = 0; i < count; ++i) {
			double p1 = chromosome.getGene(i);
			double d = Math.abs(p1 - p0);
			fitness += d * d;
			p1 = p0;
		}
	}
}
