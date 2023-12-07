package hr.dulic.pokerapp.utils.networkUtils;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatRemoteService extends Remote {
    String REMOTE_CHAT_OBJECT_NAME = "rmi.chat.service";
    void sendChatMessage(String chatMessage) throws RemoteException;
    List<String> getAllChatMessages() throws RemoteException;
}
