package hr.dulic.pokerapp.model;

import java.io.Serializable;

public class ClientInstance implements Serializable {
    Player player;
    Integer port;

    public ClientInstance(Player player, Integer port) {
        this.player = player;
        this.port = port;
    }

    public ClientInstance() {

    }

    public Player getPlayer() {
        return player;
    }

    public Integer getPort() {
        return port;
    }
}
