package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 14-05-2016.
 */
public class EstadoOpcoes extends EstadoBase {

    private Botao btn1;
    private Slider slider;
    private final static String TAG = "infoMessage";

    public EstadoOpcoes(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper1.png"); //todo fazer defines disto para ficar bonito
        btn1 = new Botao("background/options.png",200,200);

        //todo atlas para criar skin apra crar slider xd slider = new Slider(0,1,100,test);
    }

    @Override
    protected void handleInput() {
        Logger banana = new Logger(TAG,Logger.INFO); // works
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            banana.info("Finito Menu Opcoes");
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.begin();
        spriteB.draw(wallpapper,0,0,WIDTH,HEIGHT);
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
    }

}