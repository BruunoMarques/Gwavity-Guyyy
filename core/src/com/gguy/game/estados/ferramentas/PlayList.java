package com.gguy.game.estados.ferramentas;




import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.naming.Context;

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

        /* //todo maybe later
        FileHandle fh = new FileHandle("music"); //doesnt work in android because android's gay
        FileHandle[] folder = fh.list();
        for(FileHandle file : folder){
            if(!file.isDirectory())
                if(!file.name().equals("kendrick.mp3") && file.name().contains(".mp3"))
                    playlist.add(file.name());
        }
        */
        playlist.add("kong.mp3");
        playlist.add("resonance.mp3");
        playlist.add("walk.mp3");
        playlist.add("whistle.mp3");
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
