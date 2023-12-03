package org.pdeboer.genetic.app.leg;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.model.*;

import java.util.*;

public class LegFactory implements IPhenotypeFactory<Leg> {

	private Random rnd;

	private int count = 10 * 2;

	public LegFactory() {
		this.rnd = new Random(0);
	}

	public Leg create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

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
