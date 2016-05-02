package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;

/**
 * Created by Jonas on 02-05-2016.
 */
public class WalkPlatform {
    private Texture platf;
    //eventualmente um random
    private Vector2 partCima, partBaixo;
    private Rectangle colisaoCima, colisaoBaixo;
    public final int PLATF_WIDTH;

    public WalkPlatform(float x){
        partCima = new Vector2(x, EstadoBase.HEIGHT/2+100);
        partBaixo = new Vector2(x, EstadoBase.HEIGHT/2-150);
        platf = new Texture("walkplat.png");
        PLATF_WIDTH = platf.getWidth();

        colisaoCima = new Rectangle(partCima.x,partCima.y,platf.getWidth(),platf.getHeight());
        colisaoBaixo = new Rectangle(partBaixo.x,partBaixo.y,platf.getWidth(),platf.getHeight());
    }

    public Texture getPlatf() {
        return platf;
    }

    public Vector2 getPartCima() {
        return partCima;
    }

    public Vector2 getPartBaixo() {
        return partBaixo;
    }

    public void reposition(float x){
        partCima.x = x;
        partBaixo.x = x;
        colisaoCima.setPosition(partCima.x,partCima.y);
        colisaoBaixo.setPosition(partBaixo.x,partBaixo.y);
    }

    public float ColideGuy(Rectangle player){
        if(player.overlaps(colisaoCima))return partCima.y - platf.getHeight() - 1;
        if(player.overlaps(colisaoBaixo))return partBaixo.y + player.getHeight() + 1;
        else return 0;
    }

    public void freeMemory(){
        platf.dispose();
    }
}
