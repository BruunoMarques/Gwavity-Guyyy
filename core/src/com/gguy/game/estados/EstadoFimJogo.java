package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gguy.game.estados.ferramentas.Botao;

/**
 * Created by Jonas on 04-06-2016.
 */
public class EstadoFimJogo extends EstadoBase {
    private EstadoJogo estadoJogo;
    private Botao restart;
    private Botao end;
    private FileHandle fs;
    private boolean newHighscore = true;
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

        if(Gdx.files.isLocalStorageAvailable()){
            float highscore = 0;
            fs = Gdx.files.local("ficheiros/highscores.txt"); //todo testar no telemovel!!
            try{
                highscore = Float.parseFloat(fs.readString());
                System.out.println(highscore);
                if(this.estadoJogo.getTimePassed() > highscore)
                    fs.writeString("" + this.estadoJogo.getTimePassed(),false);
                else newHighscore = false;
            }catch(GdxRuntimeException e){
                fs.writeString("" + this.estadoJogo.getTimePassed(),false);
            }
        }
        else System.out.println("No local Memory available!");
    }

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

    @Override
    public void update(float dt) {
        handleInput();
        //todo smthing related with highscores and boobies
    }

    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.begin();
        //todo fazer print da score e da highscore!
        spriteB.draw(estadoJogo.wallpapper,camara.position.x - (camara.viewportWidth/2),camara.position.y - (camara.viewportHeight/2),WIDTH/2, HEIGHT/2);
        spriteB.draw(restart.getButton(),restart.getCoordView().x,restart.getCoordView().y,restart.getWidth()/2,restart.getHeight()/2);
        spriteB.draw(end.getButton(),end.getCoordView().x,end.getCoordView().y,end.getWidth()/2,end.getHeight()/2);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        restart.disposeButton();
    }
}
