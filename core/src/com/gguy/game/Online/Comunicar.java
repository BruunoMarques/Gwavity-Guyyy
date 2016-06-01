package com.gguy.game.Online;

/**
 * Created by Utilizador on 31-05-2016.
 */
import java.rmi.Remote ;
import java.rmi.RemoteException;
public interface Comunicar extends Remote {

    public String partilhaString() throws RemoteException;

}
