package com.fearke.genetic.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.fearke.genetic.model.IAlgorithm;
import com.fearke.genetic.model.ICrossover;
import com.fearke.genetic.model.IMutate;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.genetic.model.IPhenotypeFactory;
import com.fearke.genetic.model.IPopulation;

public class Algorithm<T extends IPhenotype> implements IAlgorithm<T> {

	private IPhenotypeFactory<T> factory;
	private ICrossover crossover;
	private IMutate mutate;

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

	public Algorithm(final IPhenotypeFactory<T> factory, final ICrossover crossover, final IMutate mutate) {
		this.factory = factory;
		this.crossover = crossover;
		this.mutate = mutate;
		this.population = new ArrayList<IPopulation<T>>();
		this.threads = new ArrayList<Thread>();
	}

	@Override
	public void init(final int[] populationSize) {
		for (int size : populationSize) {
			Population<T> p = new Population<T>(factory, crossover, mutate);
			p.setCount(size);
			p.setCountElite(2);
			p.init();
			population.add(p);
		}
	}

	@Override
	public void start() {
		for (IPopulation<T> p : population) {
			Thread thread = new Thread(new RunPopulation(p));
			thread.start();

			this.threads.add(thread);
		}
	}

	@Override
	public List<IPopulation<T>> getPopulation() {
		return population;
	}
}
