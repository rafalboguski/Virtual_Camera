package com.boguskir.virtualcamera;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class WorldRepository {

	public ArrayList<MyRectangle> rectangles = new ArrayList<MyRectangle>();

	public WorldRepository() {

		addBlock(new Vector3(-50, 0, 100), 150);
		addBlock(new Vector3(-60, 0, 200), 250);
		addBlock(new Vector3(-70, 0, 320), 190);

		addBlock(new Vector3(150, 0, 100), 150);
		addBlock(new Vector3(130, 0, 200), 250);
		addBlock(new Vector3(140, 0, 320), 190);

		

	}

	private void addBlock(Vector3 origin, int height) {

		int width = 50;
		int depth = 50;

		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z + depth),
				new Vector3(origin.x + width, origin.y			, origin.z + depth), 
				new Vector3(origin.x		, origin.y + height	, origin.z + depth), 
				new Vector3(origin.x + width, origin.y + height	, origin.z + depth)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z),
				new Vector3(origin.x		, origin.y			, origin.z + depth), 
				new Vector3(origin.x		, origin.y + height	, origin.z),
				new Vector3(origin.x		, origin.y + height	, origin.z + depth)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x + width, origin.y			, origin.z),
				new Vector3(origin.x + width, origin.y			, origin.z + depth), 
				new Vector3(origin.x + width, origin.y + height	, origin.z), 
				new Vector3(origin.x + width, origin.y + height	, origin.z + depth)));

		rectangles.add(new MyRectangle(
				new Vector3(origin.x		, origin.y			, origin.z),
				new Vector3(origin.x + width, origin.y			, origin.z), 
				new Vector3(origin.x		, origin.y + height	, origin.z), 
				new Vector3(origin.x + width, origin.y + height	, origin.z)));

	}

}
