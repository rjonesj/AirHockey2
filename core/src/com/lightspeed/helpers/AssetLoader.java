package com.lightspeed.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class AssetLoader {

    public static Texture texture, logoTexture;

    public static TextureRegion logo, zbLogo, playButtonUp, playButtonDown, bg, grass;
    public static TextureRegion bird, birdDown, birdUp;
    public static TextureRegion skullUp, skullDown, bar;

    public static Animation birdAnimation;

    public static Sound dead, flap, coin, bgSound;

    public static Music bgMusic;

    public static BitmapFont font, shadow;

    public static Preferences prefs;

    public static Sprite startScreen;

    public static void load() {

        logoTexture = new Texture(Gdx.files.internal("zbird/logo.png"));
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        logo = new TextureRegion(logoTexture, 0, 0, 512, 114);

        texture = new Texture(Gdx.files.internal("zbird/texture.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        playButtonUp = new TextureRegion(texture, 0, 83, 29, 16);
        playButtonDown = new TextureRegion(texture, 29, 83, 29, 16);
        playButtonUp.flip(false, true);
        playButtonDown.flip(false, true);

        zbLogo = new TextureRegion(texture, 0, 55, 135, 24);
        zbLogo.flip(false, true);

        bg = new TextureRegion(texture, 0, 0, 136, 43);
        bg.flip(false, true);

        grass = new TextureRegion(texture, 0, 43, 143, 11);
        grass.flip(false, true);

        birdDown = new TextureRegion(texture, 136, 0, 17, 12);
        birdDown.flip(false, true);

        bird = new TextureRegion(texture, 153, 0, 17, 12);
        bird.flip(false, true);

        birdUp = new TextureRegion(texture, 170, 0, 17, 12);
        birdUp.flip(false, true);

        TextureRegion[] birds = { birdDown, bird, birdUp };
        birdAnimation = new Animation(0.06f, birds);
        birdAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        skullUp = new TextureRegion(texture, 192, 0, 24, 14);
        // Create by flipping existing skullUp
        skullDown = new TextureRegion(skullUp);
        skullDown.flip(false, true);

        bar = new TextureRegion(texture, 136, 16, 22, 3);
        bar.flip(false, true);

        texture = new Texture(Gdx.files.internal("data/startscreen.png"));
        startScreen = new Sprite(texture);
        startScreen.setPosition(-startScreen.getWidth()/2, -startScreen.getHeight()/2);

        dead = Gdx.audio.newSound(Gdx.files.internal("zbird/dead48.wav"));
        flap = Gdx.audio.newSound(Gdx.files.internal("zbird/flap48.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("zbird/coin48.wav"));

        font = new BitmapFont(Gdx.files.internal("zbird/text.fnt"));
        font.getData().setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("zbird/shadow.fnt"));
        shadow.getData().setScale(.25f, -.25f);

        // Create (or retrieve existing) preferences file
        prefs = Gdx.app.getPreferences("ZombieBird");
        // Provide default high score of 0
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        //Put on some tunes
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("zbird/2ndballad.wav"));
        bgMusic.setLooping(true);
        bgMusic.play();
    }

    // Receives an integer and maps it to the String highScore in prefs
    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    // Retrieves the current high score
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void dispose() {
        // We must dispose of the texture when we are finished.
        texture.dispose();
        logoTexture.dispose();

        //Dispose sounds
        dead.dispose();
        flap.dispose();
        coin.dispose();
//        bgSound.dispose();

        //Dispose music
        bgMusic.dispose();

        //dispose bitmapfonts
        font.dispose();
        shadow.dispose();
    }
}
