package com.fearke.genetic.model;

public interface IMutate {

	/**
	 * @param probability
	 *            set mutation probability
	 */
	void setProbabitliy(double probability);

	/**
	 * @return mutation probability
	 */
	double getProbability();

	/**
	 * Mutate given chromosome.
	 * 
	 * @param o
	 *            chromosome to mutate
	 */
	void mutate(IChromosome o);

}