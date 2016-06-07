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
 * Este vai pegar num vetor que contem todas as estruturas a randomizar, e vai fazer um "mapa"
 * O random varia para cada tipo de estrutura
 */
public class MapGenerator {
    private ArrayList<MapStruct> EstruturasARandomizar;
    private ArrayList<MapStruct> RandomMap;
    private Random random;
    private float numberHoles = 0; //numero de buracos que se pode fazer no mapa
    public float smallestDistance;
    public float longestDistance;

    /**
     * construtor da classe
     * responsavel por inicializar os atributos, bem como adicionar as estruturas a randomizar
     */
    public MapGenerator(){
        EstruturasARandomizar = new ArrayList<MapStruct>();
        RandomMap = new ArrayList<MapStruct>();
        random = new Random();
        longestDistance = 0;
        smallestDistance = -160*EstadoBase.W_RES*2;
        EstruturasARandomizar.add(new Muro(0));
        EstruturasARandomizar.add(new WalkPlatform(0));
    }

    /**
     * gera inicialmente uma parte do mapa que será o começo do mapa
     * @return Mapa gerado inicial
     */
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

    /**
     * funcao responsavel por construir aleatoriamente o mapa, utilizando apenas
     * estruturas do tipo WalkPlatform.
     * Esta construcao e apenas efetuada se for preciso, ou seja, se um bloco estiver out of bonds
     * @param compare menor distancia do bloco que precisa de ser alterado
     * @param i indice do bloco/estrutura a ser alterado
     */
    public void randomType1(float compare, int i){
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
    /**
     * funcao responsavel por construir aleatoriamente o mapa, utilizando apenas
     * estruturas do tipo Muro
     * Esta construcao e apenas efetuada se for preciso, ou seja, se um bloco estiver out of bonds
     * @param compare menor distancia do bloco que precisa de ser alterado
     * @param i indice do bloco/estrutura a ser alterado
     */
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

    /**
     * funcao responsavel por adicionar Muros ao mapa, caso ainda nao exista nenhuma estrutura deste genero.
     * Esta e aleatoria, sendo que se aumenta a probabilidade de haver muros, se for possivel ainda fazer buracos no mapa
     * @param naoExisteMuros indica se atualmente no mapa existem muros
     */
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

    /**
     * funcao Principal responsavel por gerar o mapa
     * Esta invoca as funcoes que iram, ou nao, reposicionar certas estruturas, caso necessario.
     * E tambem responsavel por adicionar certas estruturas (aleatoriamente), dependendo de certas ocasioes(velocidade atual)
     * Inicializa o numero de buracos possivel de fazer (nº de buracos = speed do jogo/tamanho do bloco do chao)
     * @param speed velocidade do jogo atual
     * @return Mapa gerado
     */
    public ArrayList<MapStruct> generateMap(float speed){
        float compare = smallestDistance; //valor de x mais baixo
        boolean naoExisteMuros = true;
        numberHoles = speed / (160 * EstadoBase.W_RES);
        for(int i = 0;i<RandomMap.size();i++){
            randomType1(compare, i);
            if(randomType2(compare, i))naoExisteMuros = false;
        }
        if(smallestDistance == compare){ //bugg fixing no seu melhor
            smallestDistance += 160*EstadoBase.W_RES;
        }
        addType2(naoExisteMuros);
        return RandomMap;
    }

    /**
     * funcao responsavel por libertar memoria que as estuturas alocaram
     */
    public void disposeMap(){
        for(MapStruct lol : EstruturasARandomizar)
            lol.freeMemory();
        for(MapStruct lol : RandomMap)
            lol.freeMemory();
    }
}
