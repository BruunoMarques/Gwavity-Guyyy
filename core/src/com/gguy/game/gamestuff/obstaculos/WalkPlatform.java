package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.EstadoBase;

/**
 * Created by Jonas on 02-05-2016.
 * Classe que representa uma estrutura do mapa
 * Esta e definida por um chao e um teto, tendo ambas uma respetiva caixa de colisao
 */
public class WalkPlatform extends MapStruct{
    private Texture platf;

    private Vector2 partCima, partBaixo;
    private Rectangle colisaoCima, colisaoBaixo;
    public final float PLATF_WIDTH;
    private final static String TAG = "infoMessage";

    /**
     * Construtor responsavel por inicializar o WalkPlatform
     * Este cria a textura e 2 caixas de colisao, que vao representar as 2 partes do WalkPlatform, bem como posiciona-las
     * num sitio inicial
     * @param x posicao inicial
     */
    public WalkPlatform(float x){
        super(x);
        partCima = new Vector2(x, (EstadoBase.HEIGHT/2+EstadoBase.HEIGHT/8));
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

    /**
     * funcao responsavel por reposicionar o WalkPlatform numa dada localizacao x
     * @param x posicao horizontal a reposicionar a estrutura
     */
    public void reposition(float x){
        partCima.x = x;
        partBaixo.x = x;
        colisaoCima.setPosition(partCima);
        colisaoBaixo.setPosition(partBaixo);
    }
    /**
     * Funcao que calcula se um jogador colidiu com alguma das caixas de colisao da estrutura,
     * fazendo update da ultima caixa de colisao recente (lastColided)
     * @param player caixa de colisao do jogador
     * @return true se colidiu, false em caso negativo
     */
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

    /**
     * dispose da textura
     */
    public void freeMemory(){
        platf.dispose();
    }

    /**
     *
     * @return clone do tipo WalkPlatform na posicao 0x
     */
    public WalkPlatform clone(){
        return new WalkPlatform(0);
    }
}
