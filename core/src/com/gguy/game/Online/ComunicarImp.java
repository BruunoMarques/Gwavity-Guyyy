package com.gguy.game.Online;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Utilizador on 31-05-2016.
 */
public class ComunicarImp extends UnicastRemoteObject implements Comunicar{

    public ComunicarImp() throws RemoteException{
        super();
    }

    @Override
    public String partilhaString() throws RemoteException{
        return "naoBananas";
    }
}
