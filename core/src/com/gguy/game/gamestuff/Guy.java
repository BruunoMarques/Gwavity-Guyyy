package com.gguy.game.gamestuff;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Jonas on 01-05-2016.
 */
public class Guy {
    private Vector2 posicao;
    private Vector2 speed;
    private Texture player;
    private Rectangle colisao;
    public void buySkin(String skin){ //todo gerir dinheiro ganho
        player.dispose();
        player = new Texture(skin);
        //this.money -= money;
    }

    public Guy(int x, int y){
        posicao = new Vector2(x,y);
        speed = new Vector2(0,0);
        player = new Texture("gg1.png");
        colisao = new Rectangle(x,y,player.getWidth(),player.getHeight());
    }
    public void fly(){
        speed.y = 200;
    }
    public void updatePos(float dt){//todo isto esta em testes. Speed varia conforme o jogador nao bate
        if(posicao.y > 10) speed.add(0,-10);//random value ftw
        speed.scl(dt);
        if(speed.y + posicao.y > 10) posicao.add(100 * dt,speed.y);

        speed.scl(1/dt);// ou speed.y * dt ?!?!
    }

    public Vector2 getPosicao() {
        return posicao;
    }

    public Texture getSkin() {
        return player;
    }

    public void freeMemory(){
        player.dispose();
    }
}
