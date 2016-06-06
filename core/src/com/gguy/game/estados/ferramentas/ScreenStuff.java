package com.gguy.game.estados.ferramentas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

/**
 * Created by Jonas on 06-06-2016.
 */
public class ScreenStuff extends Connection implements RMIScreen {
    public SpriteBatch sp;
    public String bananas;
    public ScreenStuff(){
        sp = new SpriteBatch();
        bananas = "bananas";
        new ObjectSpace(this).register(42,this);
    }

    @Override
    public void addSprite(SpriteBatch sp) {
        this.sp = sp;
    }
}
