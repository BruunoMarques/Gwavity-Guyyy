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
 * Classe que representa o jogador
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
    private int walkF;
    private int jumpF;

    //bruno cancer
    public boolean isFaster = false, isSlower = false;

    private boolean bonusActivated = true;
    //por defeito é a skin do sonic
	/**
     * Inicializa a aparência por defeito do jogador.
     * Isto inclui texturas e sons
     */
    private void defaultSkin(){
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
	
	/**
     * Carrega uma nova aparencia do jogador. Isto inclui as texturas, animacoes e sons
     * @param skin nome da nova aparencia
     */
    private void loadSkin(String skin){
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
            walkF = 8;
            jumpF = 4;
        }
    }

	/**
     * Altera a aparencia do jogador. Isto inclui as texturas, animacoes e sons
     * @param skin nome da nova aparencia
     * @param walkframes  numero de frames da textura de animacao da corrida
     * @param jumpframes  numero de frames da textura de animacao dos saltos
     */
    private void buySkin(String skin, int walkframes, int jumpframes){ //todo gerir dinheiro ganho.
        walkF = walkframes;
        jumpF = jumpframes;
        loadSkin(skin);

        //criação das animacoes utilizando uma classe auxiliar
        TextureRegion region = new TextureRegion(jumpTexture);
        MyAnim anim = new MyAnim(region,jumpF,1/30f);
        jumpAnimation = anim.getSimpleAnimation();

        region = new TextureRegion(walkTexture);
        anim = new MyAnim(region,walkF,1/20f);
        walkAnimation = anim.getSimpleAnimation();

        region = new TextureRegion(inverseWalkTexture);
        anim = new MyAnim(region,walkF,1/20f);
        inverseWalkAnimation = anim.getSimpleAnimation();
    }

	/**
     * Construtor da classe Guy.
     * Inicializa as variaveis necessarias, relacionadas com a aparencia, posicao e velocidade iniciais, a caixa de colisao e o estado inicial (a correr com gravidade regular)
     * @param skin aparencia do jogador
     * @param x abcissa inicial do jogador
     * @param y ordenada inicial do jogador
     * @param vel velocidade inicial do jogador
     */
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

	/**
     * Troca o valor do boolean que representa a gravidade.
     */
    public void changeGravity(){
        isUpsideDown = isUpsideDown ? false : true;
        isFlying = true;
    }

	/**
     * @return true se a gravidade esta invertida
     * @return false se a gravidade esta regular
     */
    public boolean normalGravity(){
        return isUpsideDown;
    }

	/**
     * Muda o valor da variavel isFlying para false, o que significa que o jogador esta a correr sobre um obstaculo
     */
    public void atingeChao(){
        isFlying = false;
    }

	/**
     * @return true se o jogador esta no meio de um salto
     * @return false se o jogador esta esta a correr sobre um obstaculo
     */
    public boolean isGuyFlying(){
        return isFlying;
    }

	/**
     * Atribui false ao boolean que representa o bonus de velocidade. Ou seja, o jogador nao ganhara velocidade extra
     */
    public void ignoreBonusAccel(){
        bonusActivated = false;
    }

	/**
     * Atualiza a posicao do jogador, contabilizando a aceleracao (alterada ou nao por bonus) e se este se encontra preso atras de um obstaculo
     * A atualizacao e diferente caso o jogador esteja em salto ou corrida, pois em salto nao acelera e os bonus nao tem efeito
     * @param dt tempo decorrido entre updates
     * @param colidiu true se o jogador esta a colidir com um obstaculo
     */
    public void updatePos(float dt,boolean colidiu){
        if(isFlying){
            speed.y = isUpsideDown ? 200*EstadoBase.W_RES : -200*EstadoBase.W_RES;
            speed.x += dt;
            speed.scl(dt);
            if(colidiu)posicao.add(0,speed.y);
            else posicao.add(speed.x ,speed.y);
            speed.scl(1/dt);// ou speed.y * dt ?!?!
        }
        else {
            if(bonusActivated) speed.x += dt*2;
            else speed.x -= dt*0.5;
            if(colidiu)posicao.add(0,0);
            else posicao.add(speed.x * dt,0);
        }
        if(!bonusActivated) bonusActivated = true;
        colisao.setPosition(posicao.x,posicao.y);
    }

    public void addSpeed(){
        speed.x += 10;
    };

    public void reduceSpeed(){
        speed.x -= 10;
    };

    public void resetSpeed(){
        if (isFaster){
            speed.x -= 10;
            isFaster = false;
        }
        if (isSlower){
            speed.x += 10;
            isSlower = false;
        }

    };

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

	/**
     * Usado para manter o jogador a correr sobre o obstaculo
     * @param y ordenada do obstaculo
     */
    public void fixPosY(float y){
        posicao.y = y;
        colisao.y = y;
    }

	/**
     * Descarta memoria (fundo, texturas, sons, etc)
     */
    public void freeMemory(){
        player.dispose();
        jumpTexture.dispose();
        walkTexture.dispose();
        inverseWalkTexture.dispose();
        jumpS.dispose();
        deathS.dispose();
    }

}
