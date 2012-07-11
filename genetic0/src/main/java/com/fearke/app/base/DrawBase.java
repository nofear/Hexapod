package com.fearke.app.base;

import java.text.DecimalFormat;

import com.fearke.util.Vector2d;

import processing.core.PApplet;

public class DrawBase {

	private DecimalFormat decimalFormat;

	protected float height = 200;
	protected float width = 200;

	protected PApplet g;
	protected int sequence;

	protected DrawBase(PApplet g) {
		this.g = g;
		this.sequence = 0;
		this.decimalFormat = new DecimalFormat("##.###");
	}

	protected void vertex(Vector2d p) {
		g.vertex((float) p.x, (float) p.y);
	}

	protected void rect(Vector2d v) {
		rect(v, 5);
	}

	protected void rect(Vector2d v, float size) {
		g.rect((float) v.x - size / 2, (float) v.y - size / 2, size, size);
	}

	protected void line(Vector2d v0, Vector2d v1) {
		g.line((float) v0.x, (float) v0.y, (float) v1.x, (float) v1.y);
	}

	protected void text(double v, double x, double y) {
		g.text(decimalFormat.format(v), (float) x, (float) y);
	}
}
