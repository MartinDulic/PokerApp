package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.HandType;
import hr.dulic.pokerapp.model.enums.PlayerActionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Player implements Serializable {

    String username;
    Double balance;
    Card[] pocketCards= new Card[2];
    Double roundBet;
    Hand winnningHand;
    PlayerAction action;

    public Player(String username, Double balance){

        this.username=username;
        this.balance=balance;
        this.roundBet=0.0;
        this.winnningHand=new Hand(new ArrayList<>(), HandType.NO_PAIR);
        this.action = new PlayerAction(PlayerActionType.NONE, 0.0);

    }

    @Override
    public String toString(){

        return this.username;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public void setPocketCards(Card[] pocketCards) {
        this.pocketCards = pocketCards;
    }

    public Card[] getPocketCards() {
        return this.pocketCards;
    }

    public Double getBalance() {
        return balance;
    }

    public String getUsername() {
        return username;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public double getRoundBet() {
        return roundBet;
    }

    public void setRoundBet(double roundBet) {
        this.roundBet = roundBet;
    }

    public Hand getWinnningHand() {
        return winnningHand;
    }

    public void setWinningHand(Hand winnningHand) {
        this.winnningHand = winnningHand;
    }

    public PlayerAction getAction() {
        return action;
    }

    public void setAction(PlayerAction action) {
        this.action = action;
    }

    public String getWiningInfo(){
        StringBuilder s=new StringBuilder();
        for (Card card:
             winnningHand.cards) {
            s.append(card.toString()).append(" ");

        }
        return s.toString();
    }
}
