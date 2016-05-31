package com.gguy.game.gamestuff;

import com.badlogic.gdx.Gdx;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import com.gguy.game.gamestuff.obstaculos.Muro;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 25-05-2016.
 */
public class MapGenerator {
    ArrayList<MapStruct> EstruturasARandomizar;
    ArrayList<MapStruct> RandomMap;
    Random random;

    public MapGenerator(){
        EstruturasARandomizar = new ArrayList<MapStruct>();
        RandomMap = new ArrayList<MapStruct>();
        random = new Random();
        EstruturasARandomizar.add(new Muro(0));
    }

    public void generateMap(float x){
        for(MapStruct lol : EstruturasARandomizar){
            if(random.nextInt(10) < 7) //todo magic
            lol.reposition(x);
        }
    }
}
