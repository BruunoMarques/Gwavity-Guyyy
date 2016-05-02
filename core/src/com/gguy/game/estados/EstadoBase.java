package com.gguy.game.estados;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Jonas on 30-04-2016.
 */
public abstract class EstadoBase {
    protected OrthographicCamera camara;
    protected Vector3 rato; //Wtf rato?
    protected EstadosManager emg;
    public static int WIDTH; //todo conjugar com tlm*/
    public static int HEIGHT;
    static{
        WIDTH = 1280;
        HEIGHT = 720;
    }
    protected EstadoBase(EstadosManager emg){
        camara = new OrthographicCamera();
        rato = new Vector3();
        this.emg = emg;
    }
    protected abstract void handleInput();
    public abstract void update(float dt);// dt é a intervalo de tempo de render
    public abstract void render(SpriteBatch spriteB);
    public abstract void freeMemory(); //dispose crap
    public static void setSize(int w, int h){
        WIDTH = w;
        HEIGHT = h;
    }
}
