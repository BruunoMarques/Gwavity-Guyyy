package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.Gguy;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Classe para gerir o estado de menu
 */
public class EstadoMenu extends EstadoBase {
    //private Texture wallpapper;
    //private Texture btn1;
    private final String nomeWallpaper = "background/wallpaper.png";
    private final String SinglePlayer = "utility/singleplayer.png";
    private final String Options = "utility/options.png";
    private final String Host = "utility/server.png";
    private final String Client = "utility/client.png";
    private final String Exit = "utility/exit.png";
    private Botao btn1; //singleplayer
    private Botao btn2; //opcoes
    private Botao btn3; //servidor
    private Botao btn4; //client
    private Botao btn5; //exit
    private boolean alteracaoEstado;
    private final static String TAG = "infoMessage";
	
	/**
     * Construtor de EstadoMenu.
     * Inicializa o fundo, os botoes e a musica do estado
     * @param emg EstadosManager a que o atual EstadoMenu esta associado
     */
    public EstadoMenu(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture(nomeWallpaper);
        btn1 = new Botao(SinglePlayer,WIDTH/16,HEIGHT/2-HEIGHT/16);
        btn2 = new Botao(Options,WIDTH/16,HEIGHT/2-HEIGHT/4);
        btn3 = new Botao(Host,WIDTH/2+WIDTH/5,HEIGHT/2-HEIGHT/16);
        btn4 = new Botao(Client,WIDTH/2+WIDTH/5,HEIGHT/2-HEIGHT/4);
        btn5 = new Botao(Exit,WIDTH/2+WIDTH/3,HEIGHT/2+HEIGHT/4);

        alteracaoEstado = true;

        music = Gdx.audio.newMusic(Gdx.files.internal("music/kendrick.mp3"));
        music.setLooping(false);
        music.setVolume(this.emg.soundVolume);
        music.play();
    }

	/**
     * Processa o input do utilizador.
     * Se houver click no botao de inicio de jogo, usa o EstadosManager associado para remover o estado atual e adicionar um EstadoJogo ao topo da pilha
	 * Se houver click no botao de opcoes, usa o EstadosManager associado para remover o estado atual e adicionar um EstadoOpcoes ao topo da pilha
     * Se houver click no botao de saida, sai da aplicacao
	 */
    @Override
    protected void handleInput() {
        Logger banana = new Logger(TAG,Logger.INFO); // works
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.addEstado(new EstadoJogo(emg));
            banana.info("Finito Main Menu");
        }
        else if(Gdx.input.justTouched() && btn2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            //emg.remEstadoAct();
            emg.addEstado(new EstadoOpcoes(emg,music));
            //banana.info("Finito Main Menu");
        }
        else if(Gdx.input.justTouched() && btn3.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Servidor");
            emg.remEstadoAct();
            emg.addEstado(new EstadoHost(emg));

        }
        else if(Gdx.input.justTouched() && btn4.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Cliente");
            emg.remEstadoAct();
            emg.addEstado(new EstadoClient(emg));
        }
        else if(Gdx.input.justTouched() && btn5.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            Gdx.app.exit();
        }
    }

	/**
     * Chama a funcao handleInput.
     * @param dt intervalo de tempo entre atualizacoes
     */
    @Override
    public void update(float dt) {
        handleInput();
    }

	/**
     * Desenha o estado no ecra. Isto inclui fundo e botoes
     * @param spriteB
     */
    @Override
    public void render(SpriteBatch spriteB) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //repor a posicao da sprite para a inicial (util visto que o jogo muda o campo de visao)
        if(alteracaoEstado){
            //para dar fix da camara pq isto e estupido
            camara.setToOrtho(false,WIDTH,HEIGHT);
            camara.position.x = WIDTH/2;
            camara.position.y = HEIGHT/2;
            camara.update();
            spriteB.setProjectionMatrix(camara.combined);
            alteracaoEstado = false;
        }
        spriteB.begin();
        spriteB.draw(wallpapper,0,0,WIDTH,HEIGHT);
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y,btn1.getWidth(),btn1.getHeight());
        spriteB.draw(btn2.getButton(),btn2.getCoord().x,btn2.getCoord().y,btn2.getWidth(),btn2.getHeight());
        spriteB.draw(btn3.getButton(),btn3.getCoord().x,btn3.getCoord().y,btn3.getWidth(),btn3.getHeight());
        spriteB.draw(btn4.getButton(),btn4.getCoord().x,btn4.getCoord().y,btn4.getWidth(),btn4.getHeight());
        spriteB.draw(btn5.getButton(),btn5.getCoord().x,btn5.getCoord().y,btn5.getWidth(),btn5.getHeight());
        spriteB.end();
    }

	/**
	* Descarta memoria (fundo, botoes e musica)
	*/
    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
        btn2.disposeButton();
        btn3.disposeButton();
        btn4.disposeButton();
        btn5.disposeButton();
        music.dispose();
    }

}
