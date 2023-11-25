package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.utils.gameUtils.*;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.List;


public class ServerController {
    Timeline timeline;
    GameState gameState;
    public static List<ClientInstance> clientInstances;
    public void initialize() {
        gameState = new GameState();
    }

    private void startGame() {

        TurnUtils.setGameStateStart(gameState, clientInstances);
        clientInstances = new ArrayList<>();

    }

    public void addClientInstance(ClientInstance clientInstance) {
        clientInstances.add(clientInstance);
        System.out.println("Server: Client instance added!");
        if (clientInstances.size() == GameRules.numberOfPlayers) {
            startGame();
        }
    }

    public void startNewTurn(GameState gameState) {

        TurnUtils turnUtils = new TurnUtils();
        this.gameState = gameState;
        turnUtils.startPlayerTurn(gameState, timeline);

    }
}
