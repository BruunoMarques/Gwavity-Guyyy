package com.gguy.game.estados.ferramentas;

import java.util.ArrayList;

/**
 * Created by Jonas on 14-05-2016.
 */
public class SkinList {
    ArrayList<SkinInfo> skins;
    SkinInfo currentlySelected;

    public SkinList(){
        skins = new ArrayList<SkinInfo>();
        skins.add(new SkinInfo("sonic",8,4));
        skins.add(new SkinInfo("pikachu",4,4));
        skins.add(new SkinInfo("datboi",5,4));
        currentlySelected = skins.get(2);
    }

    public SkinInfo getSelectedSkin(){
        return currentlySelected;
    }

    public ArrayList<SkinInfo> getSkins(){
        return skins;
    }

    public void setSkin(SkinInfo skin){
        currentlySelected = skin;
    }
}
