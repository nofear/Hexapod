package com.fearke.genetic.model;

/**
 * Phenotype factor, creates our little critters, either new ones or new ones
 * based on a given chromosome.
 * 
 * @author Patrick
 * 
 * @param <T>
 *            Phenotype.
 */
public interface IPhenotypeFactory<T extends IPhenotype> {

	/**
	 * Creates a new phenotype.
	 * 
	 * @return phenotype
	 */
	T create();

	/**
	 * Creates a new phenotype with given chromosome.
	 * 
	 * @param c
	 *            chromosome
	 * @return phenotype
	 */
	T create(IChromosome c);

	/**
	 * @return cross over function
	 */
	ICrossover getCrossover();

	/**
	 * @return mutation function
	 */
	IMutate getMutate();
}
