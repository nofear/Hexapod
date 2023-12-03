package org.pdeboer.genetic.model;

import java.util.*;

public interface IAlgorithm<T extends IPhenotype> {

	void init(int[] populationSize);

	void start();

	List<IPopulation<T>> getPopulation();
}
