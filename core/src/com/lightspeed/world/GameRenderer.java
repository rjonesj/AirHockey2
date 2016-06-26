package com.lightspeed.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.lightspeed.helpers.AssetLoader;
import com.lightspeed.helpers.InputHandler;
import com.lightspeed.tweenaccessors.Value;
import com.lightspeed.tweenaccessors.ValueAccessor;
import com.lightspeed.ui.SimpleButton;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private int midPointY = 200;
//    private float gameWidth = 1280, gameHeight = 720;
    private float gameWidth = 800, gameHeight = 400;
    private final float borderWidth = 40;
    private final float PIXELS_TO_METERS = 200;

    // Game Objects
//    private Bird bird;
//    private ScrollHandler scroller;
//    private Grass frontGrass, backGrass;
//    private Pipe pipe1, pipe2, pipe3;

    // Game Assets
    private Sprite startScreen;
    private TextureRegion bg;
    private Animation birdAnimation;
    private TextureRegion birdMid, birdDown, birdUp;
    private TextureRegion skullUp, skullDown, bar;
    private Texture texture;

    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();

    // Buttons
    private List<SimpleButton> menuButtons;

    public GameRenderer(GameWorld world) {
        myWorld = world;

        // The word "this" refers to this instance.
        // We are setting the instance variables' values to be that of the
        // parameters passed in from GameScreen.
//        this.gameHeight = gameHeight;
//        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
//                .getMenuButtons();
        InputHandler input = (InputHandler) Gdx.input.getInputProcessor();
        if(input != null) {
            this.menuButtons = input.getMenuButtons();
        } else {
            Gdx.app.log("GameRenderer", "getInputProcessor returned null!");
        }

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(gameWidth+borderWidth,gameHeight+borderWidth*2, camera);
        camera.translate(0,borderWidth-20);


        viewport = new ExtendViewport(gameWidth,gameHeight, camera);
        camera.position.set(0,0,0);
        Texture texture = new Texture(Gdx.files.internal("data/startscreen.png"));
        startScreen = new Sprite(texture);
        startScreen.setSize(gameWidth,gameHeight);
        startScreen.setPosition(-startScreen.getWidth()/2, -startScreen.getHeight()/2);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
        setupTweens();
    }

    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
    }

    private void initGameObjects() {
//        bird = myWorld.getBird();
//        scroller = myWorld.getScroller();
//        frontGrass = scroller.getFrontGrass();
//        backGrass = scroller.getBackGrass();
//        pipe1 = scroller.getPipe1();
//        pipe2 = scroller.getPipe2();
//        pipe3 = scroller.getPipe3();
    }

    private void initAssets() {
        bg = AssetLoader.bg;
//        startScreen = AssetLoader.startScreen;
        birdAnimation = AssetLoader.birdAnimation;
        birdMid = AssetLoader.bird;
        birdDown = AssetLoader.birdDown;
        birdUp = AssetLoader.birdUp;
        skullUp = AssetLoader.skullUp;
        skullDown = AssetLoader.skullDown;
        bar = AssetLoader.bar;
    }

//    private void drawGrass() {
//        // Draw the grass
//        batch.draw(grass, frontGrass.getX(), frontGrass.getY(),
//                frontGrass.getWidth(), frontGrass.getHeight());
//        batch.draw(grass, backGrass.getX(), backGrass.getY(),
//                backGrass.getWidth(), backGrass.getHeight());
//    }
//
//    private void drawSkulls() {
//        // Temporary code! Sorry about the mess :)
//        // We will fix this when we finish the Pipe class.
//
//        batch.draw(skullUp, pipe1.getX() - 1,
//                pipe1.getY() + pipe1.getHeight() - 14, 24, 14);
//        batch.draw(skullDown, pipe1.getX() - 1,
//                pipe1.getY() + pipe1.getHeight() + 45, 24, 14);
//
//        batch.draw(skullUp, pipe2.getX() - 1,
//                pipe2.getY() + pipe2.getHeight() - 14, 24, 14);
//        batch.draw(skullDown, pipe2.getX() - 1,
//                pipe2.getY() + pipe2.getHeight() + 45, 24, 14);
//
//        batch.draw(skullUp, pipe3.getX() - 1,
//                pipe3.getY() + pipe3.getHeight() - 14, 24, 14);
//        batch.draw(skullDown, pipe3.getX() - 1,
//                pipe3.getY() + pipe3.getHeight() + 45, 24, 14);
//    }
//
//    private void drawPipes() {
//        // Temporary code! Sorry about the mess :)
//        // We will fix this when we finish the Pipe class.
//        batch.draw(bar, pipe1.getX(), pipe1.getY(), pipe1.getWidth(),
//                pipe1.getHeight());
//        batch.draw(bar, pipe1.getX(), pipe1.getY() + pipe1.getHeight() + 45,
//                pipe1.getWidth(), midPointY + 66 - (pipe1.getHeight() + 45));
//
//        batch.draw(bar, pipe2.getX(), pipe2.getY(), pipe2.getWidth(),
//                pipe2.getHeight());
//        batch.draw(bar, pipe2.getX(), pipe2.getY() + pipe2.getHeight() + 45,
//                pipe2.getWidth(), midPointY + 66 - (pipe2.getHeight() + 45));
//
//        batch.draw(bar, pipe3.getX(), pipe3.getY(), pipe3.getWidth(),
//                pipe3.getHeight());
//        batch.draw(bar, pipe3.getX(), pipe3.getY() + pipe3.getHeight() + 45,
//                pipe3.getWidth(), midPointY + 66 - (pipe3.getHeight() + 45));
//    }
//
//    private void drawBirdCentered(float runTime) {
//        batch.draw(birdAnimation.getKeyFrame(runTime), 59, bird.getY() - 15,
//                bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
//                bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
//    }

