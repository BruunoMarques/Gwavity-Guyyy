package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.Gguy;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoMenu extends EstadoBase {
   // private Texture wallpapper;
    //private Texture btn1;
    private final String nomeWallpaper = "background/wallpaper.png";
    private final String SinglePlayer = "utility/singleplayer.png";
    private final String Options = "utility/options.png";
    private final String Host = "utility/singleplayer.png";
    private final String Client = "utility/options.png";
    private Botao btn1;
    private Botao btn2;
    private Botao btn3;
    private Botao btn4;
    private boolean alteracaoEstado;
    private final static String TAG = "infoMessage";
    public EstadoMenu(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture(nomeWallpaper);
        btn1 = new Botao(SinglePlayer,WIDTH/16,HEIGHT/2-HEIGHT/16);
        btn2 = new Botao(Options,WIDTH/16,HEIGHT/2-HEIGHT/4);
        btn3 = new Botao(Host,WIDTH/2+WIDTH/5,HEIGHT/2-HEIGHT/16);
        btn4 = new Botao(Client,WIDTH/2+WIDTH/5,HEIGHT/2-HEIGHT/4);

        alteracaoEstado = true;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/kendrick.mp3"));
        music.setLooping(false);
        music.setVolume(this.emg.soundVolume);
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
        else if(Gdx.input.justTouched() && btn2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            //emg.remEstadoAct();
            emg.addEstado(new EstadoOpcoes(emg,music));
            //banana.info("Finito Main Menu");
        }
        else if(Gdx.input.justTouched() && btn3.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Servidor");
            emg.remEstadoAct();
            emg.addEstado(new EstadoHost(emg));
        }
        else if(Gdx.input.justTouched() && btn4.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Cliente");
            emg.remEstadoAct();
            emg.addEstado(new EstadoClient(emg));
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
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y,btn1.getWidth(),btn1.getHeight());
        spriteB.draw(btn2.getButton(),btn2.getCoord().x,btn2.getCoord().y,btn2.getWidth(),btn2.getHeight());
        spriteB.draw(btn3.getButton(),btn3.getCoord().x,btn3.getCoord().y,btn3.getWidth(),btn3.getHeight());
        spriteB.draw(btn4.getButton(),btn4.getCoord().x,btn4.getCoord().y,btn4.getWidth(),btn4.getHeight());
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
