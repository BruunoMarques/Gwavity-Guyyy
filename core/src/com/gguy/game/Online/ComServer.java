
package com.gguy.game.Online;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_EXCLUSIONPeer;

/**
 * Created by Utilizador on 31-05-2016.
 */
public class ComServer {

    public static void mainServer() {

        try{
            Registry reg = LocateRegistry.createRegistry(1099);
            Comunicar c = new ComunicarImp();
            reg.rebind("com", c);
        }
        catch (Exception e){
            System.out.println("Trouble: " + e);
        }
    }
}
