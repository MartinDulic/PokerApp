package hr.dulic.pokerapp;

import hr.dulic.pokerapp.controllers.ClientController;
import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.enums.NetworkMessageType;
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
import java.util.Objects;

public class HelloApplication extends Application {
    public static Role role;
    FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
        if (role == Role.SERVER){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
            new Thread(this::acceptRequestAsServer).start();
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("client-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1900, 1060);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
            new Thread(this::acceptRequestAsClient).start();
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

        new Thread(Application::launch).start();
    }

    private void acceptRequestAsServer() {

        try (ServerSocket serverSocket = new ServerSocket(NetworkConfiguration.SERVER_PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort());
                new Thread(() -> processSerializableServer(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptRequestAsClient() {

        boolean continiue = true;
        int index = 0;

        while (continiue){

            NetworkConfiguration.CLIENT_PORT = NetworkConfiguration.CLIENT_PORTS[index];

            try (ServerSocket serverSocket = new ServerSocket(NetworkConfiguration.CLIENT_PORT)) {
                System.err.println("Server listening on port: " + serverSocket.getLocalPort());
                continiue = false;
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.err.println("Client connected from port: " + clientSocket.getPort());
                    new Thread(() -> processSerializableClient(clientSocket)).start();
                }

            } catch (IOException e) {
                index++;
                e.printStackTrace();
            }
        }

    }

    private void processSerializableServer(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            NetworkMessageType objectType = (NetworkMessageType) ois.readObject();
            System.out.println("Type: " + objectType + " received!");
            oos.writeObject("NetworkMessageType recieved. Type: " + objectType);
            if (objectType == NetworkMessageType.GAME_STATE){
                GameState gameState = (GameState) ois.readObject();
                ServerController serverController = fxmlLoader.getController();
                Platform.runLater(() -> serverController.startNewTurn(gameState));
                System.out.println("Game state received!");
                oos.writeObject("Game state received confirmation!");
            } else if (objectType == NetworkMessageType.CLIENT_INSTANCE) {
                ClientInstance clientInstance = (ClientInstance) ois.readObject();
                ServerController serverController = fxmlLoader.getController();
                Platform.runLater(() -> serverController.addClientInstance(clientInstance));
                System.out.println("Client Instance received!");
                oos.writeObject("Client Instance received confirmation!");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            GameState gameState = (GameState) ois.readObject();
            ClientController clientController = fxmlLoader.getController();
            Platform.runLater(() -> clientController.setGameState(gameState));
            System.out.println("Game state received!");
            oos.writeObject("Game state received confirmation!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}