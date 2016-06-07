package com.gguy.game.estados;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.glass.ui.View;

/**
 * Created by Jonas on 30-04-2016.
 * EstadoBase é uma classe abstrata, onde todos os outros estados derivam
 * Esta contem informacao comum a todas os outros estados
 * Esta contem uns metodos estaticos, relativos a resolucao do ecra do dispositivo
 */
public abstract class EstadoBase {
    protected OrthographicCamera camara;
    protected Viewport viewport;
    protected EstadosManager emg;
    protected Texture wallpapper;
    protected Music music;
    public static int WIDTH;
    public static int HEIGHT;
    public static float H_RES;
    public static float W_RES;
    static{
        //nao mudar!!
        WIDTH = 1280;
        HEIGHT = 720;
        W_RES = (float)WIDTH/1280;
        H_RES = (float)HEIGHT/720;
    }

    /**
     * inicializa certos parametros essencias para todos as derivantes desta classe
     * @param emg estado manager associado, que vai tratar da/das classes
     */
    protected EstadoBase(EstadosManager emg){
        camara = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH,HEIGHT,camara);
        this.emg = emg;
    }
    protected abstract void handleInput();
    public abstract void update(float dt);// dt é a intervalo de tempo de render
    public abstract void render(SpriteBatch spriteB);
    /**
     * responsavel por libertar memoria alocada pelos estados
     */
    public abstract void freeMemory(); //dispose everything that eats memory
    /**
     * funcao exexcutada inicialmente pela aplicacao android
     * Esta vai inicializar os valores estaticos, de acordo com a resolucao do tlm
     * Esta e essencial para manter as texturas na posicao e tamanhos certos, de tlm para tlm
     * @param w width do tlm
     * @param h height do tlm
     */
    public static void setSize(int w, int h){
        WIDTH = w;
        HEIGHT = h;
        W_RES = (float)WIDTH/1280;
        H_RES = (float)HEIGHT/720;
    }
}
