package hr.dulic.pokerapp;

import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.NetworkConfiguration;
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

public class HelloApplication extends Application {
    private static Role role;
    FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
        if (role == Role.SERVER){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
            new Thread(this::acceptRequestAsServer);
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1900, 1060);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
            //acceptRequestAsClient();

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
                new Thread(() -> processSerializableServer(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processSerializableServer(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            GameState gameState = (GameState) ois.readObject();
            ServerController serverController = fxmlLoader.getController();
            Platform.runLater(() -> serverController.setGameState(gameState));
            System.out.println("Game state received!");
            oos.writeObject("Game state received confirmation!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {
            GameState gameState = (GameState) ois.readObject();
            //Platform.runLater(() -> HelloController.updateGameBoard(gameState));
            System.out.println("Game state received!");
            oos.writeObject("Game state received confirmation!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}