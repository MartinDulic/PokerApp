package hr.dulic.pokerapp;

import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.GameFaze;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private List<Player> players;
    private List<Player> remainingPlayers;
    private Player activePlayer;
    private Player bigBlindPlayer;
    private Player smallBlindPlayer;
    private List<Card> deck;
    private List<Card> communityCards;
    private GameFaze faze;
    private Integer turnsToNextFaze;
    private Integer turnTime;
    private Double pot;
    private Double runningSum;
    private Player winner;


    public GameState() {
    }

    public GameState(List<Player> players, List<Player> remainingPlayers, Player activePlayer, Player bigBlindPlayer,
                     Player smallBlindPlayer, List<Card> deck, List<Card> communityCards, GameFaze faze, Integer turnsToNextFaze,
                     Integer turnTime, Double pot, Double runningSum)
    {
        this.players = players;
        this.remainingPlayers = remainingPlayers;
        this.activePlayer = activePlayer;
        this.bigBlindPlayer = bigBlindPlayer;
        this.smallBlindPlayer = smallBlindPlayer;
        this.deck = deck;
        this.communityCards = communityCards;
        this.faze = faze;
        this.turnsToNextFaze = turnsToNextFaze;
        this.turnTime=turnTime;
        this.pot = pot;
        this.runningSum = runningSum;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Integer getTurnTime() {
        return turnTime;
    }
    public void setTurnTime(Integer turnTime) {
        this.turnTime = turnTime;
    }

    public List<Player> getRemainingPlayers() {
        return remainingPlayers;
    }
    public void setRemainingPlayers(List<Player> remainingPlayers) {
        this.remainingPlayers = remainingPlayers;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Player getBigBlindPlayer() {
        return bigBlindPlayer;
    }
    public void setBigBlindPlayer(Player bigBlindPlayer) {
        this.bigBlindPlayer = bigBlindPlayer;
    }

    public Player getSmallBlindPlayer() {
        return smallBlindPlayer;
    }
    public void setSmallBlindPlayer(Player smallBlindPlayer) {
        this.smallBlindPlayer = smallBlindPlayer;
    }

    public List<Card> getDeck() {
        return deck;
    }
    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }
    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public GameFaze getFaze() {
        return faze;
    }
    public void setFaze(GameFaze faze) {
        this.faze = faze;
    }

    public Integer getTurnsToNextFaze() {
        return turnsToNextFaze;
    }
    public void setTurnsToNextFaze(Integer turnsToNextFaze) {
        this.turnsToNextFaze = turnsToNextFaze;
    }

    public Double getPot() {
        return pot;
    }
    public void setPot(Double pot) {
        this.pot = pot;
    }

    public Double getRunningSum() {
        return runningSum;
    }
    public void setRunningSum(Double runningSum) {
        this.runningSum = runningSum;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
