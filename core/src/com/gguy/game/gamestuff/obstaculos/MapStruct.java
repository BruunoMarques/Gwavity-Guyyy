package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Jonas on 25-05-2016.
 */
public abstract class MapStruct {

    protected ArrayList<Rectangle> Colisionbox;
    protected ArrayList<Vector2> Coordenadas;
    protected ArrayList<Texture> Texturas;

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

    public ArrayList<Texture> getTexturas() {
        return Texturas;
    }

    public void setTexturas(ArrayList<Texture> texturas) {
        Texturas = texturas;
    }
    public abstract void reposition(float x);

    public MapStruct(float x){
        //todo nothing lol
        Colisionbox = new ArrayList<Rectangle>();
        Texturas = new ArrayList<Texture>();
        Coordenadas = new ArrayList<Vector2>();
    }


}
