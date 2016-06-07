package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Jonas on 25-05-2016.
 * Classe que presenta uma estrutura do mapa
 * Ou seja, uma pequena parte do mapa, podendo ser o chao, ou um muro, por exemplo
 * Esta contem informacao necessaria para poder interagir com o jogador,
 * bem como da posicao relativa no mapa, e texturas que presentam a estrutura
 */
public abstract class MapStruct implements Cloneable{

    protected ArrayList<Rectangle> Colisionbox;
    protected ArrayList<Vector2> Coordenadas;
    protected Texture Textura;
    protected Rectangle lastColided;

    /**
     *
     * @return um rectangulo que representa a caixa que colidiu mais recentemente com o jogador
     */
    public Rectangle getLastColided(){
      return lastColided;
    }

    /**
     *
     * @return vetor com todas as caixas de colisao de uma estrutura
     */
    public ArrayList<Rectangle> getColisionbox() {
        return Colisionbox;
    }

    /**
     *
     * @return vetor de posicoes das caixas de colisao
     */
    public ArrayList<Vector2> getCoordenadas() {
        return Coordenadas;
    }

    /**
     *
     * @return textura da estrutura
     */
    public Texture getTextura() {
        return Textura;
    }

    /**
     *
     * @param x posicao horizontal a reposicionar a estrutura
     */
    public abstract void reposition(float x);

    /**
     * Funcao que calcula se um jogador colidiu com alguma das caixas de colisao da estrutura,
     * fazendo update da ultima caixa de colisao recente (lastColided)
     * @param player caixa de colisao do jogador
     * @return true se colidiu, false em caso negativo
     */
    public abstract boolean ColideGuy(Rectangle player);

    /**
     *
     * @return um clone da estrutura
     */
    public abstract MapStruct clone();
    public abstract void freeMemory();

    /**
     * Construtor da estrutura serve apenas para inicializar os vetores atributos
     * @param x posicao inicial
     */
    public MapStruct(float x){
        Colisionbox = new ArrayList<Rectangle>();
        Coordenadas = new ArrayList<Vector2>();
    }
}
