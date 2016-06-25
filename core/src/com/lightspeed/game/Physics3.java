package com.lightspeed.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ricky Jones Jr on 5/20/2016.
 */
public class Physics3 extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    Body bodyEdgeScreen;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    BitmapFont font;

    float torque = 0.0f;
    boolean drawSprite = true;
    final float PIXELS_TO_METERS = 100f;

    @Override
    public void create() {

        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);

//        sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
        sprite.setPosition(0, 0);
//        sprite.setSize(50, 50);

        world = new World(new Vector2(0, 0), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set((sprite.getX()+ sprite.getWidth()/2)/ PIXELS_TO_METERS,
//                (sprite.getY()+ sprite.getHeight()/2)/ PIXELS_TO_METERS);
        bodyDef.position.set((sprite.getX())/ PIXELS_TO_METERS,
                (sprite.getY())/ PIXELS_TO_METERS);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth()/2) / PIXELS_TO_METERS,
                (sprite.getHeight()/2) / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = .001f;

        body.createFixture(fixtureDef);
        shape.dispose();

        //Set the height and width to just 50 pixels above the bottom of the screen so we can see the ege in the debug renderer
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS-(50/PIXELS_TO_METERS);
        float h = (Gdx.graphics.getHeight()/PIXELS_TO_METERS)-(50/PIXELS_TO_METERS);

        //bottom
        addBodyEdgeScreen(-w/2, -h/2, w/2, -h/2);
        //top
        addBodyEdgeScreen(-w/2, h/2, w/2, h/2);
        //left
        addBodyEdgeScreen(-w/2, h/2, -w/2, -h/2);
        //right
        addBodyEdgeScreen(w/2, h/2, w/2, -h/2);


        Gdx.input.setInputProcessor(this);

        //Create a Box2DDebugRenderer, this allows us to see the physics simulation
        //controlling the scene
        debugRenderer = new Box2DDebugRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    private float elapsed = 0;
    @Override
    public void render() {
        camera.update();
        //Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        //Apply torque to the physics puckBody.  At start this is 0 and will do nothing.
        //Controlled with [] keys.
        //Torque is applied per frame instead of just once
        body.applyTorque(torque, true);

        //Set the sprite's position from the updated physics puckBody location
        sprite.setPosition((body.getPosition().x*PIXELS_TO_METERS)-sprite.getWidth()/2,
                (body.getPosition().y*PIXELS_TO_METERS)-sprite.getHeight()/2);
        //Ditto for rotation
        sprite.setRotation((float)Math.toDegrees(body.getAngle()));

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        //Scale down the sprite batches projection matrix to box2D size
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS, PIXELS_TO_METERS, 0);

        batch.begin();

        if(drawSprite) {
            batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(),
                    sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
        }

        font.draw(batch, "Restitution: "+body.getFixtureList().first().getRestitution(), -Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

        batch.end();

        //Now render the physics world using our scaled down matrix
        //Note, this is strictly optional and is, as the name suggests,
        //just for debugging purposes
        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public void dispose() {
        img.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {


        //On right or left arrow, set the velocity at a fixed rate in that direction
        if(keycode == Input.Keys.RIGHT) {
//            puckBody.setLinearVelocity(1f, 0f);
            body.applyForceToCenter(100f, 0f, true);
        }
        if(keycode == Input.Keys.LEFT) {
            body.applyForceToCenter(-100f, 0f, true);
        }
        if(keycode == Input.Keys.UP) {
            body.applyForceToCenter(0f, 100f, true);
        }
        if(keycode == Input.Keys.DOWN) {
            body.applyForceToCenter(0f, -100f, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        //On brackets [] apply torque, either clock or counterclockwise
        if(keycode == Input.Keys.RIGHT_BRACKET) {
            torque += 0.1f;
        }
        if(keycode == Input.Keys.LEFT_BRACKET) {
            torque -= 0.1f;
        }

        //Remove the torque using backslash
        if(keycode == Input.Keys.BACKSLASH) {
            torque = 0.0f;
        }

        //If user hits spacebar, reset everything back to normal
        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.NUM_2) {
            body.setLinearVelocity(0f, 0f);
            body.setAngularVelocity(0f);
            torque = 0f;
            sprite.setPosition(0f, 0f);
            body.setTransform(0f, 0f, 0f);
        }

        if(keycode == Input.Keys.COMMA) {
            body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()-0.1f);
        }

        if(keycode == Input.Keys.PERIOD) {
            body.getFixtureList().first().setRestitution(body.getFixtureList().first().getRestitution()+0.1f);
        }

        //The ESC key toggles the visibility of the sprite
        //Allows user to see the physics debug info
        if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1) {
            drawSprite = !drawSprite;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * On touch we apply force from the direction of the users touch.
     * This could result in the object "spinning"
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //boost it!
        float boost = 0.5f;

        Gdx.app.log("TOUCHDOWN", "screenX: "+screenX+", screenY: "+screenY);


        float mousePosx = screenX - (Gdx.graphics.getWidth()/2);
        float mousePosy = -(screenY - (Gdx.graphics.getHeight()/2));
        Gdx.app.log("TOUCHDOWN", "centerX: "+mousePosx+", centerY: "+mousePosy);

        Gdx.app.log("TOUCHDOWN", "Body X: "+body.getPosition().x+", Body Y: "+body.getPosition().y);

        Vector3 coordinates = new Vector3();
        coordinates = new Vector3(screenX,screenY,0); //where x,y,0 is mouse coordinates
        Vector3 value = new Vector3();
        value = camera.unproject(coordinates);

        Gdx.app.log("TOUCHDOWN", "unproject screenX: "+value.x+", unproject screenY: "+value.y);

//        puckBody.applyForce(boost,boost,screenX,screenY,true);

//        puckBody.applyForce(boost,boost,value.x,value.y,true);

//        puckBody.applyForceToCenter((screenX*boost), -(screenY*boost), true);

        float appliedForceX = -(mousePosx*boost);
        float appliedForceY = -(mousePosy*boost);

        Gdx.app.log("TOUCHDOWN", "Applying force: ("+appliedForceX+", "+appliedForceY+")");

        body.applyForceToCenter(appliedForceX, appliedForceY, true);
        body.applyForce(1f,1f,screenX,screenY,true);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
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
