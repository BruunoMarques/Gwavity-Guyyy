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
import com.gguy.game.estados.EstadoBase;
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

    private float width;
    private float height;

    private boolean bonusActivated = true;
    //por defeito é a skin do sonic
    public void defaultSkin(){
        String name, jumpT, walkT, iwalkT;
        name = "sonic/sonic.png";
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

    public void buySkin(String skin, int walkframes, int jumpframes){ //todo gerir dinheiro ganho.
        //this.money -= money;
        String name, jumpT, walkT, iwalkT;
        name = skin + "/" + skin + ".png";
        jumpT = skin + "/jump.png";
        walkT = skin + "/walk.png";
        iwalkT = skin + "/invwalk.png";

        Logger errText = new Logger(TAG,Logger.ERROR);
        try {
            player = new Texture(name);
            jumpTexture = new Texture(jumpT);
            walkTexture = new Texture(walkT);
            inverseWalkTexture = new Texture(iwalkT);
            jumpS = Gdx.audio.newSound(Gdx.files.internal("sounds/" + skin + "/jump.wav"));
            deathS = Gdx.audio.newSound(Gdx.files.internal("sounds/" + skin + "/death.wav"));
        }
        catch(GdxRuntimeException e){
            errText.error(e.getMessage());
            errText.error("Going to load default texture");
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

    public Guy(SkinInfo skin, int x, int y,float vel){
        posicao = new Vector2(x,y);
        speed = new Vector2(vel,0);
        buySkin(skin.getName(), skin.getRunningFrames(), skin.getJumpingFrames());
        width = walkTexture.getWidth() * EstadoBase.W_RES;
        height = walkTexture.getHeight() * EstadoBase.H_RES;
        colisao = new Rectangle(x+width/skin.getRunningFrames(),y,width/skin.getRunningFrames(),height);
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

    public void ignoreBonusAccel(){
        bonusActivated = false;
    }

    public void updatePos(float dt,boolean colidiu){//todo falta limitar aceleracao
        if(isFlying){
            speed.y = isUpsideDown ? 200 : -200;
            speed.x += dt;
            speed.scl(dt);
            if(colidiu)posicao.add(0,speed.y);
            else posicao.add(speed.x ,speed.y);
            speed.scl(1/dt);// ou speed.y * dt ?!?!
        }
        else {
            if(bonusActivated) speed.x += dt*2;
            else speed.x += dt;
            if(colidiu)posicao.add(0,0);
            else posicao.add(speed.x * dt,0);
        }
        if(!bonusActivated) bonusActivated = true;
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

    public void setPosicao(Vector2 posicao) {
        this.posicao = posicao;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
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
