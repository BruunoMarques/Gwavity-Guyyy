package com.gguy.game.gamestuff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.sun.prism.TextureMap;

/**
 * Created by Jonas on 01-05-2016.
 */
public class Guy {

    private final static String TAG = "errorMessage";
    private Vector2 posicao;
    private Vector2 speed;

    private Texture player;
    private Texture jumpTexture;
    private Texture walkTexture;
    private Texture inverseWalkTexture;

    private Animation jumpAnimation;
    private Animation walkAnimation;
    private Animation inverseWalkAnimation;

    private Sound jumpS;
    private Sound catch_powerS; //todo cenas do power
    private Sound deathS;

    private boolean isUpsideDown;
    private boolean isFlying;
    private Rectangle colisao;

    private float vel = 100; //todo mudar para 100
    //todo something related to superpowers

    public void defaultSkin(){
        String name, jumpT, walkT, iwalkT;
        name = "sonic/sonic.png"; //todo dp por uma default
        jumpT = "sonic" + "/jump.png";
        walkT = "sonic" + "/walk.png";
        iwalkT = "sonic" + "/invwalk.png";
        player = new Texture(name);
        jumpTexture = new Texture(jumpT);
        walkTexture = new Texture(walkT);
        inverseWalkTexture = new Texture(iwalkT);
        jumpS = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));//todo mudar endereco
        deathS = Gdx.audio.newSound(Gdx.files.internal("sounds/death.wav"));//todo mudar endereco
    }

    public void buySkin(String skin, int walkframes, int jumpframes){ //todo gerir dinheiro ganho. Requere correr freeMemory antes para mudar a skin
        //this.money -= money;
        String name, jumpT, walkT, iwalkT;
        name = skin + "/" + skin + ".png";
        jumpT = skin + "/jump.png";
        walkT = skin + "/walk.png";
        iwalkT = skin + "/invwalk.png";

        Logger errText = new Logger(TAG,Logger.ERROR);
        try {
            player = new Texture(name);//todo era fixe criar uma base de dados de skins am i right?
            jumpTexture = new Texture(jumpT);
            walkTexture = new Texture(walkT);
            inverseWalkTexture = new Texture(iwalkT);
            jumpS = Gdx.audio.newSound(Gdx.files.internal("sounds/" + skin + "/jump.wav"));
            deathS = Gdx.audio.newSound(Gdx.files.internal("sounds/" + skin + "/death.wav"));
        }
        catch(GdxRuntimeException e){
            errText.error(e.getMessage());
            errText.error("Going to load default texture");//todo add smthing here?
            defaultSkin();
            walkframes = 8;
            jumpframes = 4;
        }
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

    //todo era uma vez um fdp que perdeu-se num puto dum sitio todo fdd, e decidiu andar a correr de um lado para o outro só por que lhe deu na mona. Esse gajo chama-se guy pq era mt ofensivo chamar-lhe fdp.
    public Guy(SkinInfo skin, int x, int y){
        posicao = new Vector2(x,y);
        speed = new Vector2(vel,0);
        buySkin(skin.getName(), skin.getRunningFrames(), skin.getJumpingFrames());
        colisao = new Rectangle(x+walkTexture.getWidth()/skin.getRunningFrames(),y,walkTexture.getWidth()/skin.getRunningFrames(),walkTexture.getHeight());
        isUpsideDown = false;
        isFlying = true;
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
    public void updatePos(float dt,boolean colidiu){//todo isto esta em testes. Speed varia conforme o jogador nao bate, fazer para returnar o normal se bater frontalmente
        if(isFlying){
            speed.y = isUpsideDown ? 200 : -200;
            speed.x += dt;
            speed.scl(dt);
            if(colidiu)posicao.add(0,speed.y);
            else posicao.add(speed.x ,speed.y);
            speed.scl(1/dt);// ou speed.y * dt ?!?!
        }
        else {
            speed.x += dt*2;
            if(colidiu)posicao.add(0,0);
            else posicao.add(speed.x * dt,0);
        }

        colisao.setPosition(posicao.x,posicao.y);
    }

    public Vector2 getPosicao() {
        return posicao;
    }

    public Vector2 getSpeed(){
        return speed;
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

    public void playJumpSound(){
        jumpS.play();
    }

    public void playDeathSound(){
        deathS.play();
    }

    public void fixPosY(float y){
        posicao.y = y;
        colisao.y = y;
    }

    public void freeMemory(){
        player.dispose();
        jumpTexture.dispose();
        walkTexture.dispose();
        inverseWalkTexture.dispose();
        jumpS.dispose();
        deathS.dispose();
    }

}
