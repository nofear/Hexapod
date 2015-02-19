package com.fearke.app.gait;

import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IMutate;

class MutateGait implements IMutate {

	private Random rnd;

	private double deviation = 10;
	private int min = -50;
	private int max = 50;
	private double probability = 0.1;

	public MutateGait() {
		super();

		this.rnd = new Random(0);
	}

	public void setProbabitliy(double probability) {
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public void mutate(final IChromosome o) {
		final double deviation2 = deviation / 2;
		for (int i = 0; i < o.getCount(); ++i) {
			if (rnd.nextDouble() < probability) {
				if ((i % Ant.size) < Ant.count) {
					double value = o.getGene(i) + rnd.nextDouble() * deviation - deviation2;
					value = Math.min(value, max);
					value = Math.max(value, min);
					o.setGene(i, (int) value);
				} else {
					o.setGene(i, rnd.nextInt(100) < 50 ? 0 : 1);
				}
			}
		}
	}
}