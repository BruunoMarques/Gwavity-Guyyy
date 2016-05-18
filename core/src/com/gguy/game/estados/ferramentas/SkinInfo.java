package com.gguy.game.estados.ferramentas;

/**
 * Created by Jonas on 14-05-2016.
 */
public class SkinInfo {
    private String name;
    private int runningFrames;
    private int jumpingFrames;

    public String getName() {
        return name;
    }

    public int getRunningFrames() {
        return runningFrames;
    }

    public int getJumpingFrames() {
        return jumpingFrames;
    }

    public SkinInfo(String name, int runningFrames, int jumpingFrames){
        this.name = name;
        this.runningFrames = runningFrames;
        this.jumpingFrames = jumpingFrames;
    }


}
