package com.fearke.hexapod.app;

import com.fearke.hexapod.Body;
import com.fearke.hexapod.Leg;
import com.fearke.hexapod.LegConfig;
import com.fearke.util.Matrix;
import com.fearke.util.Plane3d;
import com.fearke.util.Vector3d;

import static processing.core.PConstants.QUADS;
import java.awt.Color;
import processing.core.PApplet;

public class DrawBody {

	private static final int jointSize = 6;

	private Body body;

	public DrawBody(Body body) {
		this.body = body;
	}

	public void draw(PApplet g) {
		Vector3d c = body.getCenter();

		g.pushMatrix();
		g.fill(Color.gray.getRGB());
		g.translate((float) c.x, (float) c.y, (float) c.z);
		g.sphere(4);
		g.popMatrix();

		drawBody(g);
		drawLegs(g);
	}

	private void drawBody(PApplet g) {
		g.stroke(0);
		g.pushMatrix();
		g.fill(240);
		g.noFill();

		Vector3d p[] = new Vector3d[12];

		// top plate
		for (int i = 0; i < 6; ++i) {
			p[i] = new Vector3d();
			p[i].add(Body.offset[i]);
			p[i].z = Body.h / 2;
		}

		// bottom plate
		for (int i = 0; i < 6; ++i) {
			p[i + 6] = new Vector3d();
			p[i + 6].add(Body.offset[i]);
			p[i + 6].z = -Body.h / 2;
		}

		Matrix r = Matrix.getMatrix(body.getRotation());
		for (int i = 0; i < p.length; ++i) {
			p[i] = r.multiply(p[i]);
			p[i].add(body.getCenter());
		}

		g.beginShape();
		for (int i = 0; i < 6; ++i) {
			vertex(g, p[i]);
		}
		g.endShape();

		g.beginShape();
		for (int i = 6; i < 12; ++i) {
			vertex(g, p[i]);
		}
		g.endShape();

		g.beginShape(QUADS);
		for (int i = 0; i < 5; ++i) {
			int i0 = i;
			int i1 = i0 + 1;
			int i2 = (i0 + 7) % 12;
			int i3 = (i0 + 6) % 12;
			vertex(g, p[i0]);
			vertex(g, p[i1]);
			vertex(g, p[i2]);
			vertex(g, p[i3]);
		}

		int i0 = 0;
		int i1 = 5;
		int i2 = 11;
		int i3 = 6;
		vertex(g, p[i0]);
		vertex(g, p[i1]);
		vertex(g, p[i2]);
		vertex(g, p[i3]);
		g.endShape();

		g.popMatrix();
	}

	private void drawLegs(PApplet g) {
		LegConfig lc = body.getLegConfig();
		for (int i = 0; i < Body.count; ++i) {
			g.fill(lc.ground[i] ? Color.green.getRGB() : Color.orange.getRGB());

			drawJoints(g, i);
			drawLeg(g, i);
		}
	}

	private void drawJoints(PApplet g, int index) {
		Leg leg = body.getLeg(index);

		g.noStroke();
		g.pushMatrix();
		g.translate((float) leg.p1.x, (float) leg.p1.y, (float) leg.p1.z);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		g.translate((float) leg.p2.x, (float) leg.p2.y, (float) leg.p2.z);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		g.translate((float) leg.p3.x, (float) leg.p3.y, (float) leg.p3.z);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		g.translate((float) leg.p4.x, (float) leg.p4.y, (float) leg.p4.z);
		g.sphere(jointSize);
		g.popMatrix();
	}

	private void drawLeg(PApplet g, int index) {
		Leg leg = body.getLeg(index);
		double[] r = body.getRotation();

		g.pushMatrix();
		g.translate((float) leg.p1.x, (float) leg.p1.y, (float) leg.p1.z);
		g.rotateZ((float) r[0]);
		g.rotateY((float) r[1]);
		g.rotateX((float) r[2]);
		g.rotateZ((float) leg.getRa());

		g.translate(Leg.h / 2, 0, 0);
		g.box(Leg.h, 4, 6);
		g.translate(Leg.h / 2, 0, 0);

		g.rotateY((float) (2 * Math.PI - leg.getRb()));

		g.translate(Leg.f / 2, 0, 0);
		g.box(Leg.f, 4, 6);
		g.translate(Leg.f / 2, 0, 0);

		g.rotateY((float) (2 * Math.PI - leg.getRc()));

		g.translate(Leg.t / 2, 0, 0);
		g.box(Leg.t, 4, 6);

		g.popMatrix();
	}

	public void drawLegFrame(PApplet g) {
		g.pushMatrix();

		Vector3d o = new Vector3d(0, 0, 2);

		g.fill(200, 200, 50);
		g.beginShape();
		LegConfig lc = body.getLegConfig();
		for (int i = 0; i < Body.count; ++i) {
			Leg leg = body.getLeg(i);
			if (lc.ground[i]) {
				vertex(g, leg.p4, o);
			}
		}
		g.endShape();

		Vector3d c = body.getCenter();
		Vector3d t = body.getLegConfig().plane.project(c);

		g.translate((float) t.x, (float) t.y, (float) t.z);
		g.fill(Color.yellow.getRGB());
		g.sphere(4);

		g.popMatrix();
	}

	public void drawPlane(PApplet g) {
		Plane3d plane = body.getLegConfig().plane;
		double[] r = Matrix.getRotation(plane.n);

		g.pushMatrix();
		g.rotateZ((float) r[0]);
		g.rotateY((float) r[1]);
		g.rotateX((float) r[2]);
		g.translate(0, 0, (float) plane.D + 1);
		g.fill(150, 150, 100);
		g.rect(-300, -300, 600, 600);
		g.popMatrix();
	}

	private static void vertex(PApplet g, Vector3d p) {
		g.vertex((float) p.x, (float) p.y, (float) p.z);
	}

	private static void vertex(PApplet g, final Vector3d v, Vector3d o) {
		v.add(o);
		g.vertex((float) v.x, (float) v.y, (float) v.z);
	}

}
