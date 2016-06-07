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
import com.gguy.game.gamestuff.PowerUp;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import com.gguy.game.gamestuff.obstaculos.WalkPlatform;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 01-05-2016.
 * Classe para controlo do jogo em modo singleplayer.
 * Esta controla tudo o que for relacionado com a logica de jogo: cria e gere o mapa, á medida que o jogador for avancando;
 * -controla a posicao do jogador
 * -controla a interacao do jogador com o mapa(trata das colisoes)
 * -controla a interacao do jogador com power ups,
 * -etc..
 */
public class EstadoJogo extends EstadoBase {
    private Guy gguy;
   // private SpriteBatch video_mem;
    private Botao back_menu;
    private Botao use_power;

    private float timePassed = 0;
    private float lastime = 0; //cgra ftw
    private final float speedInicial = 200 * W_RES;

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

    private PowerUp powerup;
    private int score=0;
    private int bonus=0;
    public boolean flight = false;

    private final static String TAG = "infoMessage";
    /**
     * Inicializa a musica a reproduzir durante o jogo e comeca a reproducao
     * esta e executada sempre que a musica acaba
     */
    private void playMusic(){
        String selectedMusic = "music/" + emg.musicSelected.selectRandomMusic();
        music = Gdx.audio.newMusic(Gdx.files.internal(selectedMusic));
        music.setLooping(false);
        music.setVolume(emg.soundVolume/2);
        music.play();
    }
    /**
     * Construtor de EstadoJogo.
     * Inicializa todas as variaveis necessarias para correr o jogo em si.
     * Isto inclui o fundo, a aparência selecionada para o jogador e as suas animacoes, os botoes, a camara e o gerador de mapa
     * @param emg - EstadosManager a que o EstadoJogo esta associado
     */
    protected EstadoJogo(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/bck3.png");
        playMusic();
        gguy = new Guy(emg.skinSelected ,pos_inicialX,pos_inicialY,speedInicial);

        back_menu = new Botao("utility/backbutton.png",WIDTH/2+WIDTH/6,HEIGHT*3/4 + HEIGHT/8);
        back_menu.duplicateButtonSize();
        use_power = new Botao("utility/powerbutton.png",WIDTH/2-WIDTH/6,HEIGHT*3/4 + HEIGHT/8);
        use_power.duplicateButtonSize();
        back_menu.setViewPoint((WIDTH/2+WIDTH/6)/2,HEIGHT*3/4 - HEIGHT/14);
        use_power.setViewPoint((WIDTH/2-WIDTH/6)/2,HEIGHT*3/4 - HEIGHT/14);

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;
        camara.update();

        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();

        powerup = new PowerUp(EstadoJogo.this,"map/pokeball.png");

        flyingHeight = gguy.getJumpAnimation().getKeyFrame(timePassed, true).getRegionHeight()*H_RES;
        flyingWidth = gguy.getJumpAnimation().getKeyFrame(timePassed, true).getRegionWidth()*W_RES;
    }
    /**
     * Reinicia o jogo.
     * Retorna todas as variaveis necessarias a logica do jogo aos seus valores iniciais.
     * Isto inclui a posicao o jogador, o tempo passado, o mapa, a camara, etc.
     */
    public void restartGame(){
        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        back_menu.changeViewPosX((WIDTH/2+WIDTH/6)/2);
        use_power.changeViewPosX((WIDTH/2-WIDTH/6)/2);

        powerup.resetFun();//facepalm

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
    /**
     * H^2 = C1^2 + C2^2
     * @param x posicao x de um vetor
     * @param y posicao y de um vetor
     * @return H = sqrt(C1^2 + C2^2)
     */
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
    /**
     * Processa o input do utilizador.
     * Se houve um click no botao de back, usa o EstadosManager atual para remover o estado corrente da pilha e adicionar um EstadoMenu
     * Se houve um click noutro local do ecrã, muda a gravidade do jogador e reproduz o som atribuido aos saltos
     */
    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched() && back_menu.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        else if(Gdx.input.justTouched() && flight){
            gguy.fixPosY(getColisionLimit());
            gguy.changeGravity();
            if(emg.soundVolume > 0.2f) gguy.playJumpSound();
        }
        else if(Gdx.input.justTouched() && !gguy.isGuyFlying() && !flight){
            gguy.fixPosY(getColisionLimit());
            gguy.changeGravity();
            if(emg.soundVolume > 0.2f) gguy.playJumpSound();
        }
    }
    /**
     * Renova o mapa e verifica o estado do jogador.
     * Se fim do mapa já gerado estiver a aproximar-se da parte visivel do ecra, é gerado mais mapa.
     * Se o jogador tiver colidido com algum obstaculo frontalemnte, e impedido de avancar no mapa.
     * Se o jogador estiver a meio do ar,e colidir com um objeto(nao frontalmente), este aterra e começa a andar
     * @return true se atingiu um obstaculo frontal
     */
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
            if (camara.position.x - (camara.viewportWidth / 2) > geradorMapa.smallestDistance) {
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

    /**
     * verifica se ocorreu colisao entre jogador e powerup
     * @return true se colidiu, false otherwise
     */
    private boolean catchPower(){
        return powerup.getRectange().overlaps(gguy.getColisaoBox());
    };
    /**
     *
     * @return score do jogo
     */
    public float getScore(){
        return score;
    }
    /**
     *
     * @return timePassed - o tempo passado desde o inicio do jogo
     */
    public float getTimePassed(){
        return timePassed;
    }

    //condicao para ver se acontece fim de jogo
    /**
     * Verifica se o jogo acabou. Isto é, se o jogador atingiu os limiares da camara.
     */
    private boolean gameOver(){
        return (gguy.getPosicao().y > camara.position.y + HEIGHT/4 ||
                gguy.getPosicao().y + gguy.getColisaoBox().getHeight() <  camara.position.y - HEIGHT/4 ||
                gguy.getPosicao().x + gguy.getColisaoBox().getWidth() < camara.position.x - WIDTH/4 );
    }
    /**
     * Atualiza o estado do jogo.
     * Chama as funcoes para verificar se houve fim de jogo e lidar com input.
     * Atualiza a posicao do jogador de acordo com o tempo que passa
     * atualiza a posicao da camara, de modo a controlar se o jogador perde, ou nao
     * invoca o update do mapa, que ira tratar das possiveis colisoes entre este e o jogador
     * Verifica colisao com o jogador e da refresh da posicao do power up
     * @param dt tempo passado entre atualizacoes
     */
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
        if(gguy.getPosicao().x > camara.position.x - EstadoBase.WIDTH/32)gguy.ignoreBonusAccel();
        gguy.updatePos(dt,colidiu);
        timePassed += dt;
        camara.position.x += speed * dt;
        speed += dt;
        use_power.changeViewPosX(use_power.getCoordView().x + (speed * dt));
        back_menu.changeViewPosX(back_menu.getCoordView().x + (speed * dt));
      //  gguy.updatePos(dt);
        colidiu = updateMap();
        camara.update();

        score = (int) (timePassed) + bonus;

        if (catchPower()){
            powerup.getFun();
            powerup.reposition();
            lastime = timePassed;

        }else if (timePassed - lastime > 8){
            powerup.resetFun();
            powerup.position(gguy.getPosicao());
            lastime = timePassed;
        }
    }

