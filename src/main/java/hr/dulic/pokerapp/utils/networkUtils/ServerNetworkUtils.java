package hr.dulic.pokerapp.utils.networkUtils;

import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.utils.ByteArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerNetworkUtils {

    public static void receivePlayersAsServer(GameState gameState) {

        try(DatagramSocket serverSocket = new DatagramSocket(NetworkConfiguration.SERVER_PORT)) {
            System.err.printf("Server listening on port:%s%n", serverSocket.getLocalPort());

            while (gameState.getPlayers().size() != GameRules.numberOfPlayers) {
                byte[] buffer = new byte[12400];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                //Blocking
                serverSocket.receive(packet);
                Player player = ByteArrayUtils.deserializeObject(packet.getData(), Player.class);
                System.out.println("ServerNetworkUtils: Player " + player.getUsername() + " recieved");
                gameState.addPlayer(player);
            }

            System.err.println("Players connected; starting game!");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState receiveGameStateAsServer(GameState gameState) {
        GameState receivedObject = new GameState();
        try (DatagramSocket serverSocket = new DatagramSocket(NetworkConfiguration.SERVER_PORT)) {
            System.err.printf("Server listening on port:%s%n", serverSocket.getLocalPort());

            byte[] buffer = new byte[12400];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Blocking
            serverSocket.receive(packet);
            receivedObject = ByteArrayUtils.deserializeObject(packet.getData(), GameState.class);
            System.out.println("ServerNetworkUtils: game state: " + gameState + " received");

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedObject;
    }

    public static <T extends Serializable> void multicastObjectAsServer(T object) {
        try(DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.printf("Server multicasting on port: %d%n", serverSocket.getLocalPort());

            byte[] buffer = ByteArrayUtils.serializeObject(object);
            InetAddress groupAddress = InetAddress.getByName(NetworkConfiguration.GROUP);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, groupAddress, NetworkConfiguration.CLIENT_PORT);
            serverSocket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
