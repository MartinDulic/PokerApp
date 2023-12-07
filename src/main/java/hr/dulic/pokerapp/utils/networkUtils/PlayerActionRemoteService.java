package hr.dulic.pokerapp.utils.networkUtils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerActionRemoteService extends Remote {
    String REMOTE_OBJECT_NAME = "rmi.playerAction.service";
    void doFold() throws RemoteException;
    void doCheck() throws RemoteException;
    void doCall() throws RemoteException;
    void doBet() throws RemoteException;
    void doRaise() throws RemoteException;

}
