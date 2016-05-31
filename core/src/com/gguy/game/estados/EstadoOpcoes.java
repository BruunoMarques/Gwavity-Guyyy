package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Logger;
import com.gguy.game.estados.ferramentas.Botao;
import com.gguy.game.estados.ferramentas.MySlider;

/**
 * Created by Jonas on 14-05-2016.
 */
public class EstadoOpcoes extends EstadoBase {

    private Botao btn1;
    private Slider slider;
    private MySlider slider2;
    private Texture skinSelected;
    private final static String TAG = "infoMessage";

    public EstadoOpcoes(EstadosManager emg) {
        super(emg);
        wallpapper = new Texture("background/wallpaper1.png"); //todo fazer defines disto para ficar bonito
        btn1 = new Botao("background/options.png",200,200);
        Skin bananas = new Skin();
        bananas.add("knob", new Texture("map/obst.png"));
        bananas.add("bgs", new Texture("map/walkplat.png"));

        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = bananas.getDrawable("bgs");
        style.knob = bananas.getDrawable("knob");

        slider = new Slider(0,2,100,false,style);
        slider.setVisible(true);

        slider2 = new MySlider(3,700,300);
        skinSelected = new Texture(emg.skinSelected.getName()+"/"+emg.skinSelected.getName()+".png");
    }

    @Override
    protected void handleInput() {
        Logger banana = new Logger(TAG,Logger.INFO); // works
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            banana.info("Finito Menu Opcoes");
        }
        else if(Gdx.input.isTouched() && slider2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            banana.info("Selecionou");
            slider2.arrastaKnob(Gdx.input.getX());
            int option = slider2.getOptionSelected(Gdx.input.getX());
            if(!emg.skinSelected.getName().equals(emg.skins.getSkins().get(option).getName()))
            {
                emg.skinSelected = emg.skins.getSkins().get(option);
                skinSelected.dispose();
                skinSelected = new Texture(emg.skinSelected.getName()+"/"+emg.skinSelected.getName()+".png");
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteB) {
        spriteB.begin();
        spriteB.draw(wallpapper,0,0,WIDTH,HEIGHT);
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y);
        spriteB.draw(slider2.getKnob(),slider2.getCoordKnob().x,slider2.getCoordKnob().y);
        spriteB.draw(slider2.getLine(),slider2.getCoords().x,slider2.getCoords().y);
        spriteB.draw(skinSelected,900,300);
        //slider.draw(spriteB,1);
        spriteB.end();
    }

    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
        slider2.disposeSlider();
        skinSelected.dispose();
    }

}