package com.fearke.genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICondition;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.genetic.model.IPhenotypeFactory;
import com.fearke.genetic.model.IPopulation;

public class Population<T extends IPhenotype> implements IPopulation<T> {

	private Random rnd;

	private IPhenotypeFactory<T> factory;
	private ICrossover crossover;
	private IMutate mutate;
	private ICondition condition;

	private boolean stop;

	private int count;
	private int countElite;

	private double[] fitness;
	private List<T> population;
	private List<Double> history;
	private int generation;

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            phenotype factory
	 */
	public Population(final IPhenotypeFactory<T> factory, final ICrossover crossover, final IMutate mutate,
			final ICondition condition) {
		this.rnd = new Random(0);
		this.factory = factory;
		this.crossover = crossover;
		this.mutate = mutate;
		this.condition = condition;
		this.population = new ArrayList<T>();
		this.history = new ArrayList<Double>();
		this.generation = 0;
		this.count = 0;
		this.countElite = 0;
		this.stop = false;
	}

	public void init() {
		population.clear();
		for (int i = 0; i < count; ++i) {
			T phenotype = factory.create();

			population.add(phenotype);
		}
		sort();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCount(int count) {
		this.count = count;
		this.fitness = new double[count];
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCount() {
		return count;
	}

	public void setCountElite(int countElite) {
		this.countElite = countElite;
	}

	public int getCountElite() {
		return countElite;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(int idx) {
		return population.get(idx);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGeneration() {
		return generation;
	}

	/**
	 * {@inheritDoc}
	 */

	public List<Double> getFitnessHistory() {
		synchronized (history) {
			return new ArrayList<Double>(history);
		}
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean stop() {
		return stop;
	}

	/**
	 * {@inheritDoc}
	 */
	public void generate() {
		List<T> newpopulation = new ArrayList<T>(count);
		newpopulation.addAll(population.subList(0, countElite));

		// rescale the fitness, such that the minimum fitness is assigned value
		// 100 and the maximum fitness is assigned value 1.
		final double up = 100;
		final double low = 1;
		double maxFitness = population.get(0).getFitness();
		double minFitness = population.get(population.size() - 1).getFitness();
		double a = (up - low) / (maxFitness - minFitness);
		double b = (up + low) / 2 - ((up - low) / 2) * ((minFitness + maxFitness) / (maxFitness - minFitness));

		for (int i = 0; i < count; ++i) {
			T t = population.get(i);
			t.update();
		}

		double sumFitness = 0;
		for (int i = 0; i < count; ++i) {
			T t = population.get(i);
			sumFitness += t.getFitness() * a + b;
			fitness[i] = sumFitness;
		}

		for (int i = 0; i < count; ++i) {
			fitness[i] /= sumFitness;
		}

		int countCrossover = count - countElite;
		for (int i = 0; i < countCrossover / 2; ++i) {
			int idx1 = Arrays.binarySearch(fitness, rnd.nextDouble());
			int idx2 = Arrays.binarySearch(fitness, rnd.nextDouble());

			IChromosome p1 = population.get(idx1 < 0 ? -idx1 - 1 : idx1).getChromosome();
			IChromosome p2 = population.get(idx2 < 0 ? -idx2 - 1 : idx2).getChromosome();
			IChromosome[] children = crossover.crossover(p1, p2);
			for (IChromosome child : children) {
				mutate.mutate(child);
			}
			for (IChromosome child : children) {
				T phenotype = factory.create(child);
				newpopulation.add(phenotype);
			}
		}

		population = newpopulation;

		sort();

		synchronized (history) {
			history.add(0, population.get(0).getFitness());
			if (history.size() > 1000) {
				history.remove(1000);
			}
		}

		++generation;

		if (condition != null) {
			stop = condition.check(history, generation);
		}
	}

	private void sort() {
		Comparator<IPhenotype> cmp = new Comparator<IPhenotype>() {

			public int compare(IPhenotype o1, IPhenotype o2) {
				if (o1.getFitness() < o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() > o2.getFitness()) {
					return 1;
				} else {
					return 0;
				}
			}

		};
		Collections.sort(population, cmp);
	}

}
