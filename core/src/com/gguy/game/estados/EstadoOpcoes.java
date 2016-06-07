package com.gguy.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
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
 * Classe responsavel pelo menu de opcoes.
 */
public class EstadoOpcoes extends EstadoBase {

    private Botao btn1;
    //private Slider slider;
    private final String nomeWallpaper = "background/wallpaper.png";
    private final String MainMenu = "utility/mainmenu.png";
    private final String SliderBackground = "utility/slider_background.png";
    private final String SoundIcon = "utility/sound.png";
    private Texture sliderB;
    private MySlider slider;
    private MySlider slider2;
    private Texture skinSelected;
    private Texture sound;
    private final static String TAG = "infoMessage";
    private float sliderB_x;
    private float sliderB_y;
	
	 /**
     * Construtor de EstadoOpcoes.
     * Inicializa o fundo, a musica, o botao e os sliders
     * @param emg EstadosManager a que o EstadoJogo esta associado
     * @param music musica a reproduzir no menu
     */
    public EstadoOpcoes(EstadosManager emg, Music music) {
        super(emg);
        wallpapper = new Texture(nomeWallpaper);
        btn1 = new Botao(MainMenu,WIDTH/16,HEIGHT/2-HEIGHT/16);
        this.music = music;

        slider = new MySlider(99,WIDTH/2+WIDTH/5,HEIGHT/2-HEIGHT/4);
        sound = new Texture(SoundIcon);
        slider2 = new MySlider(emg.skins.getSkins().size(),WIDTH/2+WIDTH/5,HEIGHT/2+HEIGHT/10);
        sliderB = new Texture(SliderBackground);
        skinSelected = new Texture(emg.skinSelected.getName()+"/"+emg.skinSelected.getName()+".png");

        sliderB_x = slider2.getWidth()+slider2.getWidth()*2/3;
        sliderB_y = slider2.getHeight() + slider2.getHeight()/8;
    }

	/**
     * Processa o input do utilizador.
     * Se houve um click no botao, usa o EstadosManager atual para remover o estado corrente da pilha e adicionar um EstadoMenu
     * Se acordo com a posicao selecionada nos sliders, atualiza a aparencia do jogador e o volume no EstadosManager atual
     */
    @Override
    protected void handleInput() {
        Logger banana = new Logger(TAG,Logger.INFO); // works
        if(Gdx.input.justTouched() && btn1.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            emg.remEstadoAct();
            banana.info("Finito Menu Opcoes");
        }
        else if(Gdx.input.isTouched() && slider2.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            slider2.arrastaKnob(Gdx.input.getX());
            int option = slider2.getOptionSelected(Gdx.input.getX());
            if(!emg.skinSelected.getName().equals(emg.skins.getSkins().get(option).getName()))
            {
                emg.skinSelected = emg.skins.getSkins().get(option);
                skinSelected.dispose();
                skinSelected = new Texture(emg.skinSelected.getName()+"/"+emg.skinSelected.getName()+".png");
            }
        }

        else if(Gdx.input.isTouched() && slider.checkClick(Gdx.input.getX(),Gdx.input.getY())){
            slider.arrastaKnob(Gdx.input.getX());
            float option = slider.getOptionSelected(Gdx.input.getX());
            if(option == 0 && slider.getCoordKnob().x > slider.getCoords().x) option = 100;
            emg.soundVolume = option/100;
            music.setVolume(this.emg.soundVolume);
        }

    }

	/**
     * Atualiza o estado. Isto é, chama uma funcao para processar o input do utilizador
     * @param dt - tempo entre atualizacoes
     */
    @Override
    public void update(float dt) {
        handleInput();
    }

	/**
     * Desenha o estado de jogo no ecra.
     * Isto inclui o fundo, o botao, e os sliders
     * @param spriteB
     */
    @Override
    public void render(SpriteBatch spriteB) {
   //     Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteB.begin();
        spriteB.draw(wallpapper,0,0,WIDTH,HEIGHT);
        spriteB.draw(btn1.getButton(),btn1.getCoord().x,btn1.getCoord().y,btn1.getWidth(),btn1.getHeight());

        spriteB.draw(sliderB,slider.getCoords().x-slider.getWidth2()/2,slider.getCoords().y,sliderB_x,sliderB_y);
        spriteB.draw(slider.getKnob(),slider.getCoordKnob().x,slider.getCoordKnob().y,slider.getWidth2(),slider.getHeight());
        spriteB.draw(slider.getLine(),slider.getCoords().x,slider.getCoords().y,slider.getWidth(),slider.getHeight2());
        spriteB.draw(sound,slider.getCoords().x+slider.getWidth()/10+slider.getWidth(),slider.getCoords().y+slider.getHeight2()/2,sound.getWidth()*W_RES,sound.getHeight()*H_RES);

        spriteB.draw(sliderB,slider2.getCoords().x-slider2.getWidth2()/2,slider2.getCoords().y,sliderB_x,sliderB_y);
        spriteB.draw(slider2.getKnob(),slider2.getCoordKnob().x,slider2.getCoordKnob().y,slider2.getWidth2(),slider2.getHeight());
        spriteB.draw(slider2.getLine(),slider2.getCoords().x,slider2.getCoords().y,slider2.getWidth(),slider2.getHeight2());
        spriteB.draw(skinSelected,slider2.getCoords().x+slider2.getWidth()/10+slider2.getWidth(),slider2.getCoords().y+slider2.getHeight2()/2,skinSelected.getWidth()*W_RES,skinSelected.getHeight()*H_RES);
        spriteB.end();
    }

	/**
     * Descarta memoria (fundo, jogador, botoes, musica, o gerador de mapa)
     */
    @Override
    public void freeMemory() {
        wallpapper.dispose();
        btn1.disposeButton();
        slider2.disposeSlider();
        sliderB.dispose();
        skinSelected.dispose();
    }

}