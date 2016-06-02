package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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
    private SpriteBatch video_mem;
    private Botao pause_menu;
    private Botao use_power;

    private float timePassed = 0;
    private float speed = 400; //todo igualar ao valor do gguy,speed

    private MapGenerator geradorMapa;
    private ArrayList<MapStruct> world;
    //todo resolver os todos e apagá-los quando os resolver para nao ter a salganhada como aqui em baixo
    //todo ongoing changes, pls bear with me
    private Rectangle currentStepping = new Rectangle(0,0,0,0);//todo fazer isto dp, estou a curtir James Blake
    private Rectangle GuyFixed; //todo testing ...
    private boolean colidiu = false; //todo testing...

    private final static String TAG = "infoMessage";

    private void playMusic(){
        String selectedMusic = "music/" + emg.musicSelected.selectRandomMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal(selectedMusic));
        music.setLooping(false);
        music.setVolume(0.3f);
        music.play();
    }

    protected EstadoJogo(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/bck3.png");
        playMusic();
        gguy = new Guy(emg.skinSelected ,50,HEIGHT/2);

        pause_menu = new Botao("background/backbutton.png",WIDTH/2-75,HEIGHT-75); //todo placeholder wtf ta buggado
        use_power = new Botao("background/powerbutton.png",WIDTH/2+150,HEIGHT);
        pause_menu.setViewPoint((WIDTH/2-75)/2,HEIGHT*3/4 - 50); //todo still all fked up
        use_power.setViewPoint(200,HEIGHT*3/4 - 50);

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

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
            test.x += ret;
        }
        return ret+gguy.getPosicao().y;
    }
    public boolean ColideGuyFrontal(Rectangle rect, int height){
        GuyFixed = gguy.getColisaoBox(); //todo sofreu alteracoes, se for preciso reverter, substituir isto por gguy.getColisionBox();
        GuyFixed.y = getColisionLimit();
        if(!gguy.normalGravity())
            if(!gguy.isGuyFlying() && GuyFixed.y-gguy.getColisaoBox().getHeight() <= rect.y)return true;
            else return false;
        else if(gguy.normalGravity()){
            if(!gguy.isGuyFlying() && GuyFixed.y > rect.y-height &&
                    GuyFixed.y- GuyFixed.getHeight() < rect.y)return false;
        }
        return false;
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
            gguy.playJumpSound();
        }
    }
    public boolean updateMap(){
        boolean needsChange = false;
        boolean noGround = true;
        boolean hitWall = false;
        boolean isFlying = gguy.isGuyFlying();
        for(int i = 0;i<world.size();i++) {
            MapStruct obstaculo = world.get(i);
 //           if(gguy.isGuyFlying()) {
            if (obstaculo.ColideGuy(gguy.getColisaoBox())) { //todo cancer
                if(ColideGuyFrontal(obstaculo.getLastColided(),obstaculo.getTextura().getHeight())){
                    hitWall = true;
                }
                else {
                    gguy.atingeChao(); //todo acho que ja nao tá buggy!! nvm ta buggy q fode
                  /*  if(isFlying != gguy.isGuyFlying()){
                        GuyFixed = gguy.getColisaoBox();
                        GuyFixed.y = getColisionLimit();
                    }*/
                    currentStepping = obstaculo.getLastColided();
                    noGround = false;
                }
            }
//            int Width = obstaculo.getTextura().getWidth();
//            double coord = obstaculo.getCoordenadas().get(0).x;
            if (camara.position.x - (camara.viewportWidth / 2) > geradorMapa.smallestDistance) {
                needsChange = true;
            }
        }
        if(needsChange){
            world = geradorMapa.generateMap();
        }

        if(noGround && !gguy.isGuyFlying()){
            gguy.changeGravity();
            gguy.changeGravity();
        }

        return hitWall;
    }
    @Override
    public void update(float dt) {
        if(!music.isPlaying()){
            music.dispose();
            playMusic();
        }
        /* kinda works
        if(!camara.frustum.pointInFrustum(gguy.getPosicao().x,gguy.getPosicao().y,camara.position.z)){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        */
        //condicao para ver se acontece fim de jogo
        if(gguy.getPosicao().y > camara.position.y + camara.viewportHeight/2 ||
                gguy.getPosicao().y + gguy.getColisaoBox().getHeight() <  camara.position.y - camara.viewportHeight/2 ||
                gguy.getPosicao().x + gguy.getColisaoBox().getWidth() < camara.position.x - camara.viewportWidth/2){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg)); //todo add menu fim de jogo
        }
        handleInput();
        gguy.updatePos(dt,colidiu);
        timePassed += dt;
        camara.position.x += speed * dt;
        speed += dt;
        use_power.changeViewPosX(use_power.getCoordView().x + (speed * dt));
        pause_menu.changeViewPosX(pause_menu.getCoordView().x + (speed * dt));
      //  gguy.updatePos(dt);
        colidiu = updateMap();//todo por no fim antes do camara.update maybe
        camara.update();
    }

    @Override
    public void render(SpriteBatch spriteB){
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();

        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(use_power.getButton(),use_power.getCoordView().x,use_power.getCoordView().y);
        spriteB.draw(pause_menu.getButton(),pause_menu.getCoordView().x,pause_menu.getCoordView().y);

        if(gguy.isGuyFlying())spriteB.draw(gguy.getJumpAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);
        else if(!gguy.normalGravity())spriteB.draw(gguy.getWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);
        else spriteB.draw(gguy.getInverseWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);

        for(MapStruct mapa : world){
            for(int i = 0;i<mapa.getColisionbox().size();i++){
               spriteB.draw(mapa.getTextura(),mapa.getColisionbox().get(i).x,mapa.getColisionbox().get(i).y);
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
