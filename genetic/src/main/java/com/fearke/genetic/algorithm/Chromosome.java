package com.fearke.genetic.algorithm;

import com.fearke.genetic.model.IChromosome;

public class Chromosome implements IChromosome {

	private int count;
	private double[] genes;

	public Chromosome(int count) {
		this.count = count;
		this.genes = new double[count];
	}

	private Chromosome(int count, double[] genes) {
		this.count = count;
		this.genes = genes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChromosome clone() {
		return new Chromosome(count, genes.clone());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getGene(int idx) {
		return genes[idx];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGene(int idx, double gene) {
		genes[idx] = gene;
	}

}
