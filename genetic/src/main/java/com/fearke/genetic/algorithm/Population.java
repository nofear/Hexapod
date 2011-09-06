package com.fearke.genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.genetic.model.IPhenotypeFactory;
import com.fearke.genetic.model.IPopulation;

public class Population<T extends IPhenotype> implements IPopulation<T> {

	private Random rnd;

	private IPhenotypeFactory<T> factory;

	private boolean stop;

	private int count;
	private int countElite;

	private double[] fitness;
	private List<T> population;
	private List<Double> history;

	/**
	 * Constructor.
	 * 
	 * @param factory
	 *            phenotype factory
	 */
	public Population(final IPhenotypeFactory<T> factory) {
		this.rnd = new Random();
		this.factory = factory;
		this.population = new ArrayList<T>();
		this.history = new ArrayList<Double>();
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
		return history.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Double> getFitnessHistory() {
		return Collections.unmodifiableList(history);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

		double sumFitness = 0;
		for (int i = 0; i < count; ++i) {
			sumFitness += population.get(i).getFitness() * a + b;
			fitness[i] = sumFitness;
		}

		for (int i = 0; i < count; ++i) {
			fitness[i] /= sumFitness;
		}

		ICrossover crossover = factory.getCrossover();
		IMutate mutate = factory.getMutate();

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

		history.add(population.get(0).getFitness());

		//test();
	}

	private void sort() {
		Comparator<IPhenotype> cmp = new Comparator<IPhenotype>() {
			@Override
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

	public void test() {
		int generation = history.size();
		if (generation > 100) {
			double f0 = history.get(generation - 100);
			double f1 = history.get(generation - 1);
			if (Math.abs(f0 - f1) <= 0.0001) {
				stop = true;
			}
		}
	}

}
