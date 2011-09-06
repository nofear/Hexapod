package com.fearke.genetic.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.fearke.genetic.model.IAlgorithm;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.genetic.model.IPhenotypeFactory;
import com.fearke.genetic.model.IPopulation;

public class Algorithm<T extends IPhenotype> implements IAlgorithm<T> {

	private IPhenotypeFactory<T> factory;
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

	public Algorithm(IPhenotypeFactory<T> factory) {
		this.factory = factory;
		this.population = new ArrayList<IPopulation<T>>();
		this.threads = new ArrayList<Thread>();
	}

	public void init(int[] populationSize) {
		for (int size : populationSize) {
			Population<T> p = new Population<T>(factory);
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
