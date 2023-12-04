package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.utils.gameUtils.*;
import hr.dulic.pokerapp.utils.networkUtils.ServerNetworkUtils;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;


public class ServerController extends Thread{

    @FXML
    Label lblServerCounter;

    Timeline timeline;
    GameState gameState;
    public void initialize() {
        gameState = new GameState();
        gameState.setPlayers(new ArrayList<>());
        //Runs run() on a new thread
        start();
    }

    @Override
    public void run() {

        ServerNetworkUtils.receivePlayersAsServer(gameState);
        TurnUtils.setGameStateStart(gameState);
        TurnUtils turnUtils = new TurnUtils();
        turnUtils.startPlayerTurn(gameState, timeline, lblServerCounter);
        ServerNetworkUtils.multicastGameStateAsServer(gameState);



    }


}
