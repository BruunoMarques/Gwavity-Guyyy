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
        obsCima = new Vector2(x, rand.nextInt(75) + EstadoBase.HEIGHT/2);
        obsBaixo = new Vector2(x, obsCima.y - 100 - temp.getHeight());

        colisaoCima = new Rectangle(obsCima.x,obsCima.y,temp.getWidth(),temp.getHeight());
        colisaoBaixo = new Rectangle(obsBaixo.x,obsBaixo.y,temp.getWidth(),temp.getHeight());
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
        return player.overlaps(colisaoCima) ||  player.overlaps(colisaoBaixo);
    }

    public void freeMemory(){
        temp.dispose();
    }

    public void reposition(float x){
        obsCima.set(x, rand.nextInt(75) + EstadoBase.HEIGHT/2);
        obsBaixo.set(x, obsCima.y - 100 - temp.getHeight());
        colisaoBaixo.setPosition(obsBaixo);
        colisaoCima.setPosition(obsCima);
    }
    public Muro clone(){
        return new Muro(0);
    }
}
