package com.boguskir.virtualcamera;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyRectangle {

	ArrayList<Vector3> points ;


	public MyRectangle(Vector3 a, Vector3 b, Vector3 c, Vector3 d){
		
		points = new ArrayList<Vector3>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		
	}
	
	public void drawMap(ShapeRenderer s){
		
		s.setColor(Color.BLUE);
		for (Vector3 p : points) {
			s.circle(p.x, p.z, 10);
		}
		
	}
	
	public void draw3D(ShapeRenderer s, MyCamera myCam, float W, float H){
		
		s.setColor(Color.RED);
//		ArrayList<Vector2> ttt = new ArrayList<Vector2>();
//		for(int i = 0;i< points.size();i++){
//			
//			Vector2 tmp = myCam.transformWorldToPane(points.get(i));
//			ttt.add(tmp);
//			
//			
//		
//			//shapeRenderer.circle(tmp.x, tmp.y, 4);
//		}
		
		Vector2 [] tmp = new Vector2[2];
		
		tmp = myCam.transformLine(points.get(0), points.get(1));
		
		Vector2 p1 = tmp[0];
		Vector2 p2 = tmp[1];
		s.circle(tmp[2].x, tmp[2].y, 4);
		s.circle(tmp[3].x, tmp[3].y, 4);
		
		tmp = myCam.transformLine(points.get(2), points.get(3));
		Vector2 p3 = tmp[0];
		Vector2 p4 = tmp[1];
		s.circle(tmp[2].x, tmp[2].y, 4);
		s.circle(tmp[3].x, tmp[3].y, 4);
		


		
		//shapeRenderer.circle(tmp.x*W-W/2, -H/2 + tmp.y *H/2 , 10);
		
//		s.line(p1.x*W-W/2, -H/2 + ttt.get(0).y *H/2, ttt.get(1).x*W-W/2, -H/2 + ttt.get(1).y *H/2);
//		s.line(ttt.get(1).x*W-W/2, -H/2 + ttt.get(1).y *H/2, ttt.get(3).x*W-W/2, -H/2 + ttt.get(3).y *H/2);
//		s.line(ttt.get(2).x*W-W/2, -H/2 + ttt.get(2).y *H/2, ttt.get(3).x*W-W/2, -H/2 + ttt.get(3).y *H/2);
//		s.line(ttt.get(2).x*W-W/2, -H/2 + ttt.get(2).y *H/2, ttt.get(0).x*W-W/2, -H/2 + ttt.get(0).y *H/2);
		
		if(p1!=null && p2!=null)
		drawLine(p1, p2, s, W, H);
	
		if(p4!=null && p2!=null)
		drawLine(p2, p4, s, W, H);
		if(p3!=null && p4!=null)
		drawLine(p3, p4, s, W, H);
		if(p3!=null && p1!=null)
		drawLine(p3, p1, s, W, H);
	
		System.out.println("-----------");
	}
	
	private void drawLine(Vector2 A, Vector2 B, ShapeRenderer s, float W, float H){
		
		
		
		s.line(
				 A.x*W - W/2, -H/2 + A.y *H/2, 
				 B.x*W - W/2, -H/2 + B.y *H/2
		);
		
		s.circle(A.x*W - W/2, -H/2 + A.y *H/2, 8);
		s.circle(B.x*W - W/2, -H/2 + B.y *H/2, 8);

	}
	
	public ArrayList<Vector3> getPoints(){
		return points;
	}
	
	
}
