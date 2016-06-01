package com.gguy.game.Online;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Utilizador on 31-05-2016.
 */
public class ComClient {

    public static void mainClient() {
        try {
            Registry myReg =  LocateRegistry.getRegistry("127.0.0.1",1099);
            Comunicar c = (Comunicar)myReg.lookup("com");


            System.out.println(c.partilhaString());

        }
        catch (Exception e){
            System.out.println("Client trouble: " + e);
        }
    }

}
