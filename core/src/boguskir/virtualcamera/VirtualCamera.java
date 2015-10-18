package boguskir.virtualcamera;

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
import com.badlogic.gdx.math.Vector3;


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
		myCam.update();

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
//		shapeRenderer.circle(myCam.paneA.x,myCam.paneA.z, 3);
		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(myCam.paneB.x,myCam.paneB.z, 3);
		
		shapeRenderer.line(myCam.paneA.x, myCam.paneA.z,myCam.paneB.x,myCam.paneB.z);
		shapeRenderer.line(myCam.paneA.x, myCam.paneA.z,myCam.paneC.x,myCam.paneC.z);
		
//		shapeRenderer.setColor(Color.ORANGE);
//		shapeRenderer.circle(myCam.paneC.x,myCam.paneC.z, 5);
//		shapeRenderer.line(myCam.paneA.x, myCam.paneA.z, myCam.paneB.x,myCam.paneB.z);
//		
//		shapeRenderer.setColor(Color.GREEN);
//		shapeRenderer.circle(myCam.paneAC.x,myCam.paneAC.z, 5);
//		shapeRenderer.circle(myCam.paneAB.x,myCam.paneAB.z, 4);
		
		shapeRenderer.setColor(Color.PURPLE);
	//	shapeRenderer.line(myCam.posAX.x,myCam.posAX.z, myCam.paneA.x, myCam.paneA.z);
	//	shapeRenderer.circle(myCam.posAX.x,myCam.posAX.z, 3);
		shapeRenderer.circle(myCam.paneA.x, myCam.paneA.z, 3);
		shapeRenderer.setColor(Color.GREEN);
	//	shapeRenderer.line(myCam.posBX.x,myCam.posBX.z, myCam.paneB.x, myCam.paneB.z);
		
	//	shapeRenderer.circle(myCam.posBX.x,myCam.posBX.z, 3);
		shapeRenderer.circle(myCam.paneB.x, myCam.paneB.z, 3);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.circle(myCam.paneC.x,myCam.paneC.z,3);
		shapeRenderer.line(myCam.paneB.x, myCam.paneB.z,myCam.paneC.x,myCam.paneC.z);
		
		shapeRenderer.setColor(Color.SALMON);
//		shapeRenderer.circle(myCam.Zcenter.x,myCam.Zcenter.z,4);
		// end cam
		
		
	
		
		
		for (MyRectangle r : _worldRepo.rectangles) {
			r.drawMap(shapeRenderer);
			r.draw3D(shapeRenderer, myCam,W,H);
		}
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.circle(0, 0, 0);
		shapeRenderer.rect(-W/2, -H/2, W,H);
		shapeRenderer.setColor(Color.BLACK);
		
		
		Vector3 sph = new Vector3(50,0,180);
		
		Vector3 cen = new Vector3(0,100,0);
		
		sph = myCam.StoC(sph);
		
		//System.out.println(cen.add(sph));
		
		// pion, poziom  pion na 90 jest plasko, poziom na 0 to prawo
	//	Vector3 zajebany = myCam.StoCLineRot(new Vector3(200, chuj,135));
		
		chuj+=3;
//	
//		System.out.println(zajebany);
//		// zajebany2 = new Vector3(300,0,200);
//		
//		shapeRenderer.setColor(Color.BLACK);
//		shapeRenderer.circle(-W/2+cen.x,-H/2 + cen.z,5);
//		shapeRenderer.circle(-W/2,-H/2 ,5);
//		
//		
//		shapeRenderer.line(-W/2,-H/2,-W/2+zajebany.x,-H/2 + zajebany.z);
//		shapeRenderer.circle(-W/2+zajebany.x,-H/2 + zajebany.z,15);
//		shapeRenderer.circle(-W/2,-H/2,200);
//		
	
		
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
	    font.draw(batch,"angle Z: "+(int)myCam.angleZ+"'", 10, H-90);

	    batch.end();

	}
	

	float  dupa = 90;
	float chuj =0;
	
	private SpriteBatch batch ;
	private BitmapFont font;
	int fps = 60;
	
	private void handleInput() {

		// rotate
		
		if (Gdx.input.isKeyPressed(Input.Keys.K)) {
			cam.zoom += 0.03;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.L)) {
			cam.zoom -= 0.03;
		}
		myCam.handleInput(_worldRepo,cam);

	}
}