package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Jonas on 25-05-2016.
 */
public abstract class MapStruct implements Cloneable{

    protected ArrayList<Rectangle> Colisionbox;
    protected ArrayList<Vector2> Coordenadas;
    protected Texture Textura;

    public ArrayList<Rectangle> getColisionbox() {
        return Colisionbox;
    }

    public void setColisionbox(ArrayList<Rectangle> colisionbox) {
        Colisionbox = colisionbox;
    }

    public ArrayList<Vector2> getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(ArrayList<Vector2> coordenadas) {
        Coordenadas = coordenadas;
    }

    public Texture getTextura() {
        return Textura;
    }

    public void setTextura(Texture textura) {
        this.Textura = textura;
    }
    public abstract void reposition(float x);
    public abstract boolean ColideGuy(Rectangle player);
    public abstract MapStruct clone();
    public abstract void freeMemory();
    public MapStruct(float x){
        //todo nothing lol
        Colisionbox = new ArrayList<Rectangle>();
        Coordenadas = new ArrayList<Vector2>();
    }
}
