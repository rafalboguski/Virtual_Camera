package com.boguskir.virtualcamera;

import java.awt.Point;
import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.shape.Line;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;
import com.sun.org.apache.bcel.internal.generic.IDIV;

public class MyCamera {

	// punkt centralny
	public Vector3 pos;

	public Vector3 paneA;
	public Vector3 paneB;
	public Vector3 paneC;
	public Vector3 paneD;

	public Vector3 paneAB;
	
	public Vector3 paneAC;
		
	// ogniskowa
	private float paneDist = 50f;

	// wymiary plaszczyzny (pane)
	private float paneWidth = 60f;
	private float paneHeight = 30f;

	// to do: make paneDist dependent on it
	private float fov = 90f;

	// katy skierowania kamery
	private float angleW;
	private float angleH;

	private float moveSpeed = 1f;

	public MyCamera() {

		pos = new Vector3(0, 0, 0);
		angleW = 90f;
		angleH = 90f;

		paneA = new Vector3(-paneWidth / 2, -paneHeight, paneDist);
		paneB = new Vector3(paneWidth / 2, -paneHeight, paneDist);
		paneC = new Vector3(-paneWidth / 2, paneHeight, paneDist);
		paneD = new Vector3(paneWidth / 2, paneHeight, paneDist);
		
		paneAB = new Vector3(-paneWidth / 2, -paneHeight, paneDist/2);
		paneAC = new Vector3(paneWidth / 2, 0, paneDist/2);
		
		for(int i=0;i<100;i++)
			moveBack();
		for(int i=0;i<50;i++)
			moveUp();

	}

	// rotacja w plaszczyznie wertykalnej
	public void rotateW(float rotation) {
		angleW += rotation * 180f / Math.PI;

		Vector2 tmp = rotate2D(paneA.x, paneA.z, pos.x, pos.z, rotation);
		paneA.x = tmp.x;
		paneA.z = tmp.y;

		tmp = rotate2D(paneB.x, paneB.z, pos.x, pos.z, rotation);
		paneB.x = tmp.x;
		paneB.z = tmp.y;

		tmp = rotate2D(paneC.x, paneC.z, pos.x, pos.z, rotation);
		paneC.x = tmp.x;
		paneC.z = tmp.y;

		tmp = rotate2D(paneD.x, paneD.z, pos.x, pos.z, rotation);
		paneD.x = tmp.x;
		paneD.z = tmp.y;
		
		tmp = rotate2D(paneAB.x, paneAB.z, pos.x, pos.z, rotation);
		paneAB.x = tmp.x;
		paneAB.z = tmp.y;
		
		tmp = rotate2D(paneAC.x, paneAC.z, pos.x, pos.z, rotation);
		paneAC.x = tmp.x;
		paneAC.z = tmp.y;
	}


	// zamienia punkty ze swiata na procentowe polozenie na plaszczyznie (pane)
	public Vector2[] transformWorldToPane(Vector3 point) {

		Plane plane = new Plane(paneA, paneB, paneC);
		Vector3 inter = new Vector3();

		Intersector.intersectLinePlane(pos.x, pos.y, pos.z, point.x, point.y, point.z, plane, inter);
//		Intersector.intersectSegmentPlane(pos, point, plane, inter);

		Plane pozioma = new Plane(paneA, paneB, paneAB);
		float distH = pozioma.distance(inter);

		Plane pionowa = new Plane(paneA, paneC, paneAC);
		float distW = pionowa.distance(inter);
		

		return new Vector2[]{
					(point.dst(pos)>point.dst(inter))?new Vector2(-distW/paneWidth*2.5f, distH/paneHeight):null,
					new Vector2(inter.x, inter.z)
				};


	}
	
	private Vector3 PlaneLineIntersection(Vector3 A, Vector3 B) {

		Plane plane = new Plane(paneA, paneB, paneC);
		Vector3 inter = new Vector3();
		if (Intersector.intersectSegmentPlane(A, B, plane, inter))
			return inter;
		else
			return null;
	}
	

	public Vector2[] transformLine(Vector3 pointA, Vector3 pointB) {

		Vector2[] ret = new Vector2[4];
		
		Vector2[] A = transformWorldToPane(pointA);
		Vector2[] B = transformWorldToPane(pointB);
	//	transformWorldToPane(cameraPlaneIntersection(pointA,pointB));
		
//		if(A==null && B==null){
//			return null;
//		}
//		
		Vector3 inter =  PlaneLineIntersection(pointA,pointB);
		if(inter!=null){
			ret[0] = null;
			ret[1] = null;
		}

	
		
		ret[0] = A[0];
		ret[1] = B[0];
		ret[2] = A[1];
		ret[3] = B[1];
		return ret;

	}
	
	

