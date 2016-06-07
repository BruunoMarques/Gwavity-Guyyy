package com.gguy.game.estados;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gguy.game.estados.ferramentas.PlayList;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.gguy.game.estados.ferramentas.SkinList;

import java.util.Stack;

/**
 * Classe para gerir o estado a correr
 * Esta vai estar sempre a executar o update e o render do estado no topo da pilha
 */
public class EstadosManager {
    private Stack<EstadoBase> estados;

    public SkinList skins; //lista de skins do jogo
    public SkinInfo skinSelected; //informacao da skin selecionada
    public PlayList musicSelected;//playlist que contem informacao sobre a musica
    public float soundVolume = 0.0f;

	/**
     * Construtor de EstadosManager.
     * Inicializa a stack usada para gerir os estados da aplicacao, uma playlist para a musica a usar, uma SkinList para as aparencias do jogador e inicializa a aparencia por defeito
     */
    public EstadosManager(){
        estados = new Stack<EstadoBase>();
        musicSelected = new PlayList();
        skins = new SkinList();
        skinSelected = skins.getSelectedSkin();
    }

	 /**
     * @param estado estado a colocar no topo da pilha, que vai ser o estado atual
     */
    public void addEstado(EstadoBase estado){
        estados.push(estado);
    }

	/**
     * Remove o estado atual da pilha
     */
    public void remEstadoAct(){ //remove estado atual
        estados.pop().freeMemory();
    }
	/**
     * Atualiza o estado no topo da pilha
     * @param dt intervalo de tempo entre atualizacoes
     */
    public void updateEstado(float dt) {
        estados.peek().update(dt);
    }
	/**
     * Desenha o estado no topo da pilha.
     * @param spriteB SpriteBatch a ser usada pelo estado que vai ser desenhado (nao tem de ser a unica)
     */
    public void renderEstado(SpriteBatch spriteB){
        estados.peek().render(spriteB);
    }
}
