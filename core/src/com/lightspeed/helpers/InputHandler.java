package com.lightspeed.helpers;

import com.badlogic.gdx.*;
import com.lightspeed.ui.SimpleButton;
import com.lightspeed.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class InputHandler implements InputProcessor {

    private GameWorld myWorld;
//    private Bird myBird;

    // Ask for a reference to the Bird when InputHandler is created.
    public InputHandler(GameWorld myWorld) {
        // myBird now represents the gameWorld's bird.
        this.myWorld = myWorld;
//        myBird = myWorld.getBird();
    }



    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        System.out.println(screenX + " " + screenY);
        if (myWorld.isMenu()) {
            myWorld.start();
        }

//        myBird.onClick();

        if (myWorld.isGameOver() || myWorld.isHighScore()) {
            myWorld.restart();
        }

        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (myWorld.isMenu()) {
            myWorld.start();
            return true;
        }

        return false;
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {

        // Can now use Space Bar to play the game
        if (keycode == Input.Keys.SPACE) {

            if (myWorld.isMenu()) {
                myWorld.start();
            }

//            myBird.onClick();

            if (myWorld.isGameOver() || myWorld.isHighScore()) {
                myWorld.restart();
            }

        }

        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}