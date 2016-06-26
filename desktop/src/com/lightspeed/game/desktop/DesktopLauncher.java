package com.lightspeed.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lightspeed.airhockey.AirHockeyGame;
import com.lightspeed.examples.AirHockey2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 400;
		new LwjglApplication(new AirHockey2(), config);
//		new LwjglApplication(new AirHockeyGame(), config);
	}
}
