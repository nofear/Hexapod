package com.fearke.app.gait;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.algorithm.Crossover;
import com.fearke.genetic.algorithm.Crossover.Type;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotypeFactory;

public class GaitFactory implements IPhenotypeFactory<Gait> {

	private Random rnd;

	private int count = Gait.count * Ant.size;

	private ICrossover crossover = new Crossover(Type.TwoPoint, 0.80);
	private IMutate mutate = new Mutate();

	class Mutate implements IMutate {

		private double deviation = Math.PI / 100;
		private double min = -Math.PI / 8;
		private double max = Math.PI / 8;
		private double probability = 0.10;

		@Override
		public void setProbabitliy(double probability) {
			this.probability = probability;

		}

		@Override
		public double getProbability() {
			return probability;
		}

		@Override
		public void mutate(final IChromosome o) {
			final double deviation2 = deviation / 2;
			for (int i = 0; i < o.getCount(); ++i) {
				if (rnd.nextDouble() < probability) {
					if ((i % Ant.size) < Ant.count) {
						double value = o.getGene(i) + rnd.nextDouble() * deviation - deviation2;
						value = Math.min(value, max);
						value = Math.max(value, min);
						o.setGene(i, value);
					} else {
						o.setGene(i, rnd.nextDouble() < 0.5 ? 0 : 1);
					}
				}
			}
		}

	}

	public GaitFactory() {
		this.rnd = new Random(0);
	}

	@Override
	public Gait create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

	@Override
	public Gait create(IChromosome c) {
		Gait gait = new Gait(c);
		gait.update();
		return gait;
	}

	private void init(Chromosome c) {
		double pi2 = Math.PI / 2;
		for (int i = 0; i < c.getCount(); ++i) {
			// c.setGene(i, rnd.nextDouble() * pi2 - pi2 / 2);
			c.setGene(i, 0);
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

}
