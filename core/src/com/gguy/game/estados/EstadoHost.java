package com.gguy.game.estados;

//import com.gguy.game.Online.Comunicar;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.estados.ferramentas.GuyPositions;
import com.gguy.game.estados.ferramentas.MapWithoutTextures;
import com.gguy.game.estados.ferramentas.Network;
import com.gguy.game.estados.ferramentas.SkinInfo;
import com.gguy.game.estados.ferramentas.SomeRequest;
import com.gguy.game.estados.ferramentas.SomeResponse;
import com.gguy.game.estados.ferramentas.WinningMessage;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.MapGenerator;
import com.gguy.game.gamestuff.obstaculos.MapStruct;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Jonas on 30-04-2016.
 */
public class EstadoHost extends EstadoBase {
    private Botao btn1;
    private Botao btn2;
    private final static String TAG = "infoMessage";

    //GERAR MAPA

    private String ipAddress = new String("");
    private Server server;
    private int numPlayers = 0;
    private float timePassed = 0;
    private final float speedInicial = 201; //cause why not >:D

    private float speed = speedInicial;
    private final int pos_inicialX = 150;
    private final int pos_inicialY = HEIGHT/2;
    //todo mutexes todo é opcional
    private MapGenerator geradorMapa;
    private ArrayList<MapStruct> world;
    private ArrayList<Guy> Players = new ArrayList<Guy>();

    private ArrayList<Rectangle> currentStepping = new ArrayList<Rectangle>();
    private ArrayList<Boolean> alive = new ArrayList<Boolean>();

    private int StateMachineWalking = 0;
    private float flyingWidth = 0;
    private float flyingHeight = 0;
    private boolean addedPlayer = false;
    private boolean closeServer = false;

    public void initializeServer(){
        server = new Server(){
            protected  Connection newConnection(){
                numPlayers++;
                addedPlayer = true;
                ServerConnection sv = new ServerConnection();
                flyingHeight = 45 * H_RES;
                flyingWidth = 45 * W_RES;
                return sv;
            }
        };
        Network.register(server);
        server.addListener(new Listener(){
            public void received (Connection connection, Object object){
                ServerConnection c = (ServerConnection)connection;
                if(object instanceof SomeRequest){
                    SomeRequest request = (SomeRequest)object;

                    SomeResponse response = new SomeResponse();
                    System.out.println(request.text);
                    if(!request.text.equals("clicked"))
                    {
                        response.text = "success";
                        response.number = Players.size() + 1;
                        server.sendToAllTCP(response);
                    }
                    else {
                        response.text = "Received";
                        connection.sendTCP(response);
                    }

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
                System.out.println("player disconnected");
                ServerConnection c = (ServerConnection) connection;

                server.stop();
                closeServer = true;

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

    class ServerConnection extends Connection{
        public int name = numPlayers;
    }

    public void restartServer(){
        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;

        int size = Players.size();
        for(int i = 0;i < size; i++) {
            Players.get(0).freeMemory();
            Players.remove(0);
        }
        for(int i = 0;i < size;i++) {
            SkinInfo sk = emg.skins.getSkins().get(1);
            Players.add(new Guy(sk, pos_inicialX, pos_inicialY, speedInicial));
            currentStepping.set(i, new Rectangle(0, 0, 0, 0));
            alive.set(i, true);
        }

        timePassed = 0;
        speed = speedInicial;

        StateMachineWalking = 0;

        geradorMapa.disposeMap();
        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();
    }

    public EstadoHost(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/bck2.png");

        //GERAR MAPA
        geradorMapa = new MapGenerator();
        world = geradorMapa.initializeMap();
        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;
        camara.update();
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
    }

    private boolean updateMap(int indice){
        boolean needsChange = false;
        boolean noGround = true;
        boolean hitWall = false;
        MapWithoutTextures mt = new MapWithoutTextures();
        mt.rectangles.clear();
        mt.types.clear();
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
                System.out.println(":O " + geradorMapa.smallestDistance);
            }
            for(int j = 0;j<obstaculo.getColisionbox().size();j++){
                mt.rectangles.add(obstaculo.getColisionbox().get(j));
            }
            server.sendToAllTCP(mt);
        }
        if(needsChange){
            System.out.println("bananas");
            world = geradorMapa.generateMap(speed);
        }
        if(noGround && !Players.get(indice).isGuyFlying()){
            Players.get(indice).changeGravity();
            Players.get(indice).changeGravity();
        }
        if(noGround) StateMachineWalking = 0;

        return hitWall;
    }

    private boolean gameOver(Guy gguy){
        return (gguy.getPosicao().y > camara.position.y + HEIGHT/4 ||
                gguy.getPosicao().y + gguy.getColisaoBox().getHeight() <  camara.position.y - HEIGHT/4 ||
                gguy.getPosicao().x + gguy.getColisaoBox().getWidth() < camara.position.x - WIDTH/4 );
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(addedPlayer){
            SkinInfo sk = emg.skins.getSkins().get(1);
            Players.add(new Guy(sk ,pos_inicialX,pos_inicialY,speedInicial));
            currentStepping.add(new Rectangle(0,0,0,0));
            alive.add(true);
            addedPlayer = false;
        }
        if(Players.size() > 0){
            ArrayList<Vector2> positions = new ArrayList<Vector2>();
            GuyPositions guyPositions = new GuyPositions();
            int over = 0; // numero de jogadores que morreram
            int indice_vencedor = 0;
            for(int i = 0; i < Players.size(); i++){
                Guy gguy = Players.get(i);

                gguy.updatePos(dt,updateMap(i));
                if(gguy.getPosicao().x > camara.position.x)gguy.ignoreBonusAccel();
                positions.add(gguy.getPosicao());
                alive.set(i,!gameOver(gguy));
                if(!alive.get(i))over ++;
                else indice_vencedor = i +1;
                if(gguy.isGuyFlying())guyPositions.positions.add(1);
                else if(gguy.normalGravity())guyPositions.positions.add(2);
                else guyPositions.positions.add(0);
            }
            if(Players.size() <= over){
                //acaba o jogo
                WinningMessage win = new WinningMessage();
                win.message = "Player " + indice_vencedor + " Won!";
                server.sendToAllTCP(win);
                restartServer();
            }
            server.sendToAllTCP(guyPositions);
            server.sendToAllTCP(positions);
            timePassed += dt;
            camara.position.x += speed * dt;
            speed += dt;
            camara.update();
            server.sendToAllTCP(camara.position);

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
                spriteB.draw(mapa.getTextura(),mapa.getColisionbox().get(i).x,mapa.getColisionbox().get(i).y,mapa.getColisionbox().get(i).getWidth(),mapa.getColisionbox().get(i).getHeight());
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
