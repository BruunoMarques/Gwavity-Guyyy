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
 * Classe responsavel por gerar o mapa
 * Definicao de mapa: vetor de estruturas do mapa randomizadas numa certa posicao
 */
public class MapGenerator {
    private ArrayList<MapStruct> EstruturasARandomizar;
    private ArrayList<MapStruct> RandomMap;
    private Random random;
    private float numberHoles = 0;
    public float smallestDistance;
    public float longestDistance;


    public MapGenerator(){
        EstruturasARandomizar = new ArrayList<MapStruct>();
        RandomMap = new ArrayList<MapStruct>();
        random = new Random();
        longestDistance = 0;
        smallestDistance = -160*EstadoBase.W_RES*2;
        EstruturasARandomizar.add(new Muro(0));
        EstruturasARandomizar.add(new WalkPlatform(0));
    }
    public ArrayList<MapStruct> initializeMap(){
        MapStruct lol;
        //float size = EstadoBase.HEIGHT/(lol.getTextura().getWidth());
        //o nº 8 foi escolhido com base na teoria do infinito
        lol = (MapStruct) EstruturasARandomizar.get(1).clone();
        float f = lol.getTextura().getWidth()*EstadoBase.W_RES;
        for(int i = 0;i<8;i++){
            lol = (MapStruct) EstruturasARandomizar.get(1).clone();
            lol.reposition(i*f+smallestDistance);
            longestDistance = i * f;
            RandomMap.add(lol);
        }
        longestDistance -= 160*EstadoBase.W_RES;
        return RandomMap;
    }

    public void randomType1(float compare, int i, float speed){
        if(RandomMap.get(i) instanceof WalkPlatform)
        {
            MapStruct lol =(MapStruct) EstruturasARandomizar.get(1).clone();
            float temp = RandomMap.get(i).getTextura().getWidth()*EstadoBase.W_RES;
            float coordenadaX = RandomMap.get(i).getCoordenadas().get(0).x;
            if(coordenadaX < compare )
            {
                RandomMap.get(i).freeMemory();
                RandomMap.remove(i);
                lol.reposition(longestDistance);
                if(random.nextInt(5) == 4 && numberHoles > 1.25){
                    lol.reposition(0);
                    numberHoles--;
                }
                RandomMap.add(lol);
                longestDistance += temp;
            }
            else if(coordenadaX != smallestDistance && smallestDistance == compare)
                smallestDistance = coordenadaX;

            else{
                if(smallestDistance > coordenadaX)
                    smallestDistance = coordenadaX;
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
                if(random.nextInt(2) == 1){
                    lol.reposition(longestDistance);
                    RandomMap.add(lol);
                }
                else{
                    lol.freeMemory();
                    existe = false;
                }
            }
        }
        return existe;
    }

    public void addType2(boolean naoExisteMuros){
        if(naoExisteMuros){
            int n_muros = 0;
            do{
                MapStruct lol =(MapStruct) EstruturasARandomizar.get(0).clone();
                lol.reposition(longestDistance+n_muros*smallestDistance-320);
                RandomMap.add(lol);
                n_muros++;
                if(numberHoles >= 1 && random.nextInt(3) == 1){
                    lol.reposition(longestDistance+n_muros*smallestDistance-320);
                    RandomMap.add(lol);
                    n_muros++;
                }
            }while(random.nextInt(3) == 1);
        }
    }

    public ArrayList<MapStruct> generateMap(float speed){
        float compare = smallestDistance; //valor de x mais baixo
        boolean naoExisteMuros = true;
        numberHoles = speed / (160 * EstadoBase.W_RES);
        for(int i = 0;i<RandomMap.size();i++){
            randomType1(compare, i, speed);
            if(randomType2(compare, i))naoExisteMuros = false;
        }
        if(smallestDistance == compare){ //bugg fixing no seu melhor
            smallestDistance += 160*EstadoBase.W_RES;
        }
        addType2(naoExisteMuros);
        return RandomMap;
    }

    public void disposeMap(){
        for(MapStruct lol : EstruturasARandomizar)
            lol.freeMemory();
        for(MapStruct lol : RandomMap)
            lol.freeMemory();
    }
}
