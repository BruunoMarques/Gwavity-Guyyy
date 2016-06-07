package com.gguy.game.gamestuff;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;
import com.gguy.game.estados.EstadoJogo;

import java.awt.font.NumericShaper;
import java.util.Random;

/**
 * Created by Bruno on 04/06/2016.
 */

/**
 * Classe responsavel pelos powerups utilizados no jogo
 */
public class PowerUp {
    private float heighttop;
    private float heightbottom;
    Texture texmex,texcurrent,texmystery,texflight,texlowspeed,texBonus,texHighspeed;
    char type;
    private int distance = 1000;
    private Vector2 coordView;
    EstadoJogo e;

    private Rectangle box;

    /**
     * Construtor de PowerUp.
     * Inicializa todas as variaveis necessarias.
     * @param e  Estado de Jogo atual
     * @param type  Textura do PowerUp
     */
    public PowerUp(EstadoJogo e, String type){

        texmex = new Texture(type);//speed if collides *2 speed
        texmystery = new Texture("map/lock.png");
        texcurrent = texmystery;
        texflight = new Texture("map/peixe.png"); //q fail
        texlowspeed = new Texture("map/turtle.png");
        texBonus = new Texture("map/coin.png");
        texHighspeed = new Texture("map/sanic.png");

        coordView = new Vector2(0,0);
        this.e = e;
        heighttop = 5*EstadoBase.HEIGHT/16;
        heightbottom =13*EstadoBase.HEIGHT/24;
        coordView.y = 0;
        coordView.x = 0;
        box = new Rectangle(coordView.x,coordView.y,texmex.getWidth()*EstadoBase.W_RES,texmex.getHeight()*EstadoBase.H_RES);

    }


    /**
     * Funcao responsavel pelo processo de geracao aleatoria do "efeito" do powerUp sobre o jogo
     */
    public void getFun(){
        Random fun = new Random();
       int  wtv = fun.nextInt(4) + 1;

        switch (wtv){
            case 1:
                    e.returnGuy().addSpeed();
                    e.returnGuy().isFaster = true;
                    texcurrent = texHighspeed;
                break;
            case 2:
                e.flight = true;
                texcurrent = texflight;
                break;

            case 4:
                e.returnGuy().reduceSpeed();
                e.returnGuy().isSlower = false;
                texcurrent = texlowspeed;
                break;

            case 3:
                e.addBonusScore();
                texcurrent = texBonus;
                break;

            default:
                break;
        }
    };

    /**
     * Retorna os valores alterados ao seu normal
     */
    public void resetFun(){
       e.flight = false;
       e.returnGuy().resetSpeed();
       texcurrent = texmystery;
    };

    /**
     *
     * @return Textura do PowerUp
     */
    public Texture getPower(){
        return texmex;
    };

    /**
     *
     * @return Retorna Textura do icone de powerup
     */
    public Texture getTexPower(){
        return texcurrent;
    };

    /**
     *
     * @return Hitbox do powerup
     *
     */
    public Rectangle getRectange(){
        return box;
    };

    /**
     *
     * @return Retorna Distancia base entre cada powerUp
     */
    public int getdist(){
        return distance;
    };

    /**
     *
     * @return Coordenadas do powerup
     */
    public Vector2 getCoordView() {
        return coordView;
    }

    /**
     * Funcao que aleatoriamente coloca o powerUp
     * @param vec Vector posicao
     */
    public void position(Vector2 vec){
        Random numero = new Random();

        coordView.x = vec.x +1000;
        if (numero.nextInt(10) >5){
            coordView.y = heightbottom;
        }else{coordView.y= heighttop;}

        box.setPosition(coordView);
    };

    /**
     * Reposiciona a instancia de  powerUp
     */
    public void reposition(){
        Random numero = new Random();
        coordView.x += distance *( numero.nextInt(5) +3);


        if (( numero.nextInt(10)%2) == 0){
            coordView.y = heighttop;
        }
        else{
            coordView.y = heightbottom;
        }

        box.setPosition(coordView);

    };

    /**
     * Liberta todas as texturas utilizadas
     */
    public void disposePower(){
        texmex.dispose();
        texcurrent.dispose();
        texflight.dispose();
        texmystery.dispose();
        texBonus.dispose();
        texlowspeed.dispose();
        texHighspeed.dispose();
    }



}
