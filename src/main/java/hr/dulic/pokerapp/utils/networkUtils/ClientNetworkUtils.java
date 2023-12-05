package hr.dulic.pokerapp.utils.networkUtils;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.utils.ByteArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.*;

public class ClientNetworkUtils {

    public static <T extends Serializable> void sendObjectAsClient(T object){
        try (DatagramSocket clientSocket = new DatagramSocket()){
            byte[] buffer = ByteArrayUtils.serializeObject(object);
            InetAddress serverAddress = InetAddress.getByName(NetworkConfiguration.HOST);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, NetworkConfiguration.SEREVR_PORT);
            clientSocket.send(packet);

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static GameState receiveGameStateAsClient(String name) {
        GameState gameState = new GameState();
        try (MulticastSocket clientSocket = new MulticastSocket(NetworkConfiguration.CLIENT_PORT)) {

            InetAddress group = InetAddress.getByName(NetworkConfiguration.GROUP);
            InetSocketAddress groupAddress = new InetSocketAddress(group, NetworkConfiguration.CLIENT_PORT);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(NetworkConfiguration.HOST));
            System.err.printf("%s joining group%n", name);

            clientSocket.joinGroup(groupAddress, networkInterface);
            System.err.printf("%s listening...%n", name);

            byte[] buffer = new byte[12400];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(packet);
            gameState = ByteArrayUtils.deserializeObject(packet.getData(), GameState.class);
            System.out.printf("%s received game state: %s%n", name, gameState);

            System.err.printf("%s leaving group%n", name);
            clientSocket.leaveGroup(groupAddress, networkInterface);


        } catch (Exception e) {
            e.printStackTrace();

        }
        return gameState;

    }




}
