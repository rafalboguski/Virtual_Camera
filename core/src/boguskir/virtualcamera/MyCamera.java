package boguskir.virtualcamera;


import java.awt.Point;
import java.util.ArrayList;

import javafx.geometry.Point3D;
import javafx.scene.shape.Line;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

	// punkty plaszczyzny kamery
	public Vector3 paneA;
	public Vector3 paneB;
	public Vector3 paneC;
	public Vector3 paneAC;


	// katy skierowania kamery
	private float angleW;
	private float angleH;
	private float angleZ;

	// punkty pomocnicze do obrotów 
	private Vector3 posAX;
	private Vector3 posBX;
	private Vector3 posCX;

	// k¹t widzenia i ogniskowa
	float FOV = 90;
	private float F = 50;

	// szybkoœæ poruszania siê kamery
	private float moveSpeed = 3f;

	
	public MyCamera() {

		pos = new Vector3(0, 0, 0);
		angleW = 90f;
		angleH = 0;
		angleZ = 0;

		update();
	}
	
	public void update() {
		float d = F / cos(FOV / 2);
		
		posAX = new Vector3(cos(90 - FOV / 2f) * d,	 0, angleW + 90);
		posAX = StoC(posAX).add(pos);

		paneA = new Vector3(F, angleH - FOV / 2 * 9 / 16f, angleW);
		paneA = StoC(paneA).add(posAX);

		posBX = new Vector3(cos(90 - FOV / 2f) * d, 0, angleW - 90);
		posBX = StoC(posBX).add(pos);

		paneB = new Vector3(F, angleH - FOV / 2 * 9 / 16f, angleW);
		paneB = StoC(paneB).add(posBX);

		posCX = new Vector3(cos(90 - FOV / 2f) * d, 0, angleW + 90);
		posCX = StoC(posCX).add(pos);

		paneC = new Vector3(F, angleH + FOV / 2 * 9 / 16f, angleW);
		paneC = StoC(paneC).add(posCX);

		paneAC = new Vector3(50, -angleH, 180 + angleW);
		paneAC = StoC(paneAC).add(pos);

	}

	
	
	public void rotateW(float rotation) {

		double r = (rotation * 180f / Math.PI);

		Vector2 dir = new Vector2((float) -r, 0).rotate(angleZ);

		angleW += dir.x;
		angleH += dir.y;

	}

	public void rotateH(float rotation) {
		double r = (rotation * 180f / Math.PI);

		Vector2 dir = new Vector2((float) -r, 0).rotate(angleZ+90);

		angleW += dir.x;
		angleH += dir.y;
	}
	
	public void rotateZ(float rotation) {
		double  r = (rotation * 180f / Math.PI);
		angleZ += r;
	}

	
	public Vector2[] transformWorldToPane(Vector3 point) {

		
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

	public Vector2[] transformLine(Vector3 pointA, Vector3 pointB) {

		Vector2[] ret = new Vector2[4];

		Vector2[] A = transformWorldToPane(pointA);
		Vector2[] B = transformWorldToPane(pointB);

		ret[0] = A[0];
		ret[1] = B[0];
		ret[2] = A[1];
		ret[3] = B[1];
		return ret;

	}


	private float sin(float dec){
		return MathUtils.sinDeg(dec);
	}
	private float cos(float dec){
		return MathUtils.cosDeg(dec);
	}


	public void handleInput(WorldRepository world, OrthographicCamera cam) {
		

		if (Gdx.input.isKeyPressed(Input.Keys.E)) {

			this.rotateW(-0.03f);
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {

			this.rotateW(0.03f);
			
		}

		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {

			
			this.rotateH(-0.03f);
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {

			
			this.rotateH(0.03f);
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			cam.rotate(-1, 0, 0, 1);
			angleZ-=1;
			
		}
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			cam.rotate(1, 0, 0, 1);
			angleZ+=1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			cam.zoom += 0.03;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			cam.zoom -= 0.03;
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
		
		if (Gdx.input.isKeyPressed(Input.Keys.H)) {

			cam.translate(10, 0);
		}	
		if (Gdx.input.isKeyPressed(Input.Keys.J)) {

			cam.translate(-10, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.N)) {

			cam.translate(0, 10);
		}	
		if (Gdx.input.isKeyPressed(Input.Keys.M)) {

			cam.translate(0, -10);
		}	
		cam.update();

	}


	public void moveForward() {
		Vector3 dir = new Vector3(moveSpeed,angleH,angleW);
		pos = StoC(dir).add(pos);
	}

	public void moveBack() {

		Vector3 dir = new Vector3(moveSpeed,180+angleH,angleW);
		pos = StoC(dir).add(pos);

	}

	public void moveLeft() {

		Vector3 dir = new Vector3(moveSpeed,angleZ,angleW+90);
		pos = pos.sub(StoC(dir));

	}

	public void moveRight() {

		Vector3 dir = new Vector3(moveSpeed,angleZ,angleW+90);
		pos = StoC(dir).add(pos);

	}

	// todo: corelate to angleH, temporaty solution
	public void moveUp() {
		Vector3 dir = new Vector3(moveSpeed,angleZ+90,angleW-90);
		dir = StoC(dir);
		dir.x = -dir.x;
		dir.z = -dir.z;
		pos = pos.sub(dir);
	}

	public void moveDown() {
		Vector3 dir = new Vector3(moveSpeed,angleZ-90,angleW-90);
		dir = StoC(dir);
		dir.x = -dir.x;
		dir.z = -dir.z;
		pos = pos.sub(dir);
	}



	// Get Set
	public float getAngleW() {
		return angleW;
	}

	public float getAngleH() {
		return angleH;
	}
	
	public float getAngleZ() {
		return angleZ;
	}
	
	// p.y <-90,90>
	public Vector3 StoC(Vector3 p) {

		float x = p.x * sin(p.y - 90) * cos(p.z);
		float y = p.x * sin(p.y - 90) * sin(p.z);
		float z = p.x * cos(p.y - 90);

		x = -x;
		y = -y;

		return new Vector3(x, z, y);
	}
	
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