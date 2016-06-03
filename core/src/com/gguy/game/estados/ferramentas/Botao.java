package com.gguy.game.estados.ferramentas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/**
 * Created by Jonas on 09-05-2016.
 */
public class Botao {
    private Vector2 coord;
    private Vector2 coordView;
    private float height;
    private float width;
    Texture button;

    public Botao(String tex, float x, float y){
        button = new Texture(tex);
        coord = new Vector2(x,y);
        coordView = new Vector2(x,y);
        width = button.getWidth();
        height = button.getHeight();
    }
    public Botao(String tex, float y){
        button = new Texture(tex);
        float pos_meio = EstadoBase.WIDTH/2-button.getWidth()/2;
        coord = new Vector2(pos_meio,y);
        coordView = new Vector2(pos_meio,y);
        width = button.getWidth();
        height = button.getHeight();
    }

    public Texture getButton(){
        return button;
    }

    public Vector2 getCoord(){
        return coord;
    }

    public Vector2 getCoordView() {
        return coordView;
    }

    public void setViewPoint(float x, float y){
        coordView.set(x,y);
    }

    public void changeViewPosX(float x){
        coordView.x = x;
    }



    public void duplicateButtonSize(){
        width *=2;
        height*=2;
    }

    public boolean checkClick(float x, float y){
        double graphical_y = EstadoBase.HEIGHT - y - height; // fazer isto uma vez que o eixo positivo Y do click começa em cima, oposto ao do render.
        if(x > Math.abs(coord.x) && x < Math.abs(coord.x + width) //verificar se o click está dentro das boundaries do botao
                && graphical_y < Math.abs(coord.y) && graphical_y > Math.abs(coord.y - height))
            return true;
        else return false;
    }

    public void disposeButton(){
        button.dispose();
    }
}
