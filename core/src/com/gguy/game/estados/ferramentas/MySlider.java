package com.gguy.game.estados.ferramentas;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;

/**
 * Created by Jonas on 18-05-2016.
 */
public class MySlider {
    private Vector2 coord;
    private Vector2 coordKnob;
    private float width;
    private float height;

    private float width2;
    private float height2;

    private int nOptions;
    private Texture line;
    private Texture knob;
    private boolean isPressed;

    public MySlider(int nOptions,float x, float y){
        this.nOptions = nOptions;
        coord = new Vector2(x,y);
        coordKnob = new Vector2(x,y);
        line = new Texture("utility/slider_line.png");
        knob = new Texture("utility/slider_knob.png");
        width = line.getWidth()*EstadoBase.W_RES;
        height = knob.getHeight()*EstadoBase.H_RES;

        width2 = knob.getWidth()*EstadoBase.W_RES;
        height2 = line.getHeight()*EstadoBase.H_RES;

        isPressed = false;
    }

    public Texture getLine(){
        return line;
    }

    public Texture getKnob(){
        return knob;
    }

    public Vector2 getCoords(){
        return coord;
    }

    public Vector2 getCoordKnob(){
        return coordKnob;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth2() {
        return width2;
    }

    public float getHeight2() {
        return height2;
    }

    public boolean checkClick(float x, float y){
        double graphical_y = EstadoBase.HEIGHT - y - height; // fazer isto uma vez que o eixo positivo Y do click começa em cima, oposto ao do render.
        if(x > Math.abs(coordKnob.x) && x < Math.abs(coordKnob.x + width2)
                && graphical_y < Math.abs(coordKnob.y) && graphical_y > Math.abs(coordKnob.y - height)){
            isPressed = true;
            return true;
        }
        else{
            isPressed = false;
            return false;
        }
    }

    public void arrastaKnob(float x){
        if(isPressed){
            if(x > (coord.x - width2/8) && x < (coord.x + width + width2/8))
            coordKnob.x = x-width2/2;
        }
    }

    public int getOptionSelected(float x){
        float size_options = width/nOptions;
        float pos = x-coord.x;
        int option = 0;
        for(int i =0;i<nOptions;i++)
            if(pos >= i*size_options && pos < (i+1)*size_options)
                option = i;
        return option;
    }

    public void disposeSlider(){
        line.dispose();
        knob.dispose();
    }
}
