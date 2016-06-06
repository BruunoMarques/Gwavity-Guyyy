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
 */
public abstract class EstadoBase {
    protected OrthographicCamera camara;
    protected Viewport viewport;
    protected Vector3 rato; //Wtf rato?
    protected EstadosManager emg;
    protected Texture wallpapper; //todo isto é temp
    protected Music music;
    public static int WIDTH;
    public static int HEIGHT;
    public static float H_RES;
    public static float W_RES;
    static{
      //  WIDTH = 1920;
      //  HEIGHT = 1080;
        WIDTH = 1280;
        HEIGHT = 720;
      //  WIDTH = 800;
      //  HEIGHT = 400;
        W_RES = (float)WIDTH/1280;
        H_RES = (float)HEIGHT/720;

    }
    protected EstadoBase(EstadosManager emg){
        camara = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH,HEIGHT,camara);
        rato = new Vector3();
        this.emg = emg;
    }
    protected abstract void handleInput();
    public abstract void update(float dt);// dt é a intervalo de tempo de render
    public abstract void render(SpriteBatch spriteB);
    public abstract void freeMemory(); //dispose everything that eats memory
    public static void setSize(int w, int h){
        WIDTH = w;
        HEIGHT = h;
        W_RES = (float)WIDTH/1280;
        H_RES = (float)HEIGHT/720;
    }
}
