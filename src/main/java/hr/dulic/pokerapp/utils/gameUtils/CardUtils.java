package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.CardNotation;
import hr.dulic.pokerapp.model.enums.CardSuite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardUtils {

    public static List<Card> createShuffledDeck(){

        List<Card> cards = new ArrayList<>();

        for(CardNotation notation: CardNotation.values()) {
            for (CardSuite suite : CardSuite.values()){
                Card card=new Card(notation,suite);
                cards.add(card);
            }
        }

        Collections.shuffle(cards);
        return  cards;
    }


    public static Card drawCard(List<Card> cards){
        return cards.removeFirst();
    }
    public static List<Card> createCommunityCards(List<Card> deck) {

        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            cards.add(drawCard(deck));

        }

        return cards;
    }

    public static void dealCards(GameState gameState) {
        for(Player player : gameState.getRemainingPlayers()){
            Card[] cards = new Card[2];
            cards[0] = drawCard(gameState.getDeck());
            cards[1] = drawCard(gameState.getDeck());
            player.setPocketCards(cards);
        }
    }


}
