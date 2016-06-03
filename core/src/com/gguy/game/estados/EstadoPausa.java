package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.Online.ComClient;
import com.gguy.game.Online.ComServer;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 02-06-2016.
 */
public class EstadoPausa extends EstadoBase {
    private Botao btn1;
    private Botao btn2;
    public EstadoPausa(EstadosManager emg) {
        super(emg);
        btn1 = new Botao("background/singleplayer.png",200,200);
        btn2 = new Botao("background/options.png",500,200);
    }

    @Override
    protected void handleInput() {//todo por coordenadas certas para os botoes :P
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        if(Gdx.input.justTouched() && btn2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        /*
        spriteB.begin();
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y);
        spriteB.draw(btn2.getButton(),btn2.getCoord().x,btn2.getCoord().y);
        spriteB.end();*/
    }

    @Override
    public void freeMemory() {
        btn1.disposeButton();
        btn2.disposeButton();
    }
}
