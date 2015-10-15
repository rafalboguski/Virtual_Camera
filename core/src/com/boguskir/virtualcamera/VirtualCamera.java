package com.boguskir.virtualcamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;


public class VirtualCamera extends ApplicationAdapter {

	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;


	private int H;
	private int W;
	
	private WorldRepository _worldRepo;

	@Override
	public void create() {

		H = Gdx.graphics.getHeight();
		W = Gdx.graphics.getWidth();

		shapeRenderer = new ShapeRenderer();

		cam = new OrthographicCamera(W, H);
		cam.position.set(0, 0, 0);
		cam.update();

		
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		_worldRepo = new WorldRepository();

	}

	MyCamera myCam = new MyCamera();

	@Override
	public void render() {

		// Update
		handleInput();
		cam.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.setProjectionMatrix(cam.combined);

		shapeRenderer.begin(ShapeType.Line);

		// begin cam map
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(myCam.pos.x, myCam.pos.z, 11);

		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.line(myCam.paneA.x, myCam.paneA.z, myCam.paneB.x,myCam.paneB.z);
		// end cam
		
		
	
		
		
		for (MyRectangle r : _worldRepo.rectangles) {
			r.drawMap(shapeRenderer);
			r.draw3D(shapeRenderer, myCam,W,H);
		}
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(0, 0, 9);
		shapeRenderer.rect(-W/2, -H/2, W,H);

		shapeRenderer.end();
		
		// HUD
		font.setColor(Color.BLACK);

	    batch.begin();

	    if(MathUtils.random(10)==0)
	    	fps = (int)(1f/Gdx.graphics.getDeltaTime());

	    font.draw(batch,String.valueOf(fps), 10, H-10);
	    font.draw(batch,"Camera pos: ["+(int)myCam.pos.x+", "+(int)myCam.pos.y+", "+(int)myCam.pos.z+"]", 10, H-30);
	    font.draw(batch,"angle W: "+(int)myCam.getAngleW()+"'", 10, H-50);
	    font.draw(batch,"angle H: "+(int)myCam.getAngleH()+"'", 10, H-70);

	    batch.end();

	}
	
	private SpriteBatch batch ;
	private BitmapFont font;
	int fps = 60;
	
	private void handleInput() {

		// rotate
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			cam.rotate(-1, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			cam.rotate(1, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			cam.zoom += 0.03;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			cam.zoom -= 0.03;
		}
		myCam.handleInput(_worldRepo);

	}
}