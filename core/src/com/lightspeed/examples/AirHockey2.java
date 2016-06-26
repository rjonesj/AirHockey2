package com.lightspeed.examples;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.lightspeed.logic.Scoreboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AirHockey2 extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Sprite rink;
	Sprite puck;
	Sprite touchSprite;
	World world;
	Body puckBody;
	Body bodyEdgeScreen;
	Body touchBodyRight;
	Body touchBodyLeft;
	Body centerline;
	Body hitBody;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	ExtendViewport viewport;
	BitmapFont font;
	Scoreboard scoreboard = new Scoreboard();

	Vector2 target = new Vector2();
	Vector3 testPoint = new Vector3();
	MouseJoint mouseJointRight = null;
	MouseJoint mouseJointLeft = null;

	Map<Integer, MouseJoint> mouseJointHashMap = new HashMap<Integer, MouseJoint>();

	boolean enableDebug = true;

//	final float PIXELS_TO_METERS = 3779;
//	final float cornerRadius = 200; // radius/1.7f;
//	final float radius = 150;
//	final float puckRadius = 168;
//	final float touchRadius = 192;
//	final float WORLD_WIDTH = 9552;
//	final float WORLD_HEIGHT = 4920;

	final float PIXELS_TO_METERS = 200;
	final float cornerRadius = 20; // radius/1.7f;
	final float puckRadius = 17;
	final float touchRadius = 19;
	final float WORLD_WIDTH = 800;
	final float WORLD_HEIGHT = 400;
	final float GOAL_LENGTH = 66;
	final float borderWidth = 40;

	final float density = 0.5f;
	final float restitution = 0.6f; // 0.7f
	final float friction = 0.001f;

	final float puckDensity = 0.1f; //0.1f;
	final float puckRestitution = restitution; // 0.7f
	final float puckFriction = friction;

	String scoreboardString = "00 00";
	int scoreboardSize = 52;
	float scoreboardX = -390;
	float scoreboardY = WORLD_HEIGHT/2+50;

	long startTime = System.currentTimeMillis();

	@Override
	public void create () {
		world = new World(new Vector2(0, 0), true);

		batch = new SpriteBatch();
//		img = new Texture(getPixmapRoundedRectangle(WORLD_WIDTH, WORLD_HEIGHT, 32, Color.BLUE));
//		img = new Texture("data/fractal.png");
		img = new Texture("data/nebula.png");
		rink = new Sprite(img);
		rink.setPosition(-rink.getWidth()/2, -rink.getHeight()/2);

		//Set the height and width to just 50 pixels above the bottom of the screen so we can see the ege in the debug renderer
		float w = WORLD_WIDTH/PIXELS_TO_METERS;
		float h = WORLD_HEIGHT/PIXELS_TO_METERS;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		puckBody = world.createBody(bodyDef);

		BodyDef bodyDef21 = new BodyDef();
		bodyDef21.type = BodyDef.BodyType.DynamicBody;
		bodyDef21.position.set(-w/4, 0);
		touchBodyLeft = world.createBody(bodyDef21);

		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.DynamicBody;
		bodyDef2.position.set(w/4, 0);
		touchBodyRight = world.createBody(bodyDef2);

		Pixmap pixmap = new Pixmap((int)puckRadius*2+5, (int)puckRadius*2+5, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2);
//		pixmap.drawCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 1);
		Texture puckTexture = new Texture(pixmap);
		puck = new Sprite(puckTexture);

		pixmap = new Pixmap((int)touchRadius*2+5, (int)touchRadius*2+5, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 1);
//		pixmap.drawCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 1);
		Texture pointTexture = new Texture(pixmap);
		touchSprite = new Sprite(pointTexture);

		CircleShape shape = new CircleShape();
		shape.setRadius(puckRadius/PIXELS_TO_METERS);
		shape.setPosition(new Vector2(0, 0));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = puckDensity;
		fixtureDef.restitution = puckRestitution;
		fixtureDef.friction = puckFriction;
		puckBody.createFixture(fixtureDef);

		CircleShape touchShape = new CircleShape();
		touchShape.setRadius((touchRadius)/PIXELS_TO_METERS);
		touchShape.setPosition(new Vector2(0, 0));

		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = touchShape;
		fixtureDef2.density = density;
		fixtureDef2.restitution = restitution;
		fixtureDef2.friction = friction;
		touchBodyRight.createFixture(fixtureDef2);
		touchBodyLeft.createFixture(fixtureDef2);

		shape.dispose();
		touchShape.dispose();

		//bottom
		addBodyEdgeScreen(-w/2, -h/2, w/2, -h/2);
		//top
		addBodyEdgeScreen(-w/2, h/2, w/2, h/2);
		//top-left
		addBodyEdgeScreen(-w/2, h/2, -w/2, (GOAL_LENGTH/PIXELS_TO_METERS)/2);
		//bottom-left
		addBodyEdgeScreen(-w/2, -h/2, -w/2, -(GOAL_LENGTH/PIXELS_TO_METERS)/2);
		//top-right
		addBodyEdgeScreen(w/2, h/2, w/2, (GOAL_LENGTH/PIXELS_TO_METERS)/2);
		//bottom-right
		addBodyEdgeScreen(w/2, -h/2, w/2, -(GOAL_LENGTH/PIXELS_TO_METERS)/2);
		//corner-top-left
		addBodyEdgeScreen(-w/2, h/2-cornerRadius/PIXELS_TO_METERS, -w/2+cornerRadius/PIXELS_TO_METERS, h/2);
		//corner-bottom-left
		addBodyEdgeScreen(-w/2, -h/2+cornerRadius/PIXELS_TO_METERS, -w/2+cornerRadius/PIXELS_TO_METERS, -h/2);
		//corner-top-right
		addBodyEdgeScreen(w/2, h/2-cornerRadius/PIXELS_TO_METERS, w/2-cornerRadius/PIXELS_TO_METERS, h/2);
		//corner-bottom-right
		addBodyEdgeScreen(w/2-cornerRadius/PIXELS_TO_METERS, -h/2, w/2, -h/2+cornerRadius/PIXELS_TO_METERS);

		//Add centerline
		BodyDef bodyDef3 = new BodyDef();
		bodyDef3.type = BodyDef.BodyType.StaticBody;
		bodyDef3.position.set(0,0);

		FixtureDef fixtureDef3 = new FixtureDef();
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(0, h/2, 0, -h/2);
		fixtureDef3.shape = edgeShape;
		fixtureDef3.isSensor = true;

		centerline = world.createBody(bodyDef3);
		centerline.createFixture(fixtureDef3);
		edgeShape.dispose();

		//Create a Box2DDebugRenderer, this allows us to see the physics simulation
		//controlling the scene
		debugRenderer = new Box2DDebugRenderer();

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/repet.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = scoreboardSize;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!4
		font.setColor(Color.RED);

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(WORLD_WIDTH+borderWidth,WORLD_HEIGHT+borderWidth*2,camera);
		camera.translate(0,borderWidth-20);
//		viewport = new ExtendViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);

		Gdx.input.setInputProcessor(this);
	}

	private void addBodyEdgeScreen(Float v1x, Float v1y, Float v2x, Float v2y) {

		Gdx.app.log("ADD BODY EDGE", "v1x: "+v1x+", v1y: "+v1y+", v2x: "+v2x+", v2y: "+v2y);
		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.StaticBody;
		//bodyDef2.position.set(0, h-10/PIXELS_TO_METERS);
		bodyDef2.position.set(0,0);

		FixtureDef fixtureDef2 = new FixtureDef();
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(v1x, v1y, v2x, v2y);
		fixtureDef2.shape = edgeShape;

		bodyEdgeScreen = world.createBody(bodyDef2);
		bodyEdgeScreen.createFixture(fixtureDef2);
		edgeShape.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render () {
		final float dt = Gdx.graphics.getDeltaTime();

		camera.update();
		//Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		//Scale down the sprite batches projection matrix to box2D size
		debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);


		//Set the sprite's position from the updated physics puckBody location
		puck.setPosition((puckBody.getPosition().x*PIXELS_TO_METERS)-puck.getWidth()/2,
				(puckBody.getPosition().y*PIXELS_TO_METERS)-puck.getHeight()/2);
		touchSprite.setPosition((touchBodyRight.getPosition().x*PIXELS_TO_METERS)- touchSprite.getWidth()/2,
				(touchBodyRight.getPosition().y*PIXELS_TO_METERS)- touchSprite.getHeight()/2);
		//Ditto for rotation
//		puck.setRotation((float)Math.toDegrees(puckBody.getAngle()));

		update(dt);

		batch.begin();

		rink.draw(batch);
		puck.draw(batch);
		touchSprite.draw(batch);

		touchSprite.setPosition((touchBodyLeft.getPosition().x*PIXELS_TO_METERS)- touchSprite.getWidth()/2,
				(touchBodyLeft.getPosition().y*PIXELS_TO_METERS)- touchSprite.getHeight()/2);
		touchSprite.draw(batch);

		int player1score = scoreboard.getPlayerScore(0);
		int player2score = scoreboard.getPlayerScore(1);
		if(showScore) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			long second = (elapsedTime / 1000) % 60;
			long minute = (elapsedTime / (1000 * 60)) % 60;
			long hour = (elapsedTime / (1000 * 60 * 60)) % 24;

			String time = String.format("%02d:%02d", minute, second);

			scoreboardString = String.format("P1: %02d     "+time+"     P2: %02d", player1score, player2score);
		}

		font.draw(batch, scoreboardString, scoreboardX, scoreboardY);

		batch.end();

		//Now render the physics world using our scaled down matrix
		//Note, this is strictly optional and is, as the name suggests,
		//just for debugging purposes
		if(enableDebug) {
			debugRenderer.render(world, debugMatrix);
		}

	}

	private void setGoalString(int playerID) {
//		scoreboardString = "    GOAL FOR PLAYER 1 !!!";
		scoreboardString = "      PLAYER "+(playerID+1)+" GOAL !!!  ";
	}

	boolean showScore = true;
	double totalTime = 0;
	double goalDisplayTime = 2;
	double endGoalTime = 0;
	private void update(final float dt)
	{
		totalTime += dt;
		if(!showScore && totalTime > endGoalTime) {
			showScore = true;
		}

		float puckPosition = puckBody.getPosition().x*PIXELS_TO_METERS;
		if(puckPosition > WORLD_WIDTH/2+touchSprite.getWidth()/2) {
			scoreboard.incrementScoreCount(0);
			resetWorldBodies();
			showScore = false;
			setGoalString(0);
			endGoalTime = totalTime+goalDisplayTime;
		} else if(puckPosition < -WORLD_WIDTH/2-touchSprite.getWidth()/2) {
			scoreboard.incrementScoreCount(1);
			resetWorldBodies();
			showScore = false;
			setGoalString(1);
			endGoalTime = totalTime+goalDisplayTime;
		}

		float touchLeftPosition = touchBodyLeft.getPosition().x*PIXELS_TO_METERS;
		float touchRightPosition = touchBodyRight.getPosition().x*PIXELS_TO_METERS;

		//Reset mallet position if they have gone out of rink
		int trimSize = 5;
		if(touchLeftPosition > WORLD_WIDTH/2+touchSprite.getWidth()/2-trimSize ||
				touchLeftPosition < -WORLD_WIDTH/2-touchSprite.getWidth()/2+trimSize)  {
			resetLeftTouch();
		}
		if(touchRightPosition > WORLD_WIDTH/2+touchSprite.getWidth()/2-trimSize ||
				touchRightPosition < -WORLD_WIDTH/2-touchSprite.getWidth()/2+trimSize)  {
			resetRightTouch();
		}
	}

	public Pixmap getPixmapRoundedRectangle(float fwidth, float fheight, float fradius, Color color) {

		int width = (int) fwidth;
		int height = (int) fheight;
		int radius = (int) fradius;

		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);

		// Pink rectangle
		pixmap.fillRectangle(0, radius, pixmap.getWidth(), pixmap.getHeight()-2*radius);

		// Green rectangle
		pixmap.fillRectangle(radius, 0, pixmap.getWidth() - 2*radius, pixmap.getHeight());


		// Bottom-left circle
		pixmap.fillCircle(radius, radius, radius);

		// Top-left circle
		pixmap.fillCircle(radius, pixmap.getHeight()-radius, radius);

		// Bottom-right circle
		pixmap.fillCircle(pixmap.getWidth()-radius, radius, radius);

		// Top-right circle
		pixmap.fillCircle(pixmap.getWidth()-radius, pixmap.getHeight()-radius, radius);

		return pixmap;
	}

	@Override
	public void dispose() {
		img.dispose();
		world.dispose();
		batch.dispose();
		debugRenderer.dispose();
		font.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if(keycode == Input.Keys.SPACE) {
			resetWorldBodies();
		}

		return false;
	}

	private void resetWorldBodies() {
		Iterator it = mouseJointHashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			destroyMouseJoint((Integer)pair.getKey());
		}

		float w = WORLD_WIDTH/PIXELS_TO_METERS;

		puckBody.setTransform(0,0,0);
		puckBody.setLinearVelocity(0,0);
		puckBody.setAngularVelocity(0);

		touchBodyRight.setTransform(w/4, 0, 0);
		touchBodyRight.setLinearVelocity(0,0);
		touchBodyRight.setAngularVelocity(0);

		touchBodyLeft.setTransform(-w/4, 0, 0);
		touchBodyLeft.setLinearVelocity(0,0);
		touchBodyLeft.setAngularVelocity(0);
	}

	private void resetLeftTouch() {
		//Smoke the left joint if exists
		Iterator it = mouseJointHashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if(pair != null) {
				MouseJoint nextJoint = (MouseJoint) pair.getValue();
				if(nextJoint != null) {
					if (nextJoint.getBodyB() == touchBodyLeft) {
						destroyMouseJoint((Integer) pair.getKey());
					}
				}
			}
		}

		float w = WORLD_WIDTH/PIXELS_TO_METERS;

		touchBodyLeft.setTransform(-w/4, 0, 0);
//		touchBodyLeft.applyForceToCenter(0,3,true);

		touchBodyLeft.setLinearVelocity(0.1f,0);
		touchBodyLeft.setAngularVelocity(0);
	}

	private void resetRightTouch() {
		//Smoke the right joint if exists
		Iterator it = mouseJointHashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			if(pair != null) {
				MouseJoint nextJoint = (MouseJoint) pair.getValue();
				if(nextJoint != null) {
					if (nextJoint.getBodyB() == touchBodyRight) {
						destroyMouseJoint((Integer) pair.getKey());
					}
				}
			}
		}

		float w = WORLD_WIDTH/PIXELS_TO_METERS;

		touchBodyRight.setTransform(w/4, 0, 0);
//		touchBodyRight.applyForceToCenter(0,3,true);
		touchBodyRight.setLinearVelocity(-0.1f,0);
		touchBodyRight.setAngularVelocity(0);
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture (Fixture fixture) {
			// set hitbody to correct player mallet
			if (fixture.getBody() == touchBodyLeft) {
				Gdx.app.log("CB", "Touching left");
				hitBody = touchBodyLeft;
				return false;
			} else if (fixture.getBody() == touchBodyRight) {
				Gdx.app.log("CB", "Touching right");
				hitBody = touchBodyRight;
				return false;
			} else {
				Gdx.app.log("CB", "Touching nothing");
				return true;
			}
		}
	};
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// translate the mouse coordinates to world coordinates
		camera.unproject(testPoint.set(screenX, screenY, 0));

		Gdx.app.log("TD", "Mouse: ("+screenX+", "+screenY+")");
		Gdx.app.log("TD", "World: ("+testPoint.x+", "+testPoint.y+")");
		Gdx.app.log("TD", "Right Body: ("+ touchBodyRight.getPosition().x+", "+ touchBodyRight.getPosition().y+")");


		float boxSize = 0.5f;
		float w = WORLD_WIDTH/PIXELS_TO_METERS;
		float h = WORLD_HEIGHT/PIXELS_TO_METERS;
		Gdx.app.log("TD", "Meters: ("+ testPoint.x/PIXELS_TO_METERS+", "+ testPoint.y/PIXELS_TO_METERS+")");

		if(testPoint.y/PIXELS_TO_METERS < -h/2 || testPoint.y/PIXELS_TO_METERS > h/2
				|| testPoint.x/PIXELS_TO_METERS < -w/2 || testPoint.x/PIXELS_TO_METERS > w/2) {
//			puckBody.setTransform(0, 0, 0);
//			puckBody.setLinearVelocity(0, 0);
//			puckBody.setAngularVelocity(0);
//
//			touchBodyRight.setTransform(w / 4, 0, 0);
//			touchBodyRight.setLinearVelocity(0, 0);
//			touchBodyRight.setAngularVelocity(0);
//
//			touchBodyLeft.setTransform(-w / 4, 0, 0);
//			touchBodyLeft.setLinearVelocity(0, 0);
//			touchBodyLeft.setAngularVelocity(0);
		} else {
			// ask the world which bodies are within the given
			// bounding box around the mouse pointer
			hitBody = null;

			world.QueryAABB(callback, testPoint.x/PIXELS_TO_METERS - boxSize, testPoint.y/PIXELS_TO_METERS - boxSize,
					testPoint.x/PIXELS_TO_METERS + boxSize, testPoint.y/PIXELS_TO_METERS + boxSize);

			// if we hit something we create a new mouse joint
			// and attach it to the hit puckBody.
			if (hitBody != null) {
				MouseJointDef def = new MouseJointDef();
				def.bodyA = bodyEdgeScreen;
				def.bodyB = hitBody;
				def.collideConnected = true;
				def.target.set(hitBody.getPosition().x, hitBody.getPosition().y);
				def.maxForce = 1000.0f * hitBody.getMass();

				MouseJoint mouseJoint = (MouseJoint)world.createJoint(def);
				hitBody.setAwake(true);
				mouseJointHashMap.put(pointer, mouseJoint);
				mouseJoint.setTarget(target.set(testPoint.x / PIXELS_TO_METERS, testPoint.y / PIXELS_TO_METERS));
			}
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		// if a mouse joint exists we simply destroy it
		destroyMouseJoint(pointer);

		return true;
	}

	private void destroyMouseJoint(int pointer) {

		// if a mouse joint exists we simply destroy it
		MouseJoint pointerJoint = mouseJointHashMap.get(pointer);

		if (pointerJoint != null) {
			world.destroyJoint(pointerJoint);
			pointerJoint = null;
//			mouseJointHashMap.put(pointer, pointerJoint);
			mouseJointHashMap.remove(pointer);
		}

	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		camera.unproject(testPoint.set(screenX, screenY, 0));

		MouseJoint pointerJoint = mouseJointHashMap.get(pointer);

		if (pointerJoint != null) {
			pointerJoint.setTarget(target.set(testPoint.x / PIXELS_TO_METERS, testPoint.y / PIXELS_TO_METERS));
		}

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
