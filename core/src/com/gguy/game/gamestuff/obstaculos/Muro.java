package com.gguy.game.gamestuff.obstaculos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gguy.game.estados.EstadoBase;

import java.util.Random;

/**
 * Created by Jonas on 01-05-2016.
 */
public class Muro extends MapStruct{
    private Texture temp;
    private Vector2 obsCima, obsBaixo;
    private Rectangle colisaoCima, colisaoBaixo;
    private Random rand;

    public Muro(float x){
        super(x);
        temp = new Texture("map/obst.png");//como e repetido poderia ser estatico
        rand = new Random();
        obsCima = new Vector2(x, /*rand.nextInt(75)*/ EstadoBase.HEIGHT/2);
        obsBaixo = new Vector2(x, obsCima.y - 50 - temp.getHeight());//todo hardcoded maman

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
        temp.dispose();
    }

    public void reposition(float x){
        obsCima.x = x;
        obsBaixo.x = x;
        colisaoBaixo.setPosition(obsBaixo);
        colisaoCima.setPosition(obsCima);
    }
    public Muro clone(){
        return new Muro(0);
    }
}
