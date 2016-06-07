package com.gguy.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gguy.game.Gguy;
import com.gguy.game.estados.EstadoBase;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = EstadoBase.WIDTH; //ignora nao interessa
        config.height = EstadoBase.HEIGHT;
		new LwjglApplication(new Gguy(), config);

	}
}
