package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    public static List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();

        players.add(new Player("player1", 1000.0));
        players.add(new Player("player2", 2000.0));
        players.add(new Player("player3", 3000.0));
        players.add(new Player("player4", 4000.0));
        players.add(new Player("player5", 5000.0));
        players.add(new Player("player6", 6000.0));

        return players;
    }
}
