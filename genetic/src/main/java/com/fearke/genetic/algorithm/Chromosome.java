package com.fearke.genetic.algorithm;

import com.fearke.genetic.model.IChromosome;

public class Chromosome implements IChromosome {

	private int count;
	private int[] genes;

	public Chromosome(int count) {
		this.count = count;
		this.genes = new int[count];
	}

	private Chromosome(int count, int[] genes) {
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
	public int getGene(int idx) {
		return genes[idx];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGene(int idx, int gene) {
		genes[idx] = gene;
	}

}
