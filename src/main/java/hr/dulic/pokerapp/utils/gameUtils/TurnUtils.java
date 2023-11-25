package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.GameFaze;
import hr.dulic.pokerapp.utils.networkUtils.NetworkUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnUtils {

    public void startPlayerTurn(GameState gameState, Timeline timeline) {
        if (gameState.getTurnsToNextFaze() < 1) {
            setGameStateNext(gameState, timeline);
            return;
        }

        int turnTime = GameRules.turnTime;
        AtomicInteger secondsLeft = new AtomicInteger(turnTime);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> gameState.setTurnTime(secondsLeft.getAndDecrement()))
        );

        timeline.setCycleCount(GameRules.turnTime);
        PlayerActionUtils playerActionUtils = new PlayerActionUtils();
        Timeline timeline1 = timeline;
        timeline.setOnFinished(e -> playerActionUtils.doFold(gameState, timeline1));
        timeline.play();

        ClientInstance cI = new ClientInstance();
        for (ClientInstance clientInstance : ServerController.clientInstances) {
            if(clientInstance.getPlayer() == gameState.getActivePlayer()) {
                cI = clientInstance;
                break;
            }
        }

        GameStateUtils.sendGameState(gameState, cI);
    }

    public void endPlayerTurn(GameState gameState, Timeline timeline) {
        List<Player> remainingPlayers = gameState.getRemainingPlayers();

        int nextIndex = remainingPlayers.indexOf(gameState.getActivePlayer()) + 1;

        if (nextIndex == remainingPlayers.size()) {
            nextIndex = 0;
        }

        gameState.setActivePlayer(gameState.getRemainingPlayers().get(nextIndex));

        gameState.setTurnsToNextFaze(gameState.getTurnsToNextFaze() - 1);
        startPlayerTurn(gameState, timeline);
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

        startPlayerTurn(gameState, timeline);
    }

    public static void setGameStateStart(GameState gameState, List<ClientInstance> clientInstances) {

        gameState.setFaze(GameFaze.PREFLOP);
        gameState.setDeck(CardUtils.createShuffledDeck());
        gameState.setCommunityCards(CardUtils.createCommunityCards(gameState.getDeck()));
        gameState.setPlayers(PlayerUtils.getPlayers());
        gameState.setRemainingPlayers(new ArrayList<>(gameState.getPlayers()));

        setPlayerRoles(gameState);

        gameState.setTurnsToNextFaze(gameState.getRemainingPlayers().size());
        gameState.setPot(0.0);
        gameState.setRunningSum(0.0);

        CardUtils.dealCards(gameState);
        executeBlindBets(gameState);

        for (ClientInstance clientInstance : clientInstances) {
            GameStateUtils.sendGameState(gameState, clientInstance);
        }

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
