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
 * Classe que simula o servidor de jogo
 * Esta e responsavel por: gerir a criacao do mapa
 * controlar a posicao do jogador
 * ver quando e que o cliente pretende alterar a gravidade, e executa essa alteracao
 * controlar as colisoes entre diversos jogadores com o mapa
 * enviar aos clientes o estado do jogo atual (informacao da posicao de todos os jogadores, de como esta o mapa, de quem ganhou o jogo, etc)
 * Esta recebe inputs do client, para ver quando ocorre alteracao de gravidade, quando um cliente entra ou sai.
 * Neste ultimo caso, se alguem sair a meio de um jogo a decorrer, o servidor acaba e expulsa os jogadores, notificando-os de tal acontecimento.
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
    //todo fix mutexes
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

    /**
     * funcao responsavel por inicializar o servidor do kryonet
     * Este e criado sob forma de thread, tendo um listener que recebe informacao do cliente, quando este envia mensagens
     * Quando um cliente se liga ao servidor, este cria um jogador e associa ao cliente, conseguindo este controla-lo apartir do seu dispositivo
     * Tambem lhe e associado um numero de cliente, para que o servidor saiba de quem esta a receber mensagens
     * O listener, responsavel por controlar as mensagens do client, gere apenas respostas de quando o client entra no server, quando sai e quando o client carrega no ecra
     * E necessario registar inicialmente ao servidor, quais tipos de classes e que irao ser enviadas/recebidas, para efetuar uma serializacao previa.(feita automaticamente pelas librarias kryo)
     */
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

    /**
     * class responsavel por atribuir  numero ao client, quando este se liga ao servidor
     */
    class ServerConnection extends Connection{
        public int name = numPlayers;
    }

    /**
     * funcao responsavel por repor as definicoes iniciais de jogo
     */
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

    /**
     * construtor do servidor
     * inicializa o servidor e outros parametros de jogo, como a camara e o mapa
     * @param emg estado manager responsavel pelo servidor
     */
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

    /**
     * Funcao responsavel por evitar a colisao do jogador, quando este salta
     * @param indice indice do jogador a corrigir posicao
     * @return posicao corrigida do jogador, para que nao haja colisao
     */
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
     * @param gguy jogador a testar colisao
     * @return true se for uma colisao frontal, falso se for uma colisao superior ou inferior
     */
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
    /**
     * Renova o mapa e verifica o estado do jogador.
     * Se fim do mapa já gerado estiver a aproximar-se da parte visivel do ecra, é gerado mais mapa.
     * Se o jogador tiver colidido com algum obstaculo frontalemnte, e impedido de avancar no mapa.
     * Se o jogador estiver a meio do ar,e colidir com um objeto(nao frontalmente), este aterra e começa a andar
     * @param indice indice do jogador a verificar colisoes
     * @return true se atingiu um obstaculo frontal
     */
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
            }
            for(int j = 0;j<obstaculo.getColisionbox().size();j++){
                mt.rectangles.add(obstaculo.getColisionbox().get(j));
            }
            server.sendToAllTCP(mt);
        }
        if(needsChange){
            world = geradorMapa.generateMap(speed);
        }
        if(noGround && !Players.get(indice).isGuyFlying()){
            Players.get(indice).changeGravity();
            Players.get(indice).changeGravity();
        }
        if(noGround) StateMachineWalking = 0;

        return hitWall;
    }
    /**
     * Verifica se o jogo acabou para um certo jogador
     * @param gguy jogador a verificar se perdeu o jogo
     */
    private boolean gameOver(Guy gguy){
        return (gguy.getPosicao().y > camara.position.y + HEIGHT/4 ||
                gguy.getPosicao().y + gguy.getColisaoBox().getHeight() <  camara.position.y - HEIGHT/4 ||
                gguy.getPosicao().x + gguy.getColisaoBox().getWidth() < camara.position.x - WIDTH/4 );
    }

    /**
     * Atualiza o estado do jogo.
     * O jogo apenas comeca quando existirem, pelo menos ,2 jogadores conectados
     * Esta funcao envia informacao aos clients, dando-lhes update do estado do jogo atual(posicao do mapa, posicao dos jogadores e posicao da camara)
     * Chama as funcoes para verificar se houve fim de jogo e lidar com input.
     * Atualiza a posicao dos jogadores de acordo com o tempo que passa
     * atualiza a posicao da camara, de modo a controlar se os jogadores perdem, ou nao
     * invoca o update do mapa, que ira tratar das possiveis colisoes entre este e os jogadores
     * O jogo acaba quando fica apenas um jogador em cena, dando restart quando este acaba e envia informacao aos jogadores de quem venceu
     * @param dt tempo passado entre atualizacoes
     */
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
        if(Players.size() > 1){
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
            if(Players.size()-1 <= over){
                //acaba o jogo
                WinningMessage win = new WinningMessage();
                win.message = "Player " + indice_vencedor + " Won!";
                System.out.println(win.message);
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

    /**
     * funcao responsavel por desenhar o decorrer do jogo para o ecra
     * @param spriteB sprite unica de jogo para onde se desenha tudo o que e visivel
     */
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
    /**
     * Descarta memoria de metodos que a alocaram(fundo, jogador, botoes, musica, o gerador de mapa)
     */
    @Override
    public void freeMemory() {
        wallpapper.dispose();
        geradorMapa.disposeMap();
        for(Guy gguy : Players)
            gguy.freeMemory();
    }

}
