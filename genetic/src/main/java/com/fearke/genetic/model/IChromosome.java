package com.fearke.genetic.model;

public interface IChromosome {

	IChromosome clone();

	int getCount();

	double getGene(int index);

	void setGene(int index, double gene);
}