//    private void drawBird(float runTime) {
//
//        if (bird.shouldntFlap()) {
//            batch.draw(birdMid, bird.getX(), bird.getY(),
//                    bird.getWidth() / 2.0f, bird.getHeight() / 2.0f,
//                    bird.getWidth(), bird.getHeight(), 1, 1, bird.getRotation());
//
//        } else {
//            batch.draw(birdAnimation.getKeyFrame(runTime), bird.getX(),
//                    bird.getY(), bird.getWidth() / 2.0f,
//                    bird.getHeight() / 2.0f, bird.getWidth(), bird.getHeight(),
//                    1, 1, bird.getRotation());
//        }
//    }

    private void drawMenuUI() {
        batch.draw(AssetLoader.zbLogo, 136 / 2 - 56, midPointY - 50,
                AssetLoader.zbLogo.getRegionWidth() / 1.2f,
                AssetLoader.zbLogo.getRegionHeight() / 1.2f);

        for (SimpleButton button : menuButtons) {
            button.draw(batch);
        }

    }

    private void drawScore() {
        int length = ("" + myWorld.getScore()).length();
        AssetLoader.shadow.draw(batch, "" + myWorld.getScore(),
                68 - (3 * length), midPointY - 82);
        AssetLoader.font.draw(batch, "" + myWorld.getScore(),
                68 - (3 * length), midPointY - 83);
    }

    private void drawHighScore() {
        AssetLoader.shadow.draw(batch, "High Score!", 19, 56);
        AssetLoader.font.draw(batch, "High Score!", 18, 55);
    }

    private void drawTryAgain() {
        AssetLoader.shadow.draw(batch, "Try again?", 23, 76);
        AssetLoader.font.draw(batch, "Try again?", 24, 75);
    }

    private void drawStartGame() {
        // Draw shadow first
        AssetLoader.shadow.draw(batch, "Start Game", (136 / 2)
                - (52), 76);
        // Draw text
        AssetLoader.font.draw(batch, "Start Game", (136 / 2)
                - (52 - 1), 75);
    }

    private void drawGameOver() {
        AssetLoader.shadow.draw(batch, "Game Over", 25, 56);
        AssetLoader.font.draw(batch, "Game Over", 24, 55);

        AssetLoader.shadow.draw(batch, "High Score:", 23, 106);
        AssetLoader.font.draw(batch, "High Score:", 22, 105);

        String highScore = AssetLoader.getHighScore() + "";

        // Draw shadow first
        AssetLoader.shadow.draw(batch, highScore, (136 / 2)
                - (3 * highScore.length()), 128);
        // Draw text
        AssetLoader.font.draw(batch, highScore, (136 / 2)
                - (3 * highScore.length() - 1), 127);
    }

    public void render(float delta, float runTime) {
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        // Draw Background color
//        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
//        shapeRenderer.rect(0, 0, 136, midPointY + 66);
//
//        // Draw Grass
//        shapeRenderer.setColor(111 / 255.0f, 186 / 255.0f, 45 / 255.0f, 1);
//        shapeRenderer.rect(0, midPointY + 66, 136, 11);
//
//        // Draw Dirt
//        shapeRenderer.setColor(147 / 255.0f, 80 / 255.0f, 27 / 255.0f, 1);
//        shapeRenderer.rect(0, midPointY + 77, 136, 52);
//
//        shapeRenderer.end();

        batch.begin();

        startScreen.draw(batch);

//        batch.draw(bg, 0, midPointY + 23, 136, 43);
//        drawGrass();
//        drawPipes();
//        drawSkulls();

        if (myWorld.isRunning()) {
//            drawBird(runTime);
            drawScore();
        } else if (myWorld.isReady()) {
//            drawBird(runTime);
            drawStartGame();

//            drawBirdCentered(runTime);
//            drawMenuUI();
        } else if (myWorld.isMenu()) {
//            Gdx.app.log("RENDER", "In menu!");
//            drawBird(runTime);
//            drawStartGame();

//            drawBirdCentered(runTime);
//            drawMenuUI();
        } else if (myWorld.isGameOver()) {
//            drawBird(runTime);
            drawScore();
            drawGameOver();
            drawTryAgain();;
        } else if (myWorld.isHighScore()) {
//            drawBird(runTime);
            drawScore();
//            drawGameOver();
            drawHighScore();
            drawTryAgain();;
        }

        batch.end();
//        drawTransition(delta);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void drawTransition(float delta) {
        if (alpha.getValue() > 0) {
            manager.update(delta);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, alpha.getValue());
            shapeRenderer.rect(0, 0, 136, 300);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
}
