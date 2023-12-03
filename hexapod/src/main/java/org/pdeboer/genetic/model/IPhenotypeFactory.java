package org.pdeboer.genetic.model;

/**
 * Phenotype factor, creates our little critters, either new ones or new ones
 * based on a given chromosome.
 *
 * @param <T> Phenotype.
 * @author Patrick
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
	 * @param c chromosome
	 * @return phenotype
	 */
	T create(IChromosome c);
}
