package boguskir.virtualcamera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class WorldRepository{


	public ArrayList<MyRectangle> rectangles = new ArrayList<MyRectangle>();

	public WorldRepository() {

		addBlock(new Vector3(-170, -50, 100), 50,100,50);
		addBlock(new Vector3(-160, -50, 200),50, 250,50);
		addBlock(new Vector3(-140, -50, 320),50, 190,50);
		
		addBlock(new Vector3(160, -50, 100),50, 150,50);
		addBlock(new Vector3(170, -50, 200), 50,250,50);
		addBlock(new Vector3(150, -50, 320), 50,190,50);
		
		addBlock(new Vector3(150, -50, 320), 50,190,50);
		
		addBlock(new Vector3(100, -48, 360), -150,2,-250);
		
		
	}

	private void addBlock(Vector3 origin,int width, int height, int depth) {


		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z + depth),
				new Vector3(origin.x + width, origin.y			, origin.z + depth), 
				new Vector3(origin.x		, origin.y + height	, origin.z + depth), 
				new Vector3(origin.x + width, origin.y + height	, origin.z + depth),
				new Color(0.2f,0,0,1)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z),
				new Vector3(origin.x		, origin.y			, origin.z + depth), 
				new Vector3(origin.x		, origin.y + height	, origin.z),
				new Vector3(origin.x		, origin.y + height	, origin.z + depth),
				new Color(0.7f,0,0,1)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x + width, origin.y			, origin.z),
				new Vector3(origin.x + width, origin.y			, origin.z + depth), 
				new Vector3(origin.x + width, origin.y + height	, origin.z), 
				new Vector3(origin.x + width, origin.y + height	, origin.z + depth),
				new Color(0.7f,0,0,1)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z),
				new Vector3(origin.x + width, origin.y			, origin.z), 
				new Vector3(origin.x		, origin.y + height	, origin.z), 
				new Vector3(origin.x + width, origin.y + height	, origin.z),
				new Color(0.2f,0,0,1)));
		
		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y + height , origin.z),
				new Vector3(origin.x + width, origin.y + height	, origin.z), 
				new Vector3(origin.x		, origin.y + height	, origin.z + depth), 
				new Vector3(origin.x + width, origin.y + height	, origin.z + depth),
				new Color(0.6f,0,0,1)));
		
		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y  		, origin.z),
				new Vector3(origin.x + width, origin.y 			, origin.z), 
				new Vector3(origin.x		, origin.y 			, origin.z + depth), 
				new Vector3(origin.x + width, origin.y 			, origin.z + depth),
				new Color(0,0,0,1)));
	

	}
	
	public void update(Vector3 cameraPosition){
		Collections.sort(rectangles, new MyRectangleComparator(cameraPosition));	
	}
	

}
