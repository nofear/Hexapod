package com.fearke.genetic.algorithm;

import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IMutate;

public class Mutate implements IMutate {

	private Random rnd;
	private double probability;
	private int deviation;
	private int min;
	private int max;

	public Mutate(double probability, int deviation) {
		this(probability, deviation, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public Mutate(double probability, int deviation, int min, int max) {
		this.rnd = new Random();
		this.probability = probability;
		this.deviation = deviation;
		this.min = min;
		this.max = max;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setProbabitliy(final double probability) {
		this.probability = probability;
	}

	/**
	 * {@inheritDoc}
	 */

	public double getProbability() {
		return probability;
	}

	/**
	 * {@inheritDoc}
	 */
	public void mutate(final IChromosome o) {
		final double deviation2 = deviation / 2;
		for (int i = 0; i < o.getCount(); ++i) {
			if (rnd.nextDouble() <= probability) {
				double value = o.getGene(i) + rnd.nextDouble() * deviation - deviation2;
				value = Math.min(value, max);
				value = Math.max(value, min);

				// value = rnd.nextInt(max - min) + min;
				o.setGene(i, (int) value);
			}
		}
	}

}
