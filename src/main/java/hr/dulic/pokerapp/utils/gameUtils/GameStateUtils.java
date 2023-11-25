package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.HelloApplication;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.enums.Role;
import hr.dulic.pokerapp.utils.networkUtils.NetworkUtils;

public class GameStateUtils {

    public static void sendGameState(GameState gameState, ClientInstance clientInstance){

        if (HelloApplication.role == Role.CLIENT){
            NetworkUtils.sendDataToServer(gameState);
        } else {
            NetworkUtils.sendDataToClient(gameState, clientInstance);
        }
    }
    public static void sendClientInstanceState(ClientInstance instance){

        if (HelloApplication.role == Role.CLIENT){
            NetworkUtils.sendDataToServer(instance);
        }
    }


}
