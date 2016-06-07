package com.gguy.game.estados;


import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gguy.game.estados.ferramentas.GuyPositions;
import com.gguy.game.estados.ferramentas.MapWithoutTextures;
import com.gguy.game.estados.ferramentas.Network;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.gguy.game.estados.ferramentas.SomeRequest;
import com.gguy.game.estados.ferramentas.SomeResponse;
import com.gguy.game.estados.ferramentas.WinningMessage;
import com.gguy.game.gamestuff.Guy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 30-04-2016.
 * Classe responsavel por simular um client
 * Esta e responsavel por:
 * enviar ao servidor informacao de conneccao, e de que ocorre um click, quando este efetuar
 * recebe do servidor informacao do jogo, como a posicao da camara e dos diversos jogadores, e do mapa criado pelo servidor
 * O client basicamente e um receptor de informacao do servidor, sendo que toda a informacao que este da display provem do servidor
 * Infelizmente, a funcao de pesquisa de ips do kryonet nao funciona para android, logo estamos a usar um ip hardcoded local
 */
public class EstadoClient extends EstadoBase {
    // private Texture wallpapper;
    //private Texture btn1;
    private Botao btn1;
    //more hardcoding
    //servidor tem de ter resolucao 1280 / 720
    private Texture baseColor;
    private MapWithoutTextures MWT = new MapWithoutTextures();
    private ArrayList<Vector2> PlayersPosition = new ArrayList<Vector2>();
    private ArrayList<Guy> Players = new ArrayList<Guy>();
    private ArrayList<Integer> OrientationPlayer = new ArrayList<Integer>();
    private final static String TAG = "infoMessage";
    private float timePassed = 0;
    private float speed = 201; //hardcoding intensifies
    private float numberOfPlayers = 0;
    Client client;
    private int StateMachineConnection = 0; //0 = standbye, 1 = ligou, 2 = desconectou, 3 = espera de players
    private boolean addPlayer = false;

    /**
     * funcao responsavel pela criacao de um client
     * Este vai tentar ligar a um servidor, que em caso de sucesso, envia uma mensagem para este, recebendo como resposta o nº de jogadores que la estao
     * Este depois vai prontamente criar jogadores localmente, cada um representando um jogador.
     * Este client tem 1 listener, cujo comportamento varia, dependendo do tipo de informacao que recebe
     * Esta informacao varia desde a posicao dos jogadores, ate ao estado destes(se estao a voar, pousados no chao, etc);
     */
    public void initializeClient(){
        client = new Client();
        client.start();
        Network.register(client);
        client.addListener(new Listener(){
            public void received(Connection connection, Object object){
                if(object instanceof SomeResponse){
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                    if(response.text.equals("success")){
                        StateMachineConnection = 1;
                        numberOfPlayers = response.number;
                        addPlayer = true;
                    }
                }
                else if(object instanceof Vector3){
                    camara.position.set((Vector3)object);
                    camara.update();
                }
                else if(object instanceof ArrayList){
                    ArrayList<Vector2> testing = (ArrayList)object;
                    PlayersPosition = testing;
                    for(int i = 0;i < Players.size();i++)Players.get(i).setPosicao(PlayersPosition.get(i));
                }
                else if (object instanceof GuyPositions){
                    OrientationPlayer = ((GuyPositions) object).positions;
                }
                else if(object instanceof MapWithoutTextures){
                    MWT = (MapWithoutTextures)object;
                }
                else if(object instanceof WinningMessage){
                    System.out.println(((WinningMessage) object).message);
                    btn1.changeViewPosX((WIDTH/2-WIDTH/12)/2);
                    speed = 201; //hardcoding intensifies
                    timePassed = 0;
                }
            }
            public void disconnected (Connection c){
                client.stop();
                StateMachineConnection = 2;
            }
        });

        new Thread("Client"){
            public void run(){
                try{
                    client.connect(5000,"192.168.1.2",Network.port,54777);
                    SomeRequest request = new SomeRequest();
                    request.text = emg.skinSelected.getName();
                    client.sendTCP(request);
                }catch(IOException io){
                    System.out.println("Failed connection");
                    client.stop();
                    StateMachineConnection = 2;
                }
            }
        }.start();
    }

