package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 02-06-2016.
 */
public class EstadoPausa extends EstadoBase {
    private Botao btn2;
    public EstadoPausa(EstadosManager emg) {
        super(emg);
        btn2 = new Botao("background/backbutton.png",WIDTH/2-WIDTH/12,HEIGHT*3/4 + HEIGHT/8);
        btn2.duplicateButtonSize();
    }

    @Override
    protected void handleInput() {//todo por coordenadas certas para os botoes :P

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
        spriteB.begin();
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        btn2.disposeButton();
    }
}