    /**
     * Desenha o estado de jogo no ecra.
     * Isto inclui o fundo, botoes, o jogador e os obstaculos
     * a sprite batch, e lhe dado um zoom de 50%, de forma a que possamos restringir a visualizacao do utilizador
     * nao vendo ele o mapa a ser construido, ou destruido, aumentando tambem a dificuldade de jogo, pois torna-se menos previsivel
     * quais obstaculos se seguem
     * @param spriteB sprite unica da aplicacao, responsavel por desenhar tudo o que for visivel
     */
    @Override
    public void render(SpriteBatch spriteB){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();
        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(powerup.getTexPower(),use_power.getCoordView().x,use_power.getCoordView().y,use_power.getWidth()/2,use_power.getHeight()/2);
        spriteB.draw(back_menu.getButton(),back_menu.getCoordView().x,back_menu.getCoordView().y,back_menu.getWidth()/2,back_menu.getHeight()/2);

        spriteB.draw(powerup.getPower(),powerup.getCoordView().x,powerup.getCoordView().y,powerup.getRectange().getWidth(),powerup.getRectange().getHeight());

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

    /**
     * quem o retard q fez isto
     * nao quis mudar o construtor diz ele
     * @return jogador
     */
    public Guy returnGuy(){
        return gguy;
    };

    /**
     * @return speed de jogo atual
     */
    public float returnSpeed(){
        return speed;
    };

    /**
     * adiciona bonus ao score (genious)
     */
    public void addBonusScore(){bonus += 5;};

    /**
     * Descarta memoria de metodos que a alocaram(fundo, jogador, botoes, musica, o gerador de mapa)
     */
    @Override
    public void freeMemory() {
        wallpapper.dispose();
        gguy.freeMemory();
        use_power.disposeButton();
        back_menu.disposeButton();
        music.dispose();
        powerup.disposePower();
        geradorMapa.disposeMap();
    }
}
