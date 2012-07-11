package com.fearke.genetic.model;

import java.util.List;

public interface IAlgorithm<T extends IPhenotype> {

	void init(int[] populationSize);

	void start();

	List<IPopulation<T>> getPopulation();
}
