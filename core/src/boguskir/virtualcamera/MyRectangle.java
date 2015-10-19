package boguskir.virtualcamera;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyRectangle {

	private ArrayList<Vector3> points;

	private Color color;

	public MyRectangle(Vector3 a, Vector3 b, Vector3 c, Vector3 d,Color color) {

		this.points = new ArrayList<Vector3>();
		this.points.add(a);
		this.points.add(b);
		this.points.add(c);
		this.points.add(d);
		this.color = color;

	}


	public void drawMap(ShapeRenderer s) {

		s.setColor(Color.BLUE);
		for (Vector3 p : points) {
			s.circle(-p.x, p.z, 10);
		}

		s.setColor(Color.BLACK);
	}

	public void draw3D(ShapeRenderer s, MyCamera myCam, float W, float H,PolygonSpriteBatch polyBatch) {

		Vector2[] tmp = new Vector2[2];

		tmp = myCam.transformLine(points.get(0), points.get(1));
		Vector2 p1 = tmp[0];
		Vector2 p2 = tmp[1];

		tmp = myCam.transformLine(points.get(2), points.get(3));
		Vector2 p3 = tmp[0];
		Vector2 p4 = tmp[1];


		if(polyBatch == null){
			
			if (p1 != null && p2 != null)
			drawLine(p1, p2, s, W, H);
			if (p4 != null && p2 != null)
			drawLine(p2, p4, s, W, H);
			if (p3 != null && p4 != null)
			drawLine(p3, p4, s, W, H);
			if (p3 != null && p1 != null)
			drawLine(p3, p1, s, W, H);

		
		// Creating the color filling (but textures would work the same way)
		
		}
		else{
			if ((p1 != null && p2 != null && p3 != null && p4 != null)) {
	
				Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
				pix.setColor(0xDEADBEFF); // DE is red, AD is green and BE is blue.
				pix.fill();
				Texture textureSolid = new Texture(pix);
				PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
						new float[] { // Four vertices
								X(p1, W, H), Y(p1, W, H), // Vertex 0 3--2
								X(p2, W, H), Y(p2, W, H), // Vertex 1 | /|
								X(p4, W, H), Y(p4, W, H), // Vertex 2 |/ |
								X(p3, W, H), Y(p3, W, H)  // Vertex 3 0--1
				}, new short[] { 0, 1, 2, // Two triangles using vertex indices.
								 0, 2, 3 // Take care of the counter-clockwise direction.
							   });
				PolygonSprite poly = new PolygonSprite(polyReg);
				poly.setOrigin(0, 0);
	
				polyBatch.setColor(color);
				poly.setColor(color);
				poly.draw(polyBatch);
	
			}
		}

	}
	
	private float X(Vector2 A,float W, float H) {

		return A.x * W + W / 2; 
	}
	
	private float Y(Vector2 A,float W, float H) {

		return -H / 2 + A.y * H; 
	}
	
	private void drawLine(Vector2 A, Vector2 B, ShapeRenderer s, float W, float H) {

		s.line(A.x * W + W / 2, -H / 2 + A.y * H, B.x * W + W / 2, -H / 2 + B.y * H);

		s.setColor(Color.RED);
		s.circle(A.x*W + W/2, -H/2 + A.y *H, 3);
		s.circle(B.x*W + W/2, -H/2 + B.y *H, 3);
		s.setColor(Color.BLACK);
	}

	public ArrayList<Vector3> getPoints() {
		return points;
	}

}
