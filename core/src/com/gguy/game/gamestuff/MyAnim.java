package com.gguy.game.gamestuff;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Jonas on 02-05-2016.
 */
public class MyAnim { // pq nao percebi um corno da classe animacao
    private Array<TextureRegion> frames;
    private Array<Texture> simpleFrames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int nFrames;
    private int currentFrame;

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
/*nao e utilizado
    public MyAnim(Array<Texture> textures, float time){
        simpleFrames = textures;
        nFrames = simpleFrames.size;
        currentFrame =  0;
    }


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
