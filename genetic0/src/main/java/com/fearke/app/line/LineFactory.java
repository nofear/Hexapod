package com.fearke.app.line;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.algorithm.Mutate;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotypeFactory;

public class LineFactory implements IPhenotypeFactory<Line> {

	private Random rnd;

	private static double maxY = 50;
	private static double maxDiff = 5;

	private int count = 50;

	private ICrossover crossover = new Crossover(Type.Uniform, 0.80);
	private IMutate mutate = new Mutate(0.05, 0.05);

	public LineFactory() {
		this.rnd = new Random();
	}

	@Override
	public Line create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

	@Override
	public Line create(final IChromosome c) {
		Line o = new Line(c);
		o.update();
		return o;
	}

	private void init(final Chromosome c) {
		c.setGene(0, 0);
		for (int i = 1; i < c.getCount(); ++i) {
			double v = c.getGene(i - 1) + rnd.nextDouble() * maxDiff - maxDiff / 2;
			c.setGene(i, Math.min(Math.max(-maxY / 2, v), maxY / 2));
		}
	}

	@Override
	public ICrossover getCrossover() {
		return crossover;
	}

	@Override
	public IMutate getMutate() {
		return mutate;
	}

};
