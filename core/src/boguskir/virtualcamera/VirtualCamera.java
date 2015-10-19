package boguskir.virtualcamera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;


public class VirtualCamera extends ApplicationAdapter {

	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;


	private int H;
	private int W;
	
	private WorldRepository _worldRepo;
	
	private SpriteBatch batch ;
	private BitmapFont font;
	private int fps = 60;

	private boolean DEBUG = false;
	private boolean FULL_RENDER = false;
	
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
		polyBatch = new PolygonSpriteBatch();
	}

	MyCamera myCam = new MyCamera();
	PolygonSpriteBatch polyBatch  ;
	
	@Override
	public void render() {

		// Update
		myCam.handleInput(_worldRepo,cam);
		myCam.update();
		_worldRepo.update(myCam.pos);

		handleInput();
		
		// Render
		
		// Clear screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render filled polygons
		if (FULL_RENDER) {
			polyBatch.setProjectionMatrix(cam.combined);
			polyBatch.begin();
			for (MyRectangle r : _worldRepo.rectangles) {
				r.draw3D(shapeRenderer, myCam, W, H, polyBatch);
			}
			polyBatch.end();

		} 
		// Render just vertices
		else {
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Line);
			for (MyRectangle r : _worldRepo.rectangles) {
				if (DEBUG)
					r.drawMap(shapeRenderer);
				r.draw3D(shapeRenderer, myCam, W, H, null);
			}

			if (DEBUG) {

				shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.circle(-myCam.pos.x, myCam.pos.z, 3);
				shapeRenderer.line(-myCam.paneA.x, myCam.paneA.z, -myCam.paneB.x, myCam.paneB.z);
			}

			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.circle(0, 0, 0);
			shapeRenderer.rect(-W / 2, -H / 2, W, H);

			shapeRenderer.end();
		}
		
		drawHud();

	}

	private void drawHud() {
		
		font.setColor(Color.RED);
	    batch.begin();

		if (MathUtils.random(10) == 0)
			fps = (int) (1f / Gdx.graphics.getDeltaTime());

		font.draw(batch,"FPS: "+String.valueOf(fps), 10, H - 10);
		font.draw(batch,"DEBUG: "+DEBUG, W-100, H - 10);
		font.draw(batch,"FULL RENDER: "+FULL_RENDER, W-150, H - 30);
		font.draw(batch,"Rafal Boguski 2015", W-130, 20);
		font.draw(batch, "Camera pos: [" + (int) myCam.pos.x + ", " + (int) myCam.pos.y + ", " + (int) myCam.pos.z + "]", 10, H - 30);
		font.draw(batch, "angle W: " + (int) myCam.getAngleW() + "'", 10, H - 50);
		font.draw(batch, "angle H: " + (int) myCam.getAngleH() + "'", 10, H - 70);
		font.draw(batch, "angle Z: " + (int) myCam.getAngleZ() + "'", 10, H - 90);
		font.draw(batch, "FOV    : " + (int) myCam.FOV + "'", 10, H - 110);
		font.draw(batch, "F      : " + (int) myCam.F + "'", 10, H - 130);
		
		
		font.draw(batch, "Move    --- [W-S] [A-D] [Shift-Space]", 10, H - 160);
		font.draw(batch, "Rotate    --- [Q-E] [Z-X] [O-P]", 10, H - 180);
		font.draw(batch, "Translate --- [H-J] [N-M]", 10, H - 200);
		font.draw(batch, "Zoom --- [K-L]", 10, H - 220);
		font.draw(batch, "FOV --- [V-B]", 10, H - 240);
		font.draw(batch, "F --- [F-G]", 10, H - 260);
		font.draw(batch, "DEBUG RENDER --- [F1-F4]", 10, H - 280);


		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			DEBUG = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
			DEBUG = false;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
			FULL_RENDER = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.F4)) {
			FULL_RENDER = false;
		}
	}

}