package com.gguy.game.estados;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gguy.game.estados.ferramentas.PlayList;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.gguy.game.estados.ferramentas.SkinList;

import java.util.Stack;

/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadosManager {
    private Stack<EstadoBase> estados;//ou outra estrutura idk

    public SkinList skins;
    public SkinInfo skinSelected;
    public PlayList musicSelected;
    public float soundVolume = 0.3f;

    public EstadosManager(){
        estados = new Stack<EstadoBase>();
        musicSelected = new PlayList();
        skins = new SkinList();
        skinSelected = skins.getSelectedSkin();
    }

    public void addEstado(EstadoBase estado){
        estados.push(estado);
    }

    public void remEstadoAct(){ //remove estado atual
        estados.pop().freeMemory();
    }

    public void updateEstado(float dt) {
        estados.peek().update(dt);
    }

    public void renderEstado(SpriteBatch spriteB){
        estados.peek().render(spriteB);
    }
}
