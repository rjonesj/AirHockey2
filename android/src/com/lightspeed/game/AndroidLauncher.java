package com.lightspeed.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.lightspeed.airhockey.AirHockeyGame;
import com.lightspeed.examples.AirHockey2;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new AirHockey2(), config);
//		initialize(new AirHockeyGame(), config);
	}
}
