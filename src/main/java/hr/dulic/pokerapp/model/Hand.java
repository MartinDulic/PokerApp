package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.HandType;

import java.io.Serializable;
import java.util.List;

public class Hand implements Serializable {

    List<Card> cards;
    HandType handType;

    public Hand(List<Card> cards,HandType handType){
        this.cards=cards;
        this.handType=handType;
    }

    public HandType getHandType() {
        return handType;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setHandType(HandType handType) {
        this.handType = handType;
    }
}
