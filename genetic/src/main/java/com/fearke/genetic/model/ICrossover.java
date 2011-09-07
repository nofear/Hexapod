package com.fearke.genetic.model;

public interface ICrossover {

	/**
	 * @param probability
	 *            set crossover probability
	 */
	void setProbabitliy(double probability);

	/**
	 * @return crossover probability
	 */
	double getProbability();

	/**
	 * @param io1
	 *            chromosome 1
	 * @param io2
	 *            chromosome 2
	 * @return array of two (new) chromosomes
	 */
	IChromosome[] crossover(IChromosome io1, IChromosome io2);

}