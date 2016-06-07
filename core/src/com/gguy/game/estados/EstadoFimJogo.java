package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Classe para o estado de fim de jogo, a iniciar apos o utilizador perder no estado de jogo
 */
public class EstadoFimJogo extends EstadoBase {
    private EstadoJogo estadoJogo;
    private Botao restart;
    private Botao end;
    private FileHandle fs;
    private boolean newHighscore = true;
    private BitmapFont font;
    private SpriteBatch batch;
    private String scores;
	/**
     * Construtor do EstadoFimJogo.
     * Inicializa as variaveis necessarias ao estado de fim de jogo. Ou seja, a camara, botoes.
     * Escreve no ficheiro informacao sobre o jogo acabado (highscores, duracao, etc)
     * @param emg
     * @param estadoJogo
     */
    public EstadoFimJogo(EstadosManager emg, EstadoJogo estadoJogo){
        super(emg);
        this.estadoJogo = estadoJogo;

        camara.setToOrtho(false, WIDTH/2, HEIGHT/2);
        camara.position.y = HEIGHT/2;
        camara.position.x = this.estadoJogo.camara.position.x;

        restart = new Botao("utility/backbutton.png",camara.position.y);
        restart.duplicateButtonSize();
        restart.setViewPoint(camara.position.x-restart.getWidth()/4,camara.position.y);

        end = new Botao("utility/restartbutton.png",camara.position.y-HEIGHT/4);
        end.duplicateButtonSize();
        end.setViewPoint(camara.position.x-restart.getWidth()/4,camara.position.y-HEIGHT/8); //perfecto!

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("dank.fnt"));
        float highscore = 0;

        if(Gdx.files.isLocalStorageAvailable()){
            fs = Gdx.files.local("ficheiros/highscores.txt");
            try{
                highscore = Float.parseFloat(fs.readString());
                System.out.println(highscore);
                if(this.estadoJogo.getScore() > highscore)
                    fs.writeString("" + this.estadoJogo.getScore(),false);
                else newHighscore = false;
            }catch(GdxRuntimeException e){
                fs.writeString("" + this.estadoJogo.getScore(),false);
            }
        }
        else System.out.println("No local Memory available!");
        scores = "HighScore = " + (int) highscore +  "\nYour Score: " + (int) this.estadoJogo.getScore();

    }

	/**
     * Processa o input do utilizador.
     * Se houve um click no botao de recomecar, usa o EstadosManager atual para remover os dois estados masis recentes da pilha e adicionar um EstadoMenu
     * Se houve um click no botao de terminar, chama restartGame de EstadoJogo e usa o EstadosManager atual para remover o estado atual
     */
    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched() && restart.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            emg.remEstadoAct();
            emg.addEstado(new EstadoMenu(emg));
        }
        else if(Gdx.input.justTouched() && end.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            estadoJogo.restartGame();
            emg.remEstadoAct();
        }
    }

	 /**
     * Chama handleInput
     * @param dt intervalo de tempo entre updates
     */
    @Override
    public void update(float dt) {
        handleInput();
    }
	
	 /**
     * Desenha o estado. Isto é, o fundo e os botoes
     * @param spriteB
     */
    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.begin();
        //todo fazer print da score e da highscore!
        spriteB.draw(estadoJogo.wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(restart.getButton(),restart.getCoordView().x,restart.getCoordView().y,restart.getWidth()/2,restart.getHeight()/2);
        spriteB.draw(end.getButton(),end.getCoordView().x,end.getCoordView().y,end.getWidth()/2,end.getHeight()/2);
        spriteB.end();

        batch.begin();
        font.getData().setScale(Gdx.graphics.getWidth()/1280f ,Gdx.graphics.getHeight()/720f);
        font.draw(batch,scores,11*Gdx.graphics.getWidth()/20,Gdx.graphics.getWidth()/2);
        batch.end();
    }

	/**
     * Descarta memoria (botoes)
     */
    @Override
    public void freeMemory() {
        restart.disposeButton();
        end.disposeButton();
        batch.dispose();
        font.dispose();
    }
}
