package com.gguy.game.gamestuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.gguy.game.estados.EstadoBase;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import com.gguy.game.gamestuff.obstaculos.Muro;
import com.gguy.game.gamestuff.obstaculos.WalkPlatform;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 25-05-2016.
 */
public class MapGenerator {
    private ArrayList<MapStruct> EstruturasARandomizar;
    private ArrayList<MapStruct> RandomMap;
    private Random random;
    public float smallestDistance;
    public float longestDistance;


    public MapGenerator(){
        EstruturasARandomizar = new ArrayList<MapStruct>();
        RandomMap = new ArrayList<MapStruct>();
        random = new Random();
        longestDistance = 0;
        smallestDistance = -160*EstadoBase.W_RES;
        EstruturasARandomizar.add(new Muro(0));
        EstruturasARandomizar.add(new WalkPlatform(0));
    }
    public ArrayList<MapStruct> initializeMap(){ //todo hardcoding intensifies
        MapStruct lol;
        //float size = EstadoBase.HEIGHT/(lol.getTextura().getWidth());
        for(int i = 0;i<8;i++){
            lol = (MapStruct) EstruturasARandomizar.get(1).clone();
            lol.reposition(i*lol.getTextura().getWidth()*EstadoBase.W_RES+smallestDistance);
            longestDistance = i * lol.getTextura().getWidth()*EstadoBase.W_RES;
            RandomMap.add(lol);
        }
        return RandomMap;
    }

    public void randomType1(float compare, int i){
        if(RandomMap.get(i) instanceof WalkPlatform)
        {
            MapStruct lol =(MapStruct) EstruturasARandomizar.get(1).clone();
            float temp = RandomMap.get(i).getTextura().getWidth()*EstadoBase.W_RES;
            float coordenadaX = RandomMap.get(i).getCoordenadas().get(0).x;//todo fazer um metodo para ter o furthest x ?
            if(coordenadaX < compare ) //todo ponderar se é necessario msm comparar aquilo
            {
                RandomMap.get(i).freeMemory();
                RandomMap.remove(i);

                if(random.nextInt(5) != 4){
                    lol.reposition(longestDistance);
                }else {
                    lol.reposition(0);
                }
                RandomMap.add(lol);
                longestDistance += temp;

            }
            else if(coordenadaX != smallestDistance && smallestDistance == compare)
                smallestDistance = (int)coordenadaX;

            else{
                if(smallestDistance > coordenadaX)
                    smallestDistance = (int)coordenadaX;
            }
        }
    }
    public boolean randomType2(float compare, int i){
        boolean existe = false;
        if(RandomMap.get(i) instanceof Muro){
            existe = true;
            MapStruct lol =(MapStruct) EstruturasARandomizar.get(0).clone();
            float coordenadaX = RandomMap.get(i).getCoordenadas().get(0).x;
            if(coordenadaX < compare){
                RandomMap.get(i).freeMemory();
                RandomMap.remove(i);
                if(random.nextInt(5) != 4){
                    lol.reposition(longestDistance);
                }
                RandomMap.add(lol);
            }
        }
        return existe;
    }
    public ArrayList<MapStruct> generateMap(){
        float compare = smallestDistance; //valor de x mais baixo
        boolean naoExisteMuros = true;
        for(int i = 0;i<RandomMap.size();i++){
            randomType1(compare, i);
            if(randomType2(compare, i))naoExisteMuros = false;
        }
        if(naoExisteMuros){
            int n_muros = 0; //todo oh snap
            do{
                MapStruct lol =(MapStruct) EstruturasARandomizar.get(0).clone();
                lol.reposition(longestDistance+n_muros*smallestDistance-400);
                RandomMap.add(lol);
                n_muros++;
            }while(random.nextInt(10) == 1);
        }
        return RandomMap;
    }

    public void disposeMap(){
        for(MapStruct lol : EstruturasARandomizar)
            lol.freeMemory();
        for(MapStruct lol : RandomMap)
            lol.freeMemory();
    }
}
