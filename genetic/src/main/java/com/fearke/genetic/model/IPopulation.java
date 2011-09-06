package com.fearke.genetic.model;

import java.util.List;

public interface IPopulation<T extends IPhenotype> {

	/**
	 * @return number of phenotypes in population
	 */
	int getCount();

	/**
	 * Set number of phenotypes in population.
	 * 
	 * @param count
	 *            set count
	 */
	void setCount(int count);

	/**
	 * @param idx
	 *            index
	 * @return phenotype at index
	 */
	T get(int idx);

	/**
	 * Generate new generation
	 */
	void generate();

	/**
	 * @return true of stop conditions have been met
	 */
	boolean stop();

	/**
	 * @return current generation
	 */
	int getGeneration();

	/**
	 * @return list of best fitness per generation
	 */
	List<Double> getFitnessHistory();

}
