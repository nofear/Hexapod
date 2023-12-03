package org.pdeboer.genetic.model;

import java.util.*;

public interface ICondition {

	boolean check(
			List<Double> history,
			int generation);
}
