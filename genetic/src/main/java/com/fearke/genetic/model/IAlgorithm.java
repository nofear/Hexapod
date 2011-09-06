package com.fearke.genetic.model;

import java.util.List;

public interface IAlgorithm<T extends IPhenotype> {

	List<IPopulation<T>> getPopulation();
}
