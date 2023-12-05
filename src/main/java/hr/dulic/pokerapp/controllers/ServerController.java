package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.PlayerActionType;
import hr.dulic.pokerapp.utils.gameUtils.*;
import hr.dulic.pokerapp.utils.networkUtils.ServerNetworkUtils;
import hr.dulic.pokerapp.utils.viewUtils.ThreadUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ServerController extends Thread{

    @FXML
    Label lblServerCounter;
    @FXML
    Label lblPlayer;
    Timeline timeline;
    GameState gameState;
    public void initialize() {
        //Runs run() on a new thread
        start();
    }

    @Override
    public void run() {
        gameState = new GameState();
        gameState.setPlayers(new ArrayList<>());
        //Receive  all players
        ServerNetworkUtils.receivePlayersAsServer(gameState);

        TurnUtils.setGameStateStart(gameState);
        startPlayerTurn();

    }
    public void startPlayerTurn() {
        if (gameState.getTurnsToNextFaze() < 1) {
            TurnUtils.setGameStateNext(gameState, timeline);
        }

        Platform.runLater(() -> lblPlayer.setText(gameState.getActivePlayer().getUsername()));
        AtomicInteger secondsLeft = new AtomicInteger(GameRules.turnTime + 2);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    gameState.setTurnTime(secondsLeft.getAndDecrement());
                    Platform.runLater(() -> lblServerCounter.setText(secondsLeft.toString()));
                }));

        timeline.setCycleCount(GameRules.turnTime + 1);
        Timeline timeline1 = timeline;
        timeline.setOnFinished(e -> doFold(gameState, timeline1));
        timeline.play();


        //send state and await response
        ServerNetworkUtils.multicastObjectAsServer(gameState);
        //blocking
        gameState = ServerNetworkUtils.receiveGameStateAsServer(gameState);
        //if it is not the games end doPlayerAction
        if(!restartGameIfEnd()){
            doPlayerAction();
        }
    }

    private boolean restartGameIfEnd() {
        if(gameState.getWinner() != null) {

            gameState.getWinner().setBalance(gameState.getWinner().getBalance() + gameState.getPot());

            System.out.println("GAME RESTARTING...");
            ThreadUtils.pauseThread(1000);
            System.out.println("3");
            ThreadUtils.pauseThread(1000);
            System.out.println("2");
            ThreadUtils.pauseThread(1000);
            System.out.println("1");
            ThreadUtils.pauseThread(1000);

            ServerNetworkUtils.multicastObjectAsServer(gameState);
            //start();
            return true;
        }
        return false;
    }



    private void doPlayerAction() {
        if (gameState.getActivePlayer().getAction().getType() == PlayerActionType.FOLD) {
            doFold(gameState, timeline);
        } else if (gameState.getActivePlayer().getAction().getType() == PlayerActionType.CHECK) {
            doCheck(gameState, timeline);
        } else if (gameState.getActivePlayer().getAction().getType() == PlayerActionType.CALL) {
            doCall(gameState, timeline);
        } else if (gameState.getActivePlayer().getAction().getType() == PlayerActionType.RAISE) {
            doRaise(gameState.getActivePlayer().getAction().getValue(),gameState, timeline);
        } else if (gameState.getActivePlayer().getAction().getType() == PlayerActionType.BET) {
            doBet(gameState.getActivePlayer().getAction().getValue(),gameState, timeline);
        }
    }

    public void endPlayerTurn(GameState gameState, Timeline timeline) {
        timeline.stop();
        List<Player> remainingPlayers = gameState.getRemainingPlayers();

        int nextIndex = remainingPlayers.indexOf(gameState.getActivePlayer()) + 1;

        if (nextIndex == remainingPlayers.size()) {
            nextIndex = 0;
        }

        gameState.setActivePlayer(gameState.getRemainingPlayers().get(nextIndex));

        gameState.setTurnsToNextFaze(gameState.getTurnsToNextFaze() - 1);
        //Continue game if not end
        if(!restartGameIfEnd()) {
            startPlayerTurn();
        }
    }


    public  void doFold(GameState gameState, Timeline timeline) {

        this.gameState = gameState;
        gameState.setPot(gameState.getPot() + gameState.getActivePlayer().getRoundBet());
        gameState.getActivePlayer().setRoundBet(0);

        //get active player from players and set balance of that player to a value of a working copy from remaining players
        gameState.getPlayers().get(gameState.getPlayers().indexOf(gameState.getActivePlayer())).setBalance(gameState.getActivePlayer().getBalance());


        System.out.println("PLAYER: " + gameState.getActivePlayer().getUsername() + "IS BEING REMOVED!_!_!_!_!_!_!_!_!_!");
        gameState.getRemainingPlayers().remove(gameState.getActivePlayer());

        if (gameState.getRemainingPlayers().size() == 1) {
            WinnerUtils winnerUtils = new WinnerUtils();
            gameState.setWinner(winnerUtils.determineWinner(gameState, timeline));
            System.out.println("WINNER: " + gameState.getWinner().getUsername());
        }

        endPlayerTurn(gameState, timeline);
    }

    public void doCall(GameState gameState, Timeline timeline){

        double balance=gameState.getActivePlayer().getBalance();
        double currentRoundBet=gameState.getActivePlayer().getRoundBet();
        double callAmount= gameState.getRunningSum() - currentRoundBet;

        gameState.getActivePlayer().setRoundBet(currentRoundBet + callAmount);
        gameState.getActivePlayer().setBalance(balance - callAmount);

        endPlayerTurn(gameState, timeline);
    }

    public void doBet(double betAmount, GameState gameState, Timeline timeline) {

        double balance = gameState.getActivePlayer().getBalance();
        gameState.getActivePlayer().setBalance(balance-betAmount);
        double roundBet = gameState.getActivePlayer().getRoundBet();
        gameState.getActivePlayer().setRoundBet(roundBet+betAmount);

        if (gameState.getActivePlayer().getRoundBet() > gameState.getRunningSum()){

            gameState.setRunningSum(gameState.getActivePlayer().getRoundBet());

        }

        gameState.setTurnsToNextFaze(gameState.getRemainingPlayers().size());
        endPlayerTurn(gameState, timeline);
    }

    public void doRaise(double raiseAmount, GameState gameState, Timeline timeline){

        double balance = gameState.getActivePlayer().getBalance();
        gameState.getActivePlayer().setBalance(balance-raiseAmount);
        double roundBet = gameState.getActivePlayer().getRoundBet();
        gameState.getActivePlayer().setRoundBet(roundBet+raiseAmount);

        if (gameState.getActivePlayer().getRoundBet() > gameState.getRunningSum()){

            gameState.setRunningSum(gameState.getActivePlayer().getRoundBet());

        }

        gameState.setTurnsToNextFaze(gameState.getRemainingPlayers().size());
        endPlayerTurn(gameState, timeline);

    }
    public void doCheck(GameState gameState, Timeline timeline){

        endPlayerTurn(gameState, timeline);
    }


}