    /**
     * construtor do estadoclient inicializa variaveis de logica de jogo como texturas e da camara,
     * bem como a criacao de um cliente
     * @param emg estado manager responsavel
     */
    public EstadoClient(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/bck1.png");
        btn1 = new Botao("utility/backbutton.png",WIDTH/2-WIDTH/12,HEIGHT*3/4 + HEIGHT/8); //todo fix this wtf!
        btn1.duplicateButtonSize();
        btn1.setViewPoint((WIDTH/2-WIDTH/12)/2,HEIGHT*3/4 - HEIGHT/14);
        baseColor = new Texture("map/base_color.png");

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        initializeClient();
    }

    /**
     * funcao responsavel por gerir o input do utilizador
     * se carregar no botao 1, este retona para o menu inicial, parando o client, que envia ao servidor uma notificacao da sua saida
     * ao clicar noutro local qualquer, envia informacao ao servidor de que deseja alterar a gravidade da sua personagem
     */
    @Override
    protected void handleInput() {
        Logger banana = new Logger(TAG,Logger.INFO); // works

        if(Gdx.input.justTouched()) {
            SomeRequest request = new SomeRequest();
            request.text = "clicked";
            client.sendTCP(request);
        }
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            client.stop();
            StateMachineConnection = 2;
        }
    }

    /**
     * funcao responsavel por atualizar a velocidade, tempo decorrido, posicao da camara e criacao de novos jogadores, caso seja preciso
     * Estas alteracoes sao condicionados, dependendo do atual estado do StateMachineConnection
     * @param dt intervalo de tempo de update
     */
    @Override
    public void update(float dt) {
        handleInput();
        if(StateMachineConnection == 2){
            this.emg.remEstadoAct();
            this.emg.addEstado(new EstadoMenu(emg));
        }
        /*
        else if (OrientationPlayer.size() != Players.size() || PlayersPosition.size() != Players.size()){
            StateMachineConnection = 1;
        }*/
        else if (StateMachineConnection == 1 || StateMachineConnection == 3){
            if(Players.size() > 0 && OrientationPlayer.size() == Players.size() && PlayersPosition.size() == Players.size()){
                StateMachineConnection = 3;
            }
            if(StateMachineConnection == 3){
                timePassed += dt;
                speed += dt;
                btn1.changeViewPosX(btn1.getCoordView().x + (speed * dt));
            }
            if(addPlayer){
                for(int i = Players.size();i<numberOfPlayers;)
                {
                    Random r = new Random();
                    SkinInfo sk = emg.skins.getSkins().get(r.nextInt(emg.skins.getSkins().size()));
                    Players.add(new Guy(sk ,0,150,10));
                    i = Players.size();
                }
                addPlayer = false;
            }

        }
        camara.update();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();
        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        if(StateMachineConnection == 3){
            for(int i = 0;i < Players.size();i++){
                Guy gguy = Players.get(i);
                if(OrientationPlayer.get(i) == 1)spriteB.draw(gguy.getJumpAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,45*W_RES,45*H_RES);
                else if(OrientationPlayer.get(i) == 0)spriteB.draw(gguy.getWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
                else spriteB.draw(gguy.getInverseWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
            }
            for(int i =0; i < MWT.rectangles.size();i++){
                spriteB.draw(baseColor,MWT.rectangles.get(i).x,MWT.rectangles.get(i).y,MWT.rectangles.get(i).getWidth()*W_RES,MWT.rectangles.get(i).getHeight()*H_RES);
            }
        }
        spriteB.draw(btn1.getButton(),btn1.getCoordView().x,btn1.getCoordView().y,btn1.getWidth()/2,btn1.getHeight()/2);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        for(Guy gguy : Players)
            gguy.freeMemory();
        btn1.disposeButton();
        baseColor.dispose();
    }

}