	// obraca p wzgledem o, o theta stopni
	public Vector2 rotate2D(float px, float py, float ox, float oy, float theta) {

		float x = (float) (Math.cos(theta) * (px - ox) - Math.sin(theta)
				* (py - oy) + ox);
		float y = (float) (Math.sin(theta) * (px - ox) + Math.cos(theta)
				* (py - oy) + oy);
		return new Vector2(x, y);
	}

	
	// TODO add rest of movement look up down and barrel roll
	public void handleInput(WorldRepository world) {
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {

			this.rotateW(-0.03f);
			if (collidesRectangles(world.rectangles))
				this.rotateW(0.03f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {

			this.rotateW(0.03f);
			if (collidesRectangles(world.rectangles))
				this.rotateW(-0.03f);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {

			this.moveForward();
			if (collidesRectangles(world.rectangles))
				this.moveBack();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {

			this.moveBack();
			if (collidesRectangles(world.rectangles))
				this.moveForward();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {

			this.moveLeft();
			if (collidesRectangles(world.rectangles))
				this.moveRight();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {

			this.moveRight();
			if (collidesRectangles(world.rectangles))
				this.moveLeft();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {

			this.moveUp();
			if (collidesRectangles(world.rectangles))
				this.moveDown();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

			this.moveDown();
			if (collidesRectangles(world.rectangles))
				this.moveUp();
		}

	}
	
	// TODO: fix collizions pos to line of 2 points
		private boolean collidesRectangles(ArrayList<MyRectangle> rectangles) {

			for (MyRectangle rectangle : rectangles) {
				for (Vector3 point : rectangle.getPoints()) {
					float dis = 0.65f * point.dst(this.pos);
					
					if (dis <= paneDist) {
						
						return true;
					}
				}
			}

			return false;
		}

	public void moveForward() {

		Vector2 tmp = new Vector2(moveSpeed, 0).rotate(angleW);

		pos = pos.add(tmp.x, 0, tmp.y);
		paneA = paneA.add(tmp.x, 0, tmp.y);
		paneB = paneB.add(tmp.x, 0, tmp.y);
		paneC = paneC.add(tmp.x, 0, tmp.y);
		paneD = paneD.add(tmp.x, 0, tmp.y);
		
		paneAB = paneAB.add(tmp.x, 0, tmp.y);
		paneAC = paneAC.add(tmp.x, 0, tmp.y);
	}

	public void moveBack() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW);

		pos = pos.add(tmp.x, 0, tmp.y);
		paneA = paneA.add(tmp.x, 0, tmp.y);
		paneB = paneB.add(tmp.x, 0, tmp.y);
		paneC = paneC.add(tmp.x, 0, tmp.y);
		paneD = paneD.add(tmp.x, 0, tmp.y);
		
		paneAB = paneAB.add(tmp.x, 0, tmp.y);
		paneAC = paneAC.add(tmp.x, 0, tmp.y);
	}
	
	public void moveLeft() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW-90);

		pos = pos.add(tmp.x, 0, tmp.y);
		paneA = paneA.add(tmp.x, 0, tmp.y);
		paneB = paneB.add(tmp.x, 0, tmp.y);
		paneC = paneC.add(tmp.x, 0, tmp.y);
		paneD = paneD.add(tmp.x, 0, tmp.y);
		
		paneAB = paneAB.add(tmp.x, 0, tmp.y);
		paneAC = paneAC.add(tmp.x, 0, tmp.y);
	}
	public void moveRight() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW+90);

		pos = pos.add(tmp.x, 0, tmp.y);
		paneA = paneA.add(tmp.x, 0, tmp.y);
		paneB = paneB.add(tmp.x, 0, tmp.y);
		paneC = paneC.add(tmp.x, 0, tmp.y);
		paneD = paneD.add(tmp.x, 0, tmp.y);
		
		paneAB = paneAB.add(tmp.x, 0, tmp.y);
		paneAC = paneAC.add(tmp.x, 0, tmp.y);
	}
	// todo: corelate to angleH, temporaty solution
	public void moveUp() {

		pos = pos.add(0, moveSpeed, 0);
		paneA = paneA.add(0, moveSpeed, 0);
		paneB = paneB.add(0, moveSpeed, 0);
		paneC = paneC.add(0, moveSpeed, 0);
		paneD = paneD.add(0, moveSpeed, 0);
		
		paneAB = paneAB.add(0, moveSpeed, 0);
		paneAC = paneAC.add(0, moveSpeed, 0);
	}
	public void moveDown() {

		pos = pos.add(0, -moveSpeed, 0);
		paneA = paneA.add(0, -moveSpeed, 0);
		paneB = paneB.add(0, -moveSpeed, 0);
		paneC = paneC.add(0, -moveSpeed, 0);
		paneD = paneD.add(0, -moveSpeed, 0);
		
		paneAB = paneAB.add(0, -moveSpeed, 0);
		paneAC = paneAC.add(0, -moveSpeed, 0);
	}

	
	// Get Set
	public float getAngleW() {
		return angleW;
	}
	public float getAngleH() {
		return angleH;
	}

	
}