package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    public static List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        for (ClientInstance clientInstance: ServerController.clientInstances) {
            players.add(clientInstance.getPlayer());
        }
        return players;
    }
}
