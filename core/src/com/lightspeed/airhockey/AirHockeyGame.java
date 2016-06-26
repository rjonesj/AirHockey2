package com.lightspeed.airhockey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.lightspeed.helpers.AssetLoader;
import com.lightspeed.screens.SplashScreen;

/**
 * Created by Ricky Jones Jr on 6/26/2016.
 */
public class AirHockeyGame extends Game {

    @Override
    public void create() {
        Gdx.app.log("AirHockeyGame", "Created");
        AssetLoader.load();
        setScreen(new SplashScreen(this));
    }
}
