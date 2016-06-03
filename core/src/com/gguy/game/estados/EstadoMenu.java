package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.Gguy;
import com.gguy.game.Online.ComClient;
import com.gguy.game.Online.ComServer;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoMenu extends EstadoBase {
   // private Texture wallpapper;
    //private Texture btn1;
    private Botao btn1;
    private Botao btn2;
    private Botao btn3;
    private Botao btn4;
    private boolean alteracaoEstado;
    private final static String TAG = "infoMessage";
    public EstadoMenu(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper1.png"); //todo fazer defines disto para ficar bonito
        btn1 = new Botao("background/singleplayer.png",HEIGHT/2-HEIGHT/12);
        btn2 = new Botao("background/options.png",HEIGHT/2-HEIGHT/4);
        btn3 = new Botao("background/singleplayer.png",200,500);
        btn4 = new Botao("background/options.png",500,500);

        alteracaoEstado = true;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/kendrick.mp3"));
        music.setLooping(false);
        music.setVolume(0.8f);
        music.play();
    }

    @Override
    protected void handleInput() {//todo por coordenadas certas para os botoes :P
        Logger banana = new Logger(TAG,Logger.INFO); // works
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoJogo(emg));
            banana.info("Finito Main Menu");
        }
        if(Gdx.input.justTouched() && btn2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            //emg.remEstadoAct();
            emg.addEstado(new EstadoOpcoes(emg));
            //banana.info("Finito Main Menu");
        }
        if(Gdx.input.justTouched() && btn3.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Servidor");
            ComServer.mainServer();
        }
        if(Gdx.input.justTouched() && btn4.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Cliente");
            ComClient.mainClient();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //repor a posicao da sprite para a inicial (util visto que o jogo muda o campo de visao)
        if(alteracaoEstado){
            //para dar fix da camara pq isto e estupido
            camara.setToOrtho(false,WIDTH,HEIGHT);
            camara.position.x = WIDTH/2;
            camara.position.y = HEIGHT/2;
            camara.update();
            spriteB.setProjectionMatrix(camara.combined);
            alteracaoEstado = false;
        }
        spriteB.begin();
        spriteB.draw(wallpapper,0,0,WIDTH,HEIGHT);
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y);
        spriteB.draw(btn2.getButton(),btn2.getCoord().x,btn2.getCoord().y);
        spriteB.draw(btn3.getButton(),btn3.getCoord().x,btn3.getCoord().y);
        spriteB.draw(btn4.getButton(),btn4.getCoord().x,btn4.getCoord().y);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
        btn2.disposeButton();
        btn3.disposeButton();
        btn4.disposeButton();
        music.dispose();
    }

}
