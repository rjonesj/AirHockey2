package com.lightspeed.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.lightspeed.airhockey.AirHockeyGame;
import com.lightspeed.helpers.InputHandler;
import com.lightspeed.world.GameRenderer;
import com.lightspeed.world.GameWorld;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime = 0;
    private AirHockeyGame game;

    public GameScreen(AirHockeyGame game) {

        this.game = game;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 800;
        float gameHeight = 400;

        world = new GameWorld(); // initialize world
        Gdx.input.setInputProcessor(new InputHandler(world));
        renderer = new GameRenderer(world, game); // initialize renderer

        Gdx.app.log("GameScreen", "ScreenWidth: " + screenWidth + ", screenHeight: " + screenHeight + ", gameWidth: " + gameWidth + ", gameHeight: " + gameHeight);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

        Gdx.app.log("GameScreen", "resizing");
        renderer.resize(width, height);
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        // Leave blank
    }
}
