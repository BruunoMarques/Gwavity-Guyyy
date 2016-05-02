package com.gguy.game.gamestuff;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sun.prism.TextureMap;

/**
 * Created by Jonas on 01-05-2016.
 */
public class Guy {
    private Vector2 posicao;
    private Vector2 speed;

    private Texture player;
    private Texture jumpTexture;
    private Texture walkTexture;
    private Texture inverseWalkTexture;
    private Texture flyTexture;
    //private Animation idleAnimation;
    private Animation jumpAnimation;
    private Animation walkAnimation;
    private Animation inverseWalkAnimation;
    //private Animation flyAnimation;

    private boolean hasFlyingAnim;
    private boolean isUpsideDown;
    private boolean isFlying;
    private Rectangle colisao;

    private float vel = 100;
    //todo something related to superpowers

    public void buySkin(String skin, int jumpframes, int walkframes){ //todo gerir dinheiro ganho. Requere correr freeMemory antes para mudar a skin
        player = new Texture(skin);
        //this.money -= money;
        String jumpT, walkT, iwalkT;
        jumpT = "jump" + skin;
        walkT = "walk" + skin;
        iwalkT = "invwalk" + skin;
        jumpTexture = new Texture(jumpT);
        walkTexture = new Texture(walkT);
        inverseWalkTexture = new Texture(iwalkT);
        TextureRegion region = new TextureRegion(jumpTexture);
        MyAnim anim = new MyAnim(region,jumpframes,1/30f);
        jumpAnimation = anim.getSimpleAnimation();
        region = new TextureRegion(walkTexture);
        anim = new MyAnim(region,walkframes,1/20f);
        walkAnimation = anim.getSimpleAnimation();
        region = new TextureRegion(inverseWalkTexture);
        anim = new MyAnim(region,walkframes,1/20f);
        inverseWalkAnimation = anim.getSimpleAnimation();
    }

    public Guy(int x, int y){
        posicao = new Vector2(x,y);
        speed = new Vector2(0,0);
        buySkin("sonic.png", 4, 8);
        colisao = new Rectangle(x,y,50,50);
        hasFlyingAnim = true;
        isUpsideDown = false;
        isFlying = true;
        //colisao = new Rectangle(x,y,player.getWidth(),player.getHeight());//todo introduzir isto esquecime o q este todo queria dizer
    }

    public void changeGravity(){
        isUpsideDown = isUpsideDown ? false : true;
        isFlying = true;
    }

    public boolean normalGravity(){
        return isUpsideDown;
    }

    public void atingeChao(){
        isFlying = false;
    }

    public boolean isGuyFlying(){
        return isFlying;
    }
    public void updatePos(float dt){//todo isto esta em testes. Speed varia conforme o jogador nao bate nao sei se isto esta bem btw parece q anda mais rapido para cima do que baixo
        if(isFlying){
            speed.y = isUpsideDown ? 200 : -200;
            speed.scl(dt);
            vel += dt;
            posicao.add(vel * dt,speed.y);
            speed.scl(1/dt);// ou speed.y * dt ?!?!
        }
        else {
            vel += dt;
            posicao.add(vel * dt,0);
        }

        colisao.setPosition(posicao.x,posicao.y);
    }

    public Vector2 getPosicao() {
        return posicao;
    }

    public Texture getSkin() {
        return player;
    }

    public Animation getJumpAnimation(){
        return jumpAnimation;
    }

    public Animation getWalkAnimation(){
        return walkAnimation;
    }

    public Animation getInverseWalkAnimation(){
        return inverseWalkAnimation;
    }

    public Rectangle getColisaoBox(){
        return colisao;
    }

    public void fixPosY(float y){
        posicao.y = y;
    }

    public void freeMemory(){
        player.dispose();
        jumpTexture.dispose();
        walkTexture.dispose();
        inverseWalkTexture.dispose();
    }
}
