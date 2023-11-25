package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.GameState;
import javafx.animation.Timeline;


public class PlayerActionUtils {


    public void doFold(GameState gameState, Timeline timeline) {

        gameState.setPot(gameState.getPot() + gameState.getActivePlayer().getRoundBet());
        gameState.getActivePlayer().setRoundBet(0);

        //get active player from players and set balance of that player to a value of a working copy from remaining players
        gameState.getPlayers().get(gameState.getPlayers().indexOf(gameState.getActivePlayer())).setBalance(gameState.getActivePlayer().getBalance());

        onAnyBtnPressed(gameState, timeline);

        gameState.getRemainingPlayers().remove(gameState.getActivePlayer());

        if (gameState.getRemainingPlayers().size() == 1) {
            WinnerUtils winnerUtils = new WinnerUtils();
            gameState.setWinner(winnerUtils.determineWinner(gameState, timeline));
        }
    }

    public void doCall(GameState gameState, Timeline timeline){

        double balance=gameState.getActivePlayer().getBalance();
        double currentRoundBet=gameState.getActivePlayer().getRoundBet();
        double callAmount= gameState.getRunningSum() - currentRoundBet;

        gameState.getActivePlayer().setRoundBet(currentRoundBet + callAmount);
        gameState.getActivePlayer().setBalance(balance - callAmount);

        onAnyBtnPressed(gameState, timeline);
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
        onAnyBtnPressed(gameState, timeline);
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
        onAnyBtnPressed(gameState, timeline);

    }
    public void doCheck(GameState gameState, Timeline timeline){

        onAnyBtnPressed(gameState, timeline);
    }
    public void onAnyBtnPressed(GameState gameState, Timeline timeline){

        timeline.stop();
        TurnUtils turnUtils = new TurnUtils();
        turnUtils.endPlayerTurn(gameState, timeline);
    }



}
