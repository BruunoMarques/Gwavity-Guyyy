package com.gguy.game.estados;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadosManager {
    private Stack<EstadoBase> estados;//ou outra estrutura idk

    public EstadosManager(){
        estados = new Stack<EstadoBase>();
    }

    public void addEstado(EstadoBase estado){
        estados.push(estado);
    }

    public void remEstadoAct(){ //remove estado atual
        estados.pop().freeMemory();
    }

    public void updateEstado(float dt){
        estados.peek().update(dt);
    }

    public void renderEstado(SpriteBatch spriteB){
        estados.peek().render(spriteB);
    }
}
