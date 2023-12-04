package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.GameFaze;
import hr.dulic.pokerapp.utils.DialogUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnUtils {

    public void startPlayerTurn(GameState gameState, Timeline timeline, Label labelCounter) {
        if (gameState.getTurnsToNextFaze() < 1) {
            setGameStateNext(gameState, timeline);
            return;
        }

        AtomicInteger secondsLeft = new AtomicInteger(GameRules.turnTime + 1);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {gameState.setTurnTime(secondsLeft.getAndDecrement());
                    labelCounter.setText(secondsLeft.toString());
        }));

        timeline.setCycleCount(GameRules.turnTime + 1);
        PlayerActionUtils playerActionUtils = new PlayerActionUtils();
        Timeline timeline1 = timeline;
        timeline.setOnFinished(e -> playerActionUtils.doFold(gameState, timeline1));
        timeline.play();

    }

    public void endPlayerTurn(GameState gameState, Timeline timeline) {
        List<Player> remainingPlayers = gameState.getRemainingPlayers();

        int nextIndex = remainingPlayers.indexOf(gameState.getActivePlayer()) + 1;

        if (nextIndex == remainingPlayers.size()) {
            nextIndex = 0;
        }

        gameState.setActivePlayer(gameState.getRemainingPlayers().get(nextIndex));

        gameState.setTurnsToNextFaze(gameState.getTurnsToNextFaze() - 1);
        //startPlayerTurn(gameState, timeline);
    }

    private  void setGameStateNext(GameState gameState, Timeline timeline) {
        switch (gameState.getFaze()) {
            case PREFLOP:
                gameState.setFaze(GameFaze.FLOP);
                break;

            case FLOP:
                gameState.setFaze(GameFaze.TURN);
                break;

            case TURN:
                gameState.setFaze(GameFaze.RIVER);
                break;

            default:
                WinnerUtils winnerUtils = new WinnerUtils();
                gameState.setWinner(gameState.getRemainingPlayers().get(gameState.getRemainingPlayers().indexOf(winnerUtils.determineWinner(gameState,timeline))));
                gameState.setFaze(GameFaze.PREFLOP);
                return;
        }

        for (Player player : gameState.getRemainingPlayers()) {
            gameState.setPot(gameState.getPot() + player.getRoundBet());
            player.setRoundBet(0);
        }

        gameState.setTurnsToNextFaze(gameState.getRemainingPlayers().size());
        gameState.setRunningSum(0.0);

        //startPlayerTurn(gameState, timeline);
    }

    public static void setGameStateStart(GameState gameState) {

        gameState.setFaze(GameFaze.PREFLOP);
        gameState.setDeck(CardUtils.createShuffledDeck());
        gameState.setCommunityCards(CardUtils.createCommunityCards(gameState.getDeck()));
        //Players set before execution of this
        gameState.setRemainingPlayers(new ArrayList<>(gameState.getPlayers()));

        setPlayerRoles(gameState);

        gameState.setTurnsToNextFaze(gameState.getRemainingPlayers().size());
        gameState.setPot(0.0);
        gameState.setRunningSum(0.0);

        CardUtils.dealCards(gameState);
        executeBlindBets(gameState);


    }

    private static void setPlayerRoles(GameState gameState) {

        int playerCount = gameState.getRemainingPlayers().size();

        int currentIndex = 1;
        gameState.setSmallBlindPlayer(gameState.getRemainingPlayers().get(currentIndex));

        if (currentIndex == playerCount) {
            currentIndex = 0;
        } else {
            currentIndex +=1;
        }
        gameState.setBigBlindPlayer(gameState.getRemainingPlayers().get(currentIndex));

        if (currentIndex == playerCount) {
            currentIndex = 0;
        } else {
            currentIndex += 1;
        }
        gameState.setActivePlayer(gameState.getRemainingPlayers().get(currentIndex));
    }

    public static void executeBlindBets(GameState gameState) {

        gameState.getSmallBlindPlayer().setRoundBet(GameRules.smallBlind);
        double balance = gameState.getSmallBlindPlayer().getBalance() - gameState.getSmallBlindPlayer().getRoundBet();
        gameState.getSmallBlindPlayer().setBalance(balance);

        gameState.getBigBlindPlayer().setRoundBet(GameRules.bigBlind);
        balance = gameState.getBigBlindPlayer().getBalance() -gameState.getBigBlindPlayer().getRoundBet();
        gameState.getBigBlindPlayer().setBalance(balance);

        gameState.setRunningSum(gameState.getBigBlindPlayer().getRoundBet());

    }

}
