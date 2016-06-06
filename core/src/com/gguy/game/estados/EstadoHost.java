package com.gguy.game.estados;

//import com.gguy.game.Online.Comunicar;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.gguy.game.Gguy;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.estados.ferramentas.Network;
import com.gguy.game.estados.ferramentas.ScreenStuff;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.gguy.game.estados.ferramentas.SomeRequest;
import com.gguy.game.estados.ferramentas.SomeResponse;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.MapGenerator;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoHost extends EstadoBase {
    private Botao btn1;
    private Botao btn2;
    private final static String TAG = "infoMessage";

    //GERAR MAPA
    private ObjectSpace objectSpace;

    private String ipAddress = new String("");
    private Server server;
    private int numPlayers = 0;
    private ArrayList<String> typePlayers = new ArrayList<String>();
    private float timePassed = 0;
    private final float speedInicial = 10;

    private float speed = speedInicial;
    private final int pos_inicialX = 150;
    private final int pos_inicialY = HEIGHT/2;

    private MapGenerator geradorMapa;
    private ArrayList<MapStruct> world;
    private ArrayList<Guy> Players = new ArrayList<Guy>();

    private ArrayList<Rectangle> currentStepping = new ArrayList<Rectangle>();
    private int StateMachineWalking = 0;
    private boolean colidiu = false;

    private float flyingWidth = 0;
    private float flyingHeight = 0;
    private String currentlySelectedSkin = "";
    private boolean addedPlayer = false;
    private boolean closeServer = false;
    public void initializeServer(){
        server = new Server(){
            protected  Connection newConnection(){
                numPlayers++;
                addedPlayer = true;
                Random r = new Random();
                ServerConnection sv = new ServerConnection();
                flyingHeight = 50 * H_RES;
                flyingWidth = 50 * W_RES;
                return sv;
            }
        };
        Network.register(server);
        server.addListener(new Listener(){
            public void received (Connection connection, Object object){
                ServerConnection c = (ServerConnection)connection;


                if(object instanceof SomeRequest){
                    SomeRequest request = (SomeRequest)object;
                    System.out.println(request.text);
                    currentlySelectedSkin = request.text;

                    SomeResponse response = new SomeResponse();
                    response.text = "Received";
                    connection.sendTCP(response);
                    System.out.println("" + c.name);
                    for(int i = 0; i< Players.size();i++){
                        if(c.name-1 == i && !Players.get(i).isGuyFlying()){
                            Players.get(i).fixPosY(getColisionLimit(i));
                            Players.get(i).changeGravity();
                        }
                    }
                }
            }
            public void disconnected(Connection connection){
                System.out.println("cenas");
                ServerConnection c = (ServerConnection) connection;
                Players.remove(c.name-1);
                if(Players.size() == 0 && speed != speedInicial){
                    server.stop();
                    closeServer = true;
                }
            }
        });



        try{
            server.bind(Network.port,54777);
        }catch (IOException io){
            System.out.println("somehow failed idk");
            server.stop();
            //    server.close();
        }
        server.start();
    }

    class ServerConnection extends Connection {
        public int name = numPlayers;
    }

    public EstadoHost(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper.png");

        //GERAR MAPA
        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();
        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;


        initializeServer();
    }

    private float getColisionLimit(int indice){
        Rectangle test = new Rectangle();
        test.set(Players.get(indice).getColisaoBox());
        boolean colidiuBaixo = Players.get(indice).normalGravity();
        float ret = 0;
        while(test.overlaps(currentStepping.get(indice))){
            if(colidiuBaixo) ret--;
            else ret++;
            test.y += ret;
        }
        return test.y;
    }

    private float getSizeVector(float x, float y){
        return (float) Math.sqrt((double)(x*x + y*y));
    }

    private boolean FrontalColision(Rectangle rect, Guy gguy){
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
        Logger banana = new Logger(TAG,Logger.INFO); // works
        String textMessage = "bananas";
        if(Gdx.input.justTouched()) {
        }
    }

    private boolean updateMap(int indice){
        boolean needsChange = false;
        boolean noGround = true;
        boolean hitWall = false;
        for(int i = 0;i<world.size();i++) {
            MapStruct obstaculo = world.get(i);
            if (obstaculo.ColideGuy(Players.get(indice).getColisaoBox())) {
                if(StateMachineWalking == 1 && currentStepping.get(indice).y == obstaculo.getLastColided().y){
                    noGround = false;
                    currentStepping.set(indice,obstaculo.getLastColided());
                }
                else if(FrontalColision(obstaculo.getLastColided(),Players.get(indice))){
                    hitWall = true;
                }
                else{
                    Players.get(indice).atingeChao();
                    StateMachineWalking = 1;
                    currentStepping.set(indice,obstaculo.getLastColided());
                    noGround = false;
                }
            }
            if (camara.position.x - (camara.viewportWidth / 2) > geradorMapa.smallestDistance) {
                needsChange = true;
            }
        }
        if(needsChange){
            world = geradorMapa.generateMap();
        }
        if(noGround && !Players.get(indice).isGuyFlying()){
            Players.get(indice).changeGravity();
            Players.get(indice).changeGravity();
        }
        if(noGround) StateMachineWalking = 0;

        return hitWall;
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(addedPlayer){
            Random r = new Random();
            SkinInfo sk = emg.skins.getSkins().get(r.nextInt(emg.skins.getSkins().size()));
            Players.add(new Guy(sk ,pos_inicialX,pos_inicialY,speedInicial));
            currentStepping.add(new Rectangle(0,0,0,0));
            addedPlayer = false;
        }
        if(Players.size() > 0){
            for(int i = 0; i < Players.size(); i++){
          /*  if(gguy.getPosicao().x > camara.position.x + WIDTH/6 )gguy.updatePos(dt,colidiu);
            else gguy.updatePos(dt,colidiu);*/
                Players.get(i).updatePos(dt,updateMap(i));
                //todo Falta transferir informaçao de volta para desenhar para o cliente
            }

            timePassed += dt;
            camara.position.x += speed * dt;
            speed += dt;
            camara.update();

        }
        if(closeServer){
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        /*
        for(Connection c : server.getConnections()){
            objectSpace.addConnection(c);
        }
        */
    }

    @Override
    public void render(SpriteBatch spriteB) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();

        spriteB.draw(wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        for(Guy gguy : Players){
            if(gguy.isGuyFlying())spriteB.draw(gguy.getJumpAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,flyingWidth,flyingHeight);
            else if(!gguy.normalGravity())spriteB.draw(gguy.getWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
            else spriteB.draw(gguy.getInverseWalkAnimation().getKeyFrame(timePassed, true),gguy.getPosicao().x,gguy.getPosicao().y,gguy.getColisaoBox().getWidth(),gguy.getColisaoBox().getHeight());
        }

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
        geradorMapa.disposeMap();
        for(Guy gguy : Players)
            gguy.freeMemory();
    }

}
