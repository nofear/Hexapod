package org.pdeboer;

@FunctionalInterface
public interface Terrain {

	// map 0..49 -> -(resX*MESH_SIZE)/2 -> +(resX*MESH_SIZE)/2
	double height(
			double x,
			double y);
}
