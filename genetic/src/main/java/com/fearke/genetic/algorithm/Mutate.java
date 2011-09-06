package com.fearke.genetic.algorithm;

import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IMutate;

public class Mutate implements IMutate {

	private Random rnd;
	private double probability;
	private double deviation;
	private double min;
	private double max;

	public Mutate(double probability, double deviation) {
		this(probability, deviation, Double.MIN_VALUE, Double.MAX_VALUE);
	}

	public Mutate(double probability, double deviation, double min, double max) {
		this.rnd = new Random();
		this.probability = probability;
		this.deviation = deviation;
		this.min = min;
		this.max = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProbabitliy(final double probability) {
		this.probability = probability;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
				o.setGene(i, value);
			}
		}
	}

}
