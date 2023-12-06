package hr.dulic.pokerapp;

import hr.dulic.pokerapp.controllers.ClientController;
import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.ConfigurationKey;
import hr.dulic.pokerapp.model.ConfigurationReader;
import hr.dulic.pokerapp.model.enums.NetworkMessageType;
import hr.dulic.pokerapp.utils.ChatUtils;
import hr.dulic.pokerapp.utils.networkUtils.ChatRemoteService;
import hr.dulic.pokerapp.utils.networkUtils.ChatRemoteServiceImpl;
import hr.dulic.pokerapp.utils.networkUtils.NetworkConfiguration;
import hr.dulic.pokerapp.model.enums.Role;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

public class HelloApplication extends Application {
    public static Role role;
    FXMLLoader fxmlLoader;
    public static ChatRemoteService chatRemoteService;
    @Override
    public void start(Stage stage) throws IOException {
        if (role == Role.SERVER){
            startChatService();
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 200, 200);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
        } else {
            startChatClient();
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-dialog.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 300);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        String roleString = args[0];
        role = Role.CLIENT;
        for (Role r : Role.values()){
            if (r.name().equals(roleString)){
                role = r;
                break;
            }
        }

        launch();
    }

    private static void startChatClient() {
        try {
            Registry registry = LocateRegistry.getRegistry(
                    NetworkConfiguration.HOST,
                    NetworkConfiguration.RMI_PORT);
            chatRemoteService = (ChatRemoteService) registry.lookup(ChatRemoteService.REMOTE_CHAT_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startChatService() {
        try {
            Registry registry = LocateRegistry.createRegistry(
                    NetworkConfiguration.RMI_PORT);
            chatRemoteService = new ChatRemoteServiceImpl();
            ChatRemoteService skeleton = (ChatRemoteService) UnicastRemoteObject.exportObject(chatRemoteService,
                    NetworkConfiguration.RANDOM_PORT_HINT);
            registry.rebind(ChatRemoteService.REMOTE_CHAT_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (
                RemoteException e) {
            e.printStackTrace();
        }
    }


}