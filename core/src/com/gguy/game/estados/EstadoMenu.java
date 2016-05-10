package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;


/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoMenu extends EstadoBase {
   // private Texture wallpapper;
    //private Texture btn1;
    private Botao btn1;
    private TextButton singlePlayer;
    private final static String TAG = "infoMessage";
    public EstadoMenu(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper1.png"); //todo fazer defines disto para ficar bonito
        btn1 = new Botao("background/singleplayer.png",200,200);

        music = Gdx.audio.newMusic(Gdx.files.internal("music/kendrick.mp3"));
        music.setLooping(false);
        music.setVolume(0.8f);
        music.play();
    }

    @Override
    protected void handleInput() {//todo por coordenadas certas para os botoes :P
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoJogo(emg));
            Logger banana = new Logger(TAG,Logger.INFO); // works
            banana.info("Finito Main Menu");
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
        music.dispose();
    }

}
