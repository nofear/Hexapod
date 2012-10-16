package com.fearke.genetic.model;

public interface IPhenotype {

	/**
	 * @return chromosome
	 */
	IChromosome getChromosome();

	/**
	 * update fitness.
	 */
	void update();

	/**
	 * @return fitness for this instance.
	 */
	double getFitness();

}
