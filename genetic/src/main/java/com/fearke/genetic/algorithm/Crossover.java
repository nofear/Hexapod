package com.fearke.genetic.algorithm;

import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICrossover;

public class Crossover implements ICrossover {

	public enum Type {
		OnePoint, TwoPoint, Uniform
	};

	private Random rnd;
	private Type type;
	private double probability;

	public Crossover(final Type type, final double probability) {
		this.rnd = new Random();
		this.type = type;
		this.probability = probability;
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
	@Override
	public IChromosome[] crossover(IChromosome o1, IChromosome o2) {
		if (rnd.nextDouble() < probability) {
			switch (type) {
			case OnePoint:
				return crossoverOnePoint(o1, o2);
			case TwoPoint:
				return crossoverTwoPoint(o1, o2);
			case Uniform:
				return crossoverUniform(o1, o2);
			default:
				throw new RuntimeException("Unknown type: " + type);
			}
		} else {
			return crossoverNone(o1, o2);
		}
	}

	private IChromosome[] crossoverNone(IChromosome o1, IChromosome o2) {
		IChromosome c1 = o1.clone();
		IChromosome c2 = o1.clone();
		return new IChromosome[] { c1, c2 };
	}

	private IChromosome[] crossoverOnePoint(IChromosome o1, IChromosome o2) {
		int count = o1.getCount();
		IChromosome c1 = o1.clone();
		IChromosome c2 = o1.clone();
		int index = rnd.nextInt(count);
		for (int i = 0; i < count; ++i) {
			c1.setGene(i, i < index ? o1.getGene(i) : o2.getGene(i));
			c2.setGene(i, i >= index ? o1.getGene(i) : o2.getGene(i));
		}

		return new IChromosome[] { c1, c2 };
	}

	private IChromosome[] crossoverTwoPoint(IChromosome o1, IChromosome o2) {
		int count = o1.getCount();
		IChromosome c1 = o1.clone();
		IChromosome c2 = o1.clone();
		int idx1 = rnd.nextInt(count);
		int idx2 = rnd.nextInt(count);
		if (idx1 > idx2) {
			int idx = idx1;
			idx1 = idx2;
			idx2 = idx;
		}
		for (int i = 0; i < count; ++i) {
			boolean inc = i < idx1 || i >= idx2;
			c1.setGene(i, inc ? o1.getGene(i) : o2.getGene(i));
			c2.setGene(i, !inc ? o1.getGene(i) : o2.getGene(i));
		}

		return new IChromosome[] { c1, c2 };
	}

	private IChromosome[] crossoverUniform(IChromosome o1, IChromosome o2) {
		int count = o1.getCount();
		IChromosome c1 = o1.clone();
		IChromosome c2 = o1.clone();
		for (int i = 0; i < count; ++i) {
			boolean inc = rnd.nextDouble() < 0.5;
			c1.setGene(i, inc ? o1.getGene(i) : o2.getGene(i));
			c2.setGene(i, !inc ? o1.getGene(i) : o2.getGene(i));
		}

		return new IChromosome[] { c1, c2 };
	}
};