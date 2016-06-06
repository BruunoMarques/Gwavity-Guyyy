package com.gguy.game.estados.ferramentas;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.gguy.game.gamestuff.Guy;
import com.gguy.game.gamestuff.MyAnim;
import com.gguy.game.gamestuff.obstaculos.MapStruct;

/**
 * Created by Jonas on 05-06-2016.
 */
public class Network {
    static public final int port = 54555;
    static public void register (EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        kryo.register(MapStruct.class);
        kryo.register(Vector2.class);
        kryo.register(RMIScreen.class);
        kryo.register(Vector3.class);
        kryo.register(Guy.class);
        kryo.register(Rectangle.class);
        kryo.register(Sound.class);
        kryo.register(Animation.class);
        kryo.register(MyAnim.class);
        kryo.register(OrthographicCamera.class);
        kryo.register(Texture.class);
       // kryo.register(SpriteBatch.class);
    }
}
