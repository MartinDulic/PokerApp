package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.HandType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Player implements Serializable {


    private static Integer idCounter  = 0;
    Integer id;
    String username;
    Double balance;
    Card[] pocketCards= new Card[2];
    Double roundBet;
    Hand winnningHand;

    public Player(String username, Double balance){

        this.id = idCounter;
        idCounter++;
        this.username=username;
        this.balance=balance;
        this.roundBet=0.0;
        this.winnningHand=new Hand(new ArrayList<>(), HandType.NO_PAIR);

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
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public Integer getId() {
        return id;
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

    public void setUsername(String username) {
        this.username = username;
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

    public void setWinnningHand(Hand winnningHand) {
        this.winnningHand = winnningHand;
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
