package com.gguy.game.estados.ferramentas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jonas on 14-05-2016.
 */
public class PlayList {
    ArrayList<String> playlist;
    ArrayList<String> removedlist;
    String lastSong;
    public PlayList(){
        playlist = new ArrayList<String>();
        removedlist = new ArrayList<String>();
        lastSong = new String();
        //todo fazer isto automatico mb? Ou ter um ficheiro txt com o nome das musicas
        playlist.add("crazyfrog.mp3");
        playlist.add("dafunk.mp3");
       //playlist.add("kendrick.mp3");
        playlist.add("resonance.mp3");
        playlist.add("kong.mp3");
    }
    void addMusic(String name){
        if(removedlist.contains(name)){
            removedlist.remove(name);
            playlist.add(name);
        }
    }
    void removeMusic(String name){
        if(playlist.contains(name)){
            playlist.remove(name);
            removedlist.add(name);
        }
    }
    public String selectRandomMusic(){
        Random rand = new Random();
        boolean validSong = false;
        int nMusic = 0;
        while(!validSong){
            nMusic = rand.nextInt(playlist.size());
            if(playlist.get(nMusic).equals(lastSong)){/*todo smthing here?*/}
            else validSong = true;
        }
        lastSong = playlist.get(nMusic);
        return lastSong;
    }

    public String currentlyPlaying(){
        return lastSong;
    }
}
