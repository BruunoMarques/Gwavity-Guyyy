package com.gguy.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.gguy.game.estados.EstadoBase;
import com.gguy.game.estados.EstadoMenu;
import com.gguy.game.estados.EstadosManager;

public class Gguy extends ApplicationAdapter {
    private EstadosManager estadosJogo;
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
    //    Gdx.gl.glClearColor(1, 0, 0, 1);
        estadosJogo = new EstadosManager();
        estadosJogo.addEstado(new EstadoMenu(estadosJogo));
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//refresh screen
	}

	@Override
	public void render () {
	//	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//refresh screen
        estadosJogo.updateEstado(Gdx.graphics.getDeltaTime());
        estadosJogo.renderEstado(batch);
	}
}
