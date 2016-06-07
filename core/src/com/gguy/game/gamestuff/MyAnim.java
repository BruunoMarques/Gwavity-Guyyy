package com.gguy.game.gamestuff;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Classe para carregar e animar texturas
 */
public class MyAnim { // pq nao percebi um corno da classe animacao
    private Array<TextureRegion> frames;
    private Array<Texture> simpleFrames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int nFrames;
    private int currentFrame;

	/**
     * Construtor de MyAnim para animacoes que requerem frames
     * @param tr textura, divida em regioes que servirao de frames da animacao
     * @param nFrames numero de frames em que a textura esta dividida
     * @param time tempo que demorara a animacao das frames
     */
    public MyAnim(TextureRegion tr, int nFrames, float time){
        frames = new Array<TextureRegion>();
        this.nFrames = nFrames;
        int tamanhoText = tr.getRegionWidth() / nFrames; //obriga a que tenham espacos iguais
        for(int i = 0;i < nFrames; i++){
            frames.add(new TextureRegion(tr, i * tamanhoText, 0, tamanhoText, tr.getRegionHeight()));
        }
        currentFrame = 0; // maybe change to smthing else to alow more accessibility
        maxFrameTime = time;
        currentFrameTime = 0;
    }

	/**
     * Atualiza a frame atual. Se a frame atual for a ultima da textura, continua a animacao a partir do inicio da textura
     * @param dt tempo entre atualizacoes
     */
    public void update(float dt){
        currentFrame += dt;
        if(currentFrameTime > maxFrameTime){
            currentFrame = 0;
            currentFrame ++;
        }
        if(currentFrame >= nFrames)
            currentFrame = 0;

    }

	
    public TextureRegion getFrame(){
        return frames.get(currentFrame);
    }

    public Animation getSimpleAnimation(){
        return new Animation(maxFrameTime,frames);
    }


}
