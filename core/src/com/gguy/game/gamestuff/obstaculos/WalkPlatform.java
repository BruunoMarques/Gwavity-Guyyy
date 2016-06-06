package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.EstadoBase;

/**
 * Created by Jonas on 02-05-2016.
 */
public class WalkPlatform extends MapStruct{ //todo fazer class mae chamada tipo colisao qq coisa. Depurar isto. tá depurado
    private Texture platf;
    //eventualmente um random
    private Vector2 partCima, partBaixo;
    private Rectangle colisaoCima, colisaoBaixo;
    public final float PLATF_WIDTH;
    private final static String TAG = "infoMessage";

    public WalkPlatform(float x){
        super(x);
        partCima = new Vector2(x, EstadoBase.HEIGHT/2+75);
        partBaixo = new Vector2(x, EstadoBase.HEIGHT/4);
        platf = new Texture("map/walkplat.png");
        PLATF_WIDTH = platf.getWidth()*EstadoBase.W_RES;

        colisaoCima = new Rectangle(partCima.x,partCima.y,platf.getWidth()*EstadoBase.W_RES,platf.getHeight()*EstadoBase.H_RES);
        colisaoBaixo = new Rectangle(partBaixo.x,partBaixo.y,platf.getWidth()*EstadoBase.W_RES,platf.getHeight()*EstadoBase.H_RES);

        Textura = platf;
        Colisionbox.add(colisaoCima);
        Coordenadas.add(partCima);
        Colisionbox.add(colisaoBaixo);
        Coordenadas.add(partBaixo);
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
        colisaoCima.setPosition(partCima);
        colisaoBaixo.setPosition(partBaixo);
    }

    public boolean ColideGuy(Rectangle player){
        if(player.overlaps(colisaoCima))
        {
            lastColided = colisaoCima;
            return true;
        }
        if(player.overlaps(colisaoBaixo))
        {
            lastColided = colisaoBaixo;
            return true;
        }
        return false;
    }

    public void freeMemory(){
        platf.dispose();
    }

    public WalkPlatform clone(){
        return new WalkPlatform(0);
    }
}
