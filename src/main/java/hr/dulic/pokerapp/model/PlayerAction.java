package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.PlayerActionType;

import java.io.Serializable;


public class PlayerAction implements Serializable {

    private final PlayerActionType type;
    private final Double value;

    public PlayerAction(PlayerActionType type, double value) {
        this.type = type;
        this.value = value;
    }

    public PlayerActionType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
