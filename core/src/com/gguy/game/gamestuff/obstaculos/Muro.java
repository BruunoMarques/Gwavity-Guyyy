package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;

import java.util.Random;

/**
 * Created by Jonas on 01-05-2016.
 * Classe que representa um tipo de estrutura do mapa
 * Esta e formada por 2 muros, 1 superior e 1 inferior, com 1 buraco entre eles
 */
public class Muro extends MapStruct{
    private Texture temp;
    private Vector2 obsCima, obsBaixo;
    private Rectangle colisaoCima, colisaoBaixo;
    private Random rand;
    private float minY ;
    private float maxY ;

    /**
     * Construtor responsavel por inicializar o Muro
     * Este cria a textura e 2 caixas de colisao, que vao representar as 2 partes do muro, bem como posiciona-las
     * num sitio inicial
     * @param x posicao inicial
     */
    public Muro(float x){
        super(x);
        temp = new Texture("map/obst.png");//como e repetido poderia ser estatico
        minY = EstadoBase.HEIGHT/4-temp.getHeight()*EstadoBase.H_RES/3;
        maxY = EstadoBase.HEIGHT/2+EstadoBase.HEIGHT/8-temp.getHeight()*EstadoBase.H_RES/2;
        rand = new Random();
        obsBaixo = new Vector2(x,minY);
        obsCima = new Vector2(x,maxY);


        colisaoCima = new Rectangle(obsCima.x,obsCima.y,temp.getWidth()*EstadoBase.W_RES,temp.getHeight()*EstadoBase.H_RES);
        colisaoBaixo = new Rectangle(obsBaixo.x,obsBaixo.y,temp.getWidth()*EstadoBase.W_RES,temp.getHeight()*EstadoBase.H_RES);
        Colisionbox.add(colisaoBaixo);
        Colisionbox.add(colisaoCima);
        Textura = temp;
        Coordenadas.add(obsBaixo);
        Coordenadas.add(obsCima);
    }

    public Texture getTemp() {
        return temp;
    }

    public Vector2 getObsCima() {
        return obsCima;
    }

    public Vector2 getObsBaixo() {
        return obsBaixo;
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
        temp.dispose();
    }

    /**
     * funcao responsavel por reposicionar o muro numa dada localizacao x
     * Ira tambem alterar a posicao vertical, aleatoriamente
     * @param x posicao horizontal a reposicionar a estrutura
     */
    public void reposition(float x){
        obsCima.x = x;
        obsBaixo.x = x;
        float cenas = rand.nextFloat();
        float random = 51*cenas*EstadoBase.H_RES; //trust me, i know what i'm doin
        if(rand.nextInt(2) == 1) random *= -1;
        obsCima.y = maxY + random;
        obsBaixo.y = minY + random;
        colisaoBaixo.setPosition(obsBaixo);
        colisaoCima.setPosition(obsCima);
    }

    /**
     *
     * @return clone do muro a posicao 0x
     */
    public Muro clone(){
        return new Muro(0);
    }
}
