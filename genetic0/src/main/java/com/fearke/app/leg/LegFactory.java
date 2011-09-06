package com.fearke.app.leg;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.algorithm.Mutate;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotypeFactory;

public class LegFactory implements IPhenotypeFactory<Leg> {

	private Random rnd;

	private int count = 10 * 2;

	private ICrossover crossover = new Crossover(Type.Uniform, 0.80);
	private IMutate mutate = new Mutate(0.05, 0.025);

	public LegFactory() {
		this.rnd = new Random(0);
	}

	@Override
	public Leg create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

	@Override
	public Leg create(final IChromosome c) {
		Leg o = new Leg(c);
		o.update();
		return o;
	}

	private void init(final Chromosome c) {
		int count = c.getCount() / 2;
		double ra = rnd.nextDouble() * Math.PI;
		double rb = rnd.nextDouble() * Math.PI;

		for (int i = 0; i < count; ++i) {
			c.setGene(i * 2, ra);
			c.setGene(i * 2 + 1, rb);
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
