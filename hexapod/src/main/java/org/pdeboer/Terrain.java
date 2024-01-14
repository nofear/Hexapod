package org.pdeboer;

@FunctionalInterface
public interface Terrain {

	// map 0..49 -> -(resX*MESH_SIZE)/2 -> +(resX*MESH_SIZE)/2
	double height(
			double x,
			double y);

	default double[] angle(
			double x,
			double y) {
		var z0 = height(x, y);
		var zx = height(x + 1, y);
		var zy = height(x, y + 1);

		var pitch = Math.atan2(zx - z0, 1);
		var roll = Math.atan2(zy - z0, 1);

		return new double[] { roll, pitch };
	}
}
