package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.Muro;

/**
 * Created by Jonas on 01-05-2016.
 */
public class EstadoJogo extends EstadoBase {
    private Guy gguy;
    private Texture fundo; //todo isto é temp

    private Array<Muro> obstaculos;

    protected EstadoJogo(EstadosManager emg) {
        super(emg);
        fundo = new Texture("fundo1.png");
        gguy = new Guy(50,50);
        camara.setToOrtho(false, WIDTH*8/10, HEIGHT*8/10);
        obstaculos = new Array<Muro>();

        for(int i = 0;i< 10;i++){
            obstaculos.add(new Muro(i* (125 + 50)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
           gguy.fly();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        gguy.updatePos(dt);

        camara.position.x = gguy.getPosicao().x;

        for(Muro obstaculo : obstaculos){
            if(camara.position.x - (camara.viewportWidth*8/10) > obstaculo.getObsCima().x + obstaculo.getTemp().getWidth())
                obstaculo.reposition(obstaculo.getObsCima().x + ((125 + 50) * 10));
        }

        camara.update();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.setProjectionMatrix(camara.combined);
        spriteB.begin();
        spriteB.draw(fundo,camara.position.x - (camara.viewportWidth*8/10),0);
        spriteB.draw(gguy.getSkin(),gguy.getPosicao().x,gguy.getPosicao().y,50,50);
        for(Muro obstaculo : obstaculos){
            spriteB.draw(obstaculo.getTemp(),obstaculo.getObsCima().x,obstaculo.getObsCima().y);
            spriteB.draw(obstaculo.getTemp(),obstaculo.getObsBaixo().x,obstaculo.getObsBaixo().y);
        }
        // spriteB.draw(obstaculo.getTemp(),obstaculo.getObsCima().x,obstaculo.getObsCima().y);
       // spriteB.draw(obstaculo.getTemp(),obstaculo.getObsBaixo().x,obstaculo.getObsBaixo().y);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        fundo.dispose();
        gguy.freeMemory();
        for(Muro muro : obstaculos){
            muro.freeMemory();
        }
    }
}
