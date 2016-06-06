package com.gguy.game.estados;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.gguy.game.estados.ferramentas.Network;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.estados.ferramentas.ScreenStuff;
import com.gguy.game.estados.ferramentas.SomeRequest;
import com.gguy.game.estados.ferramentas.SomeResponse;
import com.gguy.game.gamestuff.Guy;

import java.io.IOException;

/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoClient extends EstadoBase {
    // private Texture wallpapper;
    //private Texture btn1;
    private Botao btn1;
    private Botao btn2;
    //private ScreenStuff sf = new ScreenStuff();
    private boolean alteracaoEstado;
    private final static String TAG = "infoMessage";
    Client client;
    private  boolean wtf = true;

    public void initializeClient(String ip){
        client = new Client();
        client.start();
        Network.register(client);

        client.addListener(new Listener(){
            public void received(Connection connection, Object object){
                if(object instanceof SomeResponse){
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                }
                else if(object instanceof SpriteBatch){
                    System.out.println("magic");
                }
                else if(object instanceof Guy){
                    System.out.println("guy received");
                }
            }
            public void disconnected (Connection c){

            }
        });

        new Thread("Client"){
            public void run(){
                try{
                    client.connect(5000,"127.0.0.1",Network.port,54777);
                    SomeRequest request = new SomeRequest();
                    request.text = emg.skinSelected.getName();
                    client.sendTCP(request);
                }catch(IOException io){
                    System.out.println("Failed connection");
                    client.stop();
                    wtf = false;
                }
            }
        }.start();
    }

    public EstadoClient(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper.png"); //todo fazer defines disto para ficar bonito
        btn1 = new Botao("utility/singleplayer.png",200,200);
        btn2 = new Botao("utility/options.png",500,200);

        alteracaoEstado = true;


        initializeClient("89.153.47.165");
    }

    @Override
    protected void handleInput() {//todo por coordenadas certas para os botoes :P
        Logger banana = new Logger(TAG,Logger.INFO); // works

        if(Gdx.input.justTouched()) {
            SomeRequest request = new SomeRequest();
            request.text = "clicked";
            client.sendTCP(request);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(!wtf){
            this.emg.remEstadoAct();
            this.emg.addEstado(new EstadoMenu(emg));
        }

    }

    @Override
    public void render(SpriteBatch spriteB) {

        spriteB.begin();
        spriteB.draw(btn1.getButton(),0,0);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
        btn2.disposeButton();
    }

}
