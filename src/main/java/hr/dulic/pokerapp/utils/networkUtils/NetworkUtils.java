package hr.dulic.pokerapp.utils.networkUtils;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.enums.NetworkMessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtils {

    public static void sendDataToServer(GameState gameState) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST, NetworkConfiguration.SERVER_PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());
            sendSerializableRequest(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendDataToClient(GameState gameState, ClientInstance instance) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST, instance.getPort())){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());
            sendSerializableRequest(clientSocket, gameState);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void sendDataToServer(ClientInstance instance) {
        try (Socket clientSocket = new Socket(NetworkConfiguration.HOST, NetworkConfiguration.SERVER_PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());
            sendSerializableRequest(clientSocket, instance);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequest(Socket client, GameState gameState) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        oos.writeObject(NetworkMessageType.GAME_STATE);
        System.out.println("Type info GAME_STATE sent to server!");
        String confirmationMessage = (String) ois.readObject();
        System.out.println("Confirmation message: " + confirmationMessage);
        oos.writeObject(gameState);
        System.out.println("Game state sent to server!");
        confirmationMessage = (String) ois.readObject();
        System.out.println("Confirmation message: " + confirmationMessage);
    }
    private static void sendSerializableRequest(Socket client, ClientInstance instance) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        oos.writeObject(NetworkMessageType.CLIENT_INSTANCE);
        System.out.println("Type info CLIENT_INSTANCE sent to server!");
        String confirmationMessage = (String) ois.readObject();
        System.out.println("Confirmation message: " + confirmationMessage);
        oos.writeObject(instance);
        System.out.println("Client Instance sent to server!");
        confirmationMessage = (String) ois.readObject();
        System.out.println("Confirmation message: " + confirmationMessage);
    }

}


