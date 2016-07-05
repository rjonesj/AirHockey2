package com.lightspeed.world;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class GameWorld {
//    private Bird bird;
//    private ScrollHandler scroller;
//    private Rectangle ground;
    private GameState currentState;
    private int score = 0;
    private float runTime = 0;

    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    public GameWorld() {
        currentState = GameState.MENU;
//        this.midPointY = midPointY;
        // Initialize bird here
//        bird = new Bird(33, midPointY - 5, 17, 12);
        //Initialize the scroller
        //The grass should start 66 pixels below the midPointY
//        scroller = new ScrollHandler(this, midPointY + 66);
//        ground = new Rectangle(0, midPointY + 66, 136, 11);

    }

    public void update(float delta) {

        runTime += delta;

        switch (currentState) {
            case READY:
            case MENU:
//                updateReady(delta);
                break;
            case RUNNING:
//                updateRunning(delta);
                break;
            default:
                break;
        }
    }

//    private void updateReady(float delta) {
//        bird.updateReady(runTime);
//        scroller.updateReady(delta);
//    }
//
//    public void updateRunning(float delta) {
//
//        // Add a delta cap so that if our game takes too long
//        // to update, we will not break our collision detection.
//        if (delta > .15f) {
//            delta = .15f;
//        }
//
//        bird.update(delta);
//        scroller.update(delta);
//
//        if (scroller.collides(bird) && bird.isAlive()) {
//            scroller.stop();
//            bird.die();
//            AssetLoader.dead.play();
//        }
//
//        if (Intersector.overlaps(bird.getBoundingCircle(), ground)) {
//            scroller.stop();
//            bird.die();
//            bird.decelerate();
//            currentState = GameState.GAMEOVER;
//
//            if (score > AssetLoader.getHighScore()) {
//                AssetLoader.setHighScore(score);
//                currentState = GameState.HIGHSCORE;
//            }
//        }
//    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isHighScore() {
        return currentState == GameState.HIGHSCORE;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public void restart() {
        currentState = GameState.READY;
        score = 0;
//        bird.onRestart(midPointY - 5);
//        scroller.onRestart();
        currentState = GameState.READY;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int increment) {
        score += increment;
    }

//    public Bird getBird() {
//        return bird;
//    }
//
//    public ScrollHandler getScroller() {
//        return scroller;
//    }

}