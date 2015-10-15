package boguskir.virtualcamera;


import java.awt.Point;
import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.shape.Line;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
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
	// public Vector3 paneD;

	//public Vector3 paneAB;

	public Vector3 paneAC;



	// to do: make paneDist dependent on it
	private float fov = 90f;

	// katy skierowania kamery
	private float angleW;
	private float angleH;

	private float moveSpeed = 1f;

	public Vector3 posAX;

	public Vector3 posBX;

	private Vector3 posCX;

	public MyCamera() {

		pos = new Vector3(0, 0, 0);
		angleW = 90f;
		angleH = 0;

//		paneA = new Vector3(-paneWidth / 2, -paneHeight, paneDist);
//		paneB = new Vector3(paneWidth / 2, -paneHeight, paneDist);
//		paneC = new Vector3(-paneWidth / 2, paneHeight, paneDist);
//		// paneD = new Vector3(paneWidth / 2, paneHeight, paneDist);
//
//		//paneAB = new Vector3(-paneWidth / 2, -paneHeight, paneDist / 2);
//		paneAC = new Vector3(-paneWidth / 2, 0, paneDist / 2);

//		for (int i = 0; i < 100; i++)
//			moveBack();
//		for (int i = 0; i < 50; i++)
//			moveUp();

		
		
		update();

		
		// TODO: zrob ogniskowa i fov potem update pane na podstawie pos i
		// oblicznie nowych pozyci

		//

	}
	
	public void update(){
		float d = F / cos(FOV / 2);
		
		posAX = new Vector3(cos(90-FOV/2f)*d, 0,angleW + 90);
		posAX = StoC(posAX).add(pos);
		
		paneA = new Vector3(d, angleH - FOV/2 *9/16f,angleW );
		paneA = StoC(paneA).add(posAX);
		
		
		posBX = new Vector3(cos(90-FOV/2f)*d, 0,angleW - 90);
		posBX = StoC(posBX).add(pos);
		
		paneB = new Vector3(d, angleH - FOV/2 *9/16f ,angleW );
		paneB = StoC(paneB).add(posBX);
		
		
		posCX = new Vector3(cos(90-FOV/2f)*d, 0,angleW + 90);
		posCX = StoC(posCX).add(pos);
		
		paneC = new Vector3(d, angleH + FOV/2 *9/16f,angleW);
		paneC = StoC(paneC).add(posCX);
		
		
		System.out.println((posAX));
//		paneAB = new Vector3(50, -angleH ,180+angleW );
//		paneAB = StoC(paneAB).add(paneA);
		
		
//		
		paneAC = new Vector3(50, -angleH ,180+angleW );
		paneAC = StoC(paneAC).add(pos);
		//System.out.println(paneA.y +" "+(-angleH)+" "+paneC.y+" "+paneAC.y) ;

		// TODO zjebane to jest napraw
	//	System.out.println(StoC(new Vector3(10,angleH,135)));
		
		
		
	}

	
	private float FOV = 90;
	private float F = 50;
	
	// rotacja w plaszczyznie wertykalnej
	public void rotateW(float rotation) {
		
		double  r = (rotation * 180f / Math.PI);
		angleW += r;

	}
	
	public void rotateH(float rotation) {
		double  r = (rotation * 180f / Math.PI);
		angleH += r;
	}

	// zamienia punkty ze swiata na procentowe polozenie na plaszczyznie (pane)
	public Vector2[] transformWorldToPane2(Vector3 point, ShapeRenderer s) {

	
		Plane plane = new Plane(paneA, paneB, paneC);
		Vector3 inter = new Vector3();

		Intersector.intersectLinePlane(
				pos.x, 		pos.y, 		pos.z, 
				point.x, 	point.y,	point.z, 
				plane, inter);
		
		Plane pozioma = new Plane(paneA, paneAC, paneB);
		float distH = -pozioma.distance(inter);
		
	
		Plane pionowa = new Plane(paneA, paneAC, paneC);
		float distW = -pionowa.distance(inter);
		
		
		Vector3 A = new Vector3(paneA.x - pos.x, paneA.z - pos.z, paneA.y - pos.y);
		A = CtoS(A);
		Vector3 I = new Vector3(inter.x - pos.x, inter.z - pos.z, inter.y - pos.y);
		I = CtoS(I);

		distW = (-I.z + A.z) / FOV;
		
		distH = (-I.y + A.y) / (FOV*9/16f);
		
		

		return new Vector2[] {
				(point.dst(pos) > point.dst(inter)) ? 
							new Vector2(
									distW , 
									distH )
						
						: null,
				new Vector2(inter.x, inter.z) };

	}
	
	public Vector2[] transformWorldToPane(Vector3 point, ShapeRenderer s) {

		
		Plane plane = new Plane(paneA, paneB, paneC);
		Vector3 inter = new Vector3();

		Intersector.intersectLinePlane(
				pos.x, 		pos.y, 		pos.z, 
				point.x, 	point.y,	point.z, 
				plane, inter);
		
		Plane pozioma = new Plane(paneA, paneAC, paneB);
		float distH = -pozioma.distance(inter);
		
	
		Plane pionowa = new Plane(paneA, paneAC, paneC);
		float distW = -pionowa.distance(inter);

		return new Vector2[] {
				(point.dst(pos) > point.dst(inter)) ? 
							new Vector2(
									distW / (paneA.dst(paneB)), 
									distH / (paneA.dst(paneC)))
						
						: null,
				new Vector2(inter.x, inter.z) };

	}



	public Vector2[] transformLine(Vector3 pointA, Vector3 pointB, ShapeRenderer s) {

		Vector2[] ret = new Vector2[4];

		Vector2[] A = transformWorldToPane(pointA,s);
		Vector2[] B = transformWorldToPane(pointB,s);

		ret[0] = A[0];
		ret[1] = B[0];
		ret[2] = A[1];
		ret[3] = B[1];
		return ret;

	}

	int i = 0;

	


	
	private float sin(float dec){
		return MathUtils.sinDeg(dec);
	}
	private float cos(float dec){
		return MathUtils.cosDeg(dec);
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
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {

			this.rotateW(0.03f);
			
		}

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {

			if(angleH>-(90-FOV/2*9/16f))
			this.rotateH(-0.03f);
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {

			if(angleH<(90-FOV/2*9/16f))
			this.rotateH(0.03f);
			
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {

			this.moveForward();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {

			this.moveBack();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {

			this.moveLeft();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {

			this.moveRight();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {

			this.moveUp();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

			this.moveDown();
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F)) {

			
			F-=0.5f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.G)) {

			F+=0.5f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.V)) {

			
			FOV-=0.5f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.B)) {

			FOV+=0.5f;
		}	

	}


	public void moveForward() {

		Vector2 tmp = new Vector2(moveSpeed, 0).rotate(angleW);

		pos = pos.add(tmp.x, 0, tmp.y);

	}

	public void moveBack() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW);

		pos = pos.add(tmp.x, 0, tmp.y);

	}

	public void moveLeft() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW - 90);

		pos = pos.add(tmp.x, 0, tmp.y);

	}

	public void moveRight() {

		Vector2 tmp = new Vector2(-moveSpeed, 0).rotate(angleW + 90);

		pos = pos.add(tmp.x, 0, tmp.y);

	}

	// todo: corelate to angleH, temporaty solution
	public void moveUp() {

		pos = pos.add(0, moveSpeed, 0);
	}

	public void moveDown() {

		pos = pos.add(0, -moveSpeed, 0);

	}



	// Get Set
	public float getAngleW() {
		return angleW;
	}

	public float getAngleH() {
		return angleH;
	}
	
	// p.y <-90,90>
	public Vector3 StoC(Vector3 p) {

		float x = p.x * sin(p.y - 90) * cos(p.z);
		float y = p.x * sin(p.y - 90) * sin(p.z);
		float z = p.x * cos(p.y - 90);

		x = -x;
		y = -y;

		//System.out.println((int) (p.z) + "  " + (int) x + "\t" + (int) y + "\t" + (int) z);
		return new Vector3(x, z, y);
	}
	

//	public Vector3 StoCLineRot(Vector3 p) {
//
//		float x = p.x * sin(p.y - 90) * cos(angleW);
//		float y = p.x * sin(p.y - 90) * sin(angleW);
//		float z = p.x * cos(p.y - 90);
//
//		x = -x;
//		y = -y;
//
//		x+= p.x * sin(0) * cos()
//		p.rotate(degrees, axisX, axisY, axisZ);
//		
//		//System.out.println((int) (p.z) + "  " + (int) x + "\t" + (int) y + "\t" + (int) z);
//		return new Vector3(x, z, y);
//	}
//	
	public Vector3 CtoS(Vector3 p) {

		float r = p.len();
		float alfa = (float) Math.toDegrees( 
					Math.atan(p.y/p.x)
				);

		float beta = -(float) Math.toDegrees(Math.acos(p.z / r))+90;
		
		if(p.x<0 && p.y>0)
			alfa = 180+alfa;
		if(p.x<0 && p.y<0)
			alfa = 180+alfa;	
		if(p.x>0 && p.y<0)
			alfa = 360+alfa;
		
//		if(p.x<0 && p.y>0)
//			alfa = 180+alfa;
//		if(p.x<0 && p.y<0)
//			alfa = 180+alfa;	
//		if(p.x>0 && p.y<0)
//			alfa = 360+alfa;
		
		
	//	System.out.println((int)beta+"\t"+(int)alfa);
		
		return new Vector3(r, beta, alfa);
	}

}