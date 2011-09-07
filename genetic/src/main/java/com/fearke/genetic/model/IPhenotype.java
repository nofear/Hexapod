package com.fearke.genetic.model;

public interface IPhenotype {

	/**
	 * @return chromosome
	 */
	IChromosome getChromosome();

	/**
	 * @return fitness for this instance.
	 */
	double getFitness();

}
