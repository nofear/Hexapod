package org.pdeboer.genetic.algorithm;

import org.pdeboer.genetic.model.*;

import java.util.*;

public class Algorithm<T extends IPhenotype> implements IAlgorithm<T> {

	private IPhenotypeFactory<T> factory;
	private ICrossover crossover;
	private IMutate mutate;
	private ICondition condition;

	private List<IPopulation<T>> population;
	private List<Thread> threads;

	public class RunPopulation implements Runnable {

		private boolean quit;
		private IPopulation<T> population;

		public RunPopulation(IPopulation<T> population) {
			this.quit = false;
			this.population = population;
		}

		public void quit() {
			quit = true;
		}

		public void run() {
			while (!quit) {
				synchronized (this) {
					population.generate();

					if (population.stop()) {
						quit = true;
					}
				}
			}
		}
	}

	public Algorithm(final IPhenotypeFactory<T> factory, final ICrossover crossover, final IMutate mutate,
			final ICondition condition) {
		this.factory = factory;
		this.crossover = crossover;
		this.mutate = mutate;
		this.condition = condition;
		this.population = new ArrayList<IPopulation<T>>();
		this.threads = new ArrayList<Thread>();
	}

	public void init(final int[] populationSize) {
		for (int size : populationSize) {
			org.pdeboer.genetic.algorithm.Population<T> p = new org.pdeboer.genetic.algorithm.Population<T>(factory, crossover, mutate, condition);
			p.setCount(size);
			p.setCountElite(2);
			p.init();
			population.add(p);
		}
	}


	public void start() {
		for (IPopulation<T> p : population) {
			Thread thread = new Thread(new RunPopulation(p));
			thread.start();

			this.threads.add(thread);
		}
	}

	public List<IPopulation<T>> getPopulation() {
		return population;
	}
}
