package com.fearke.genetic.model;

import java.util.List;

public interface ICondition {

	boolean check(List<Double> history, int generation);
}
