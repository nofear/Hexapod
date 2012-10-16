package com.fearke.app.leg;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotypeFactory;

public class LegFactory implements IPhenotypeFactory<Leg> {

	private Random rnd;

	private int count = 10 * 2;

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
		o.init();
		return o;
	}

	private void init(final Chromosome c) {
		int count = c.getCount() / 2;
		int ra = rnd.nextInt(Leg.stepSize);
		int rb = rnd.nextInt(Leg.stepSize);

		for (int i = 0; i < count; ++i) {
			c.setGene(i * 2, ra);
			c.setGene(i * 2 + 1, rb);
		}
	}
};
