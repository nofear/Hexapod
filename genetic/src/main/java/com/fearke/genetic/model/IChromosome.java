package com.fearke.genetic.model;

public interface IChromosome {

	IChromosome clone();

	int getCount();

	int getGene(int index);

	void setGene(int index, int gene);
}
