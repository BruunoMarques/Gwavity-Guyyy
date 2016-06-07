package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.MapGenerator;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import com.gguy.game.gamestuff.obstaculos.WalkPlatform;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 01-05-2016.
 */
public class EstadoJogo extends EstadoBase {
    private Guy gguy;
   // private SpriteBatch video_mem;
    private Botao pause_menu;
    private Botao use_power;

    private float timePassed = 0;
    private final float speedInicial = 600; //todo alterar isto x)

    private float speed = speedInicial;
    private final int pos_inicialX = 150;
    private final int pos_inicialY = HEIGHT/2;

    private MapGenerator geradorMapa;
    private ArrayList<MapStruct> world;

    private Rectangle currentStepping = new Rectangle(0,0,0,0);
    private int StateMachineWalking = 0;
    private boolean colidiu = false;

    private float flyingWidth;
    private float flyingHeight;

    private final static String TAG = "infoMessage";

    private void playMusic(){
        String selectedMusic = "music/" + emg.musicSelected.selectRandomMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal(selectedMusic));
        music.setLooping(false);
        music.setVolume(emg.soundVolume/2);
        music.play();
    }

    protected EstadoJogo(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/bck3.png");
        playMusic();
        gguy = new Guy(emg.skinSelected ,pos_inicialX,pos_inicialY,speedInicial);

        pause_menu = new Botao("utility/backbutton.png",WIDTH/2+WIDTH/6,HEIGHT*3/4 + HEIGHT/8);
        pause_menu.duplicateButtonSize();
        use_power = new Botao("utility/powerbutton.png",WIDTH/2-WIDTH/6,HEIGHT*3/4 + HEIGHT/8);
        use_power.duplicateButtonSize();
        pause_menu.setViewPoint((WIDTH/2+WIDTH/6)/2,HEIGHT*3/4 - HEIGHT/14);
        use_power.setViewPoint((WIDTH/2-WIDTH/6)/2,HEIGHT*3/4 - HEIGHT/14);

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();

        flyingHeight = gguy.getJumpAnimation().getKeyFrame(timePassed, true).getRegionHeight()*H_RES;
        flyingWidth = gguy.getJumpAnimation().getKeyFrame(timePassed, true).getRegionWidth()*W_RES;
    }

    public void restartGame(){
        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        pause_menu.changeViewPosX((WIDTH/2+WIDTH/6)/2);
        use_power.changeViewPosX((WIDTH/2-WIDTH/6)/2);

        gguy.freeMemory();
        gguy = new Guy(emg.skinSelected ,150,HEIGHT/2,speedInicial);

        timePassed = 0;
        speed = speedInicial;

        currentStepping = new Rectangle(0,0,0,0);
        StateMachineWalking = 0;
        colidiu = false;

        geradorMapa.disposeMap();
        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();
    }

    /**
     * Calcula a posicao do jogador para sair da colision box, de acordo com a orientaçao deste
     * @return posicao fixed
     */
    private float getColisionLimit(){
        Rectangle test = new Rectangle();
        test.set(gguy.getColisaoBox());
        boolean colidiuBaixo = gguy.normalGravity();
        float ret = 0;
        while(test.overlaps(currentStepping)){
            if(colidiuBaixo) ret--;
            else ret++;
            test.y += ret;
        }
        return test.y;
    }

    private float getSizeVector(float x, float y){
        return (float) Math.sqrt((double)(x*x + y*y));
    }

    /**
     * Algoritmo a executar apos a deteçao de uma colisao.
     * Este vai indicar se a colisao foi frontal, ou nao
     * Breve explicacao: simula o movimento anterior do jogador, consoante a sua velocidade
     * Depois obtenho a caixa no instante antes da colisao, e verifico se o x da frente do jogador é maior do que o da caixa
     * se for, quer dizer que a colisao foi frontal.
     * @param rect caixa de colisao a testar com o jogador
     * @return true se for uma colisao frontal, falso se for uma colisao superior ou inferior
     */
    private boolean FrontalColision(Rectangle rect){
        Rectangle test = new Rectangle();
        test.set(gguy.getColisaoBox());
        float size = getSizeVector(gguy.getSpeed().x,gguy.getSpeed().y);
        float xInc = gguy.getSpeed().x/size; //
        float yInc;
        if(gguy.getSpeed().y == 0){ //not realy necessary, but oki xD
            if(gguy.normalGravity()) yInc = 1;
            else yInc = -1;
        }
        else yInc = gguy.getSpeed().y/size;
        while(test.overlaps(rect)){
            test.y -= yInc;
            test.x -= xInc;
        }
        if(test.x + test.getWidth() > rect.x) return false;
        return true;
    }
    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched() && pause_menu.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        else if(Gdx.input.justTouched() && !gguy.isGuyFlying()){
            gguy.fixPosY(getColisionLimit());
            gguy.changeGravity();
            if(emg.soundVolume > 0.2f) gguy.playJumpSound();
        }
    }

    private boolean updateMap(){
        boolean needsChange = false;
        boolean noGround = true;
        boolean hitWall = false;
        for(int i = 0;i<world.size();i++) {
            MapStruct obstaculo = world.get(i);
            if (obstaculo.ColideGuy(gguy.getColisaoBox())) {
                if(StateMachineWalking == 1 && currentStepping.y == obstaculo.getLastColided().y){
                    noGround = false;
                    currentStepping = obstaculo.getLastColided();
                }
                else if(FrontalColision(obstaculo.getLastColided())){
                    hitWall = true;
                }
                else{
                    gguy.atingeChao();
                    StateMachineWalking = 1;
                    currentStepping = obstaculo.getLastColided();
                    noGround = false;
                }
            }
            if (camara.position.x - (camara.viewportWidth / 2) > geradorMapa.smallestDistance) { //todo maybe this is wrong
                needsChange = true;
                //world = geradorMapa.generateMap(speed);
            }
        }
        if(needsChange){
            Logger banana = new Logger(TAG,Logger.INFO); // works
            banana.info("Gerador : " + geradorMapa.smallestDistance);
            world = geradorMapa.generateMap(speed);
        }
        if(noGround && !gguy.isGuyFlying()){
            gguy.changeGravity();
            gguy.changeGravity();
        }
        if(noGround) StateMachineWalking = 0;

        return hitWall;
    }

    public float getTimePassed(){
        return timePassed;
    }

    //condicao para ver se acontece fim de jogo
    private boolean gameOver(){
        return (gguy.getPosicao().y > camara.position.y + HEIGHT/4 ||
                gguy.getPosicao().y + gguy.getColisaoBox().getHeight() <  camara.position.y - HEIGHT/4 ||
                gguy.getPosicao().x + gguy.getColisaoBox().getWidth() < camara.position.x - WIDTH/4 );
    }

    @Override
    public void update(float dt) {
        if(!music.isPlaying()){
            music.dispose();
            playMusic();
        }
        if(gameOver()){
            if(emg.soundVolume > 0.2f) gguy.playDeathSound();
            emg.addEstado(new EstadoFimJogo(emg, this));
        }
        handleInput();
        if(gguy.getPosicao().x > camara.position.x )gguy.ignoreBonusAccel();
        gguy.updatePos(dt,colidiu);
        timePassed += dt;
        camara.position.x += speed * dt;
        speed += dt;
        use_power.changeViewPosX(use_power.getCoordView().x + (speed * dt));
        pause_menu.changeViewPosX(pause_menu.getCoordView().x + (speed * dt));
      //  gguy.updatePos(dt);
        colidiu = updateMap();
        camara.update();
    }


    @Override
    public void render(SpriteBatch spriteB){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();
        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(use_power.getButton(),use_power.getCoordView().x,use_power.getCoordView().y,use_power.getWidth()/2,use_power.getHeight()/2);
        spriteB.draw(pause_menu.getButton(),pause_menu.getCoordView().x,pause_menu.getCoordView().y,pause_menu.getWidth()/2,pause_menu.getHeight()/2);

        spriteB.draw(use_power.getButton(),gguy.getColisaoBox().x,gguy.getColisaoBox().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
        if(gguy.isGuyFlying())spriteB.draw(gguy.getJumpAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,flyingWidth,flyingHeight);
        else if(!gguy.normalGravity())spriteB.draw(gguy.getWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
        else spriteB.draw(gguy.getInverseWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());

        for(MapStruct mapa : world){
            for(int i = 0;i<mapa.getColisionbox().size();i++){
                spriteB.draw(mapa.getTextura(),mapa.getColisionbox().get(i).x,mapa.getColisionbox().get(i).y,mapa.getColisionbox().get(i).getWidth(),mapa.getColisionbox().get(i).getHeight());
            }
        }
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        gguy.freeMemory();
        use_power.disposeButton();
        pause_menu.disposeButton();
        music.dispose();
        geradorMapa.disposeMap();
    }
}
