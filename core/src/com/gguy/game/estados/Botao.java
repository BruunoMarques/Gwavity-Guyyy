package com.gguy.game.estados;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Jonas on 09-05-2016.
 */
public class Botao {
    private Vector2 coord;
    private float height;
    private float width;
    Texture button;

    public Botao(String tex, float x, float y){
        button = new Texture(tex);
        coord = new Vector2(x,y);
        width = button.getWidth();
        height = button.getHeight();
    }

    public Texture getButton(){
        return button;
    }

    public Vector2 getCoord(){
        return coord;
    }

    public boolean checkClick(float x, float y){
        if(x > Math.abs(coord.x) && x < Math.abs(coord.x + width)
                && y < Math.abs(coord.y) && y > Math.abs(coord.y - height))
            return true;
        else return false;
    }

    public void disposeButton(){
        button.dispose();
    }
}
