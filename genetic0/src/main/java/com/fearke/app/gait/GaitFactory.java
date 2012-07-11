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

	private ICrossover crossover = new Crossover(Type.Uniform, 0.80);
	private IMutate mutate = new Mutate();

	class Mutate implements IMutate {

		private double deviation = Math.PI / 100;
		private double min = -0.3;
		private double max = 0.3;
		private double probability = 0.1;

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
						double value = o.getGene(i) + rnd.nextDouble()
								* deviation - deviation2;
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
		Gait gait = create(c);
		// initGait(gait);
		initGait0(gait);
		gait.update();
		return gait;
	}

	private void initGait0(Gait gait) {
		for (int i = 0; i < Gait.count; ++i) {
			gait.setStep(i, new double[] { 0, 0, 0, 0, 1, 1, 1, 1 });
		}
	}

	private void initGait(Gait gait) {
		double r = 0.6;

		double r0 = rnd.nextDouble() - 0.5;
		double r1 = rnd.nextDouble() - 0.5;
		double r2 = rnd.nextDouble() - 0.5;
		double r3 = rnd.nextDouble() - 0.5;

		int l[] = { 0, 1, 2, 3 };

		double s = r / 4;
		double q = -s / 3;

		int i0 = 0;
		for (int ll : l) {
			for (int i = 0; i < 4; ++i) {
				gait.setStep(i0 + i, new double[] { r0, r1, r2, r3,
						ll == 0 ? 0 : 1, ll == 1 ? 0 : 1, ll == 2 ? 0 : 1,
						ll == 3 ? 0 : 1 });

				r0 += ll == 0 ? s : q;
				r1 += ll == 1 ? s : q;
				r2 += ll == 2 ? s : q;
				r3 += ll == 3 ? s : q;
			}
			i0 += 4;
		}
	}

	@Override
	public Gait create(IChromosome c) {
		Gait gait = new Gait(c);
		gait.update();
		return gait;
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
