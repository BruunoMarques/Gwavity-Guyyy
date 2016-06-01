package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.obstaculos.WalkPlatform;

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
    private float speed = 200; //todo mudar dp, isto e temp
    private int nObstaculos = 6;
   // private Array<Muro> obstaculos;
    private Array<WalkPlatform> walkPlats;
    private WalkPlatform currentStepping;//todo fazer isto dp, estou a curtir James Blake
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

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        pause_menu.setViewPoint(0,HEIGHT*3/4 - 50);
        use_power.setViewPoint(200,HEIGHT*3/4 - 50);


        walkPlats = new Array<WalkPlatform>();
        for(int i = 0;i< nObstaculos;i++){
            walkPlats.add(new WalkPlatform(0));
            walkPlats.get(i).reposition(i*walkPlats.get(i).PLATF_WIDTH);
        }

    }

    private float getColisionLimit(){
        float y1 = currentStepping.getPartBaixo().y + gguy.getColisaoBox().getHeight();
        float y2 = currentStepping.getPartCima().y - currentStepping.getPlatf().getHeight();
        float yg = gguy.getPosicao().y;

        Logger banana = new Logger(TAG,Logger.INFO);
        String cenas = "colidiu " + gguy.getColisaoBox().y + " com " + y1;
        banana.info(cenas);

        if(yg <= y1) return y1+5;
        else if (yg >= y2) return y2-5;
        else return yg;


    }

    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched() && pause_menu.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        else if(Gdx.input.justTouched() && !gguy.isGuyFlying()){
            Logger banana = new Logger(TAG,Logger.INFO);
            String cenas = "pos_click" + Gdx.input.getX() + ":" + Gdx.input.getY();
            banana.info(cenas);
            gguy.fixPosY(getColisionLimit());
            gguy.changeGravity();
            gguy.playJumpSound();
        }
    }

    @Override
    public void update(float dt) {
        if(!music.isPlaying()){
            music.dispose();
            playMusic();
        }
        handleInput();
        gguy.updatePos(dt);
        timePassed += dt;
        camara.position.x = gguy.getPosicao().x;

        speed += dt;
        use_power.changeViewPosX(use_power.getCoordView().x + (speed * dt));
        pause_menu.changeViewPosX(pause_menu.getCoordView().x + (speed * dt));

        for(WalkPlatform obstaculo : walkPlats){
            if(camara.position.x - (camara.viewportWidth/2) > obstaculo.getPartCima().x + obstaculo.PLATF_WIDTH)
            {
                Random r = new Random();
                if(r.nextInt(4) != 2 ) obstaculo.reposition(obstaculo.getPartCima().x + obstaculo.PLATF_WIDTH*nObstaculos);
            }
            if(obstaculo.ColideGuy(gguy.getColisaoBox())){
                gguy.atingeChao(); //todo isto está buggy!!
                currentStepping = obstaculo;
            //    Logger banana = new Logger(TAG,Logger.INFO);
            //    String cenas = "colidiu " + gguy.getColisaoBox().y + " com " + obstaculo.getPartBaixo().y;
            //    banana.info(cenas);
            }
        }
        camara.update();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();

        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(use_power.getButton(),use_power.getCoordView().x,use_power.getCoordView().y);
        spriteB.draw(pause_menu.getButton(),pause_menu.getCoordView().x,pause_menu.getCoordView().y);

        if(gguy.isGuyFlying())spriteB.draw(gguy.getJumpAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);
        else if(!gguy.normalGravity())spriteB.draw(gguy.getWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);
        else spriteB.draw(gguy.getInverseWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y);

        for(WalkPlatform obstaculo : walkPlats){
            spriteB.draw(obstaculo.getPlatf(),obstaculo.getPartCima().x,obstaculo.getPartCima().y);
            spriteB.draw(obstaculo.getPlatf(),obstaculo.getPartBaixo().x,obstaculo.getPartBaixo().y);
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
        for(WalkPlatform obstaculo : walkPlats){
            obstaculo.freeMemory();
        }
    }
}
