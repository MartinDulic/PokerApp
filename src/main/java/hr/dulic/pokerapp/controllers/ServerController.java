package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.utils.gameUtils.*;
import javafx.animation.Timeline;


public class ServerController {
    Timeline timeline;
    GameState gameState;
    TurnUtils turnUtils;
    public void initialize() {
        turnUtils = new TurnUtils();
        gameState = new GameState();
        startServer();
    }

    private void startServer() {

        TurnUtils.setGameStateStart(gameState);
        turnUtils.startPlayerTurn(gameState, timeline);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
