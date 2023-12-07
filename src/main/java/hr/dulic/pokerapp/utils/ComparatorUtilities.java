package hr.dulic.pokerapp.utils;

import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.CardNotation;

import java.util.Comparator;

public class ComparatorUtilities {
    public static Comparator<Card> notationComparator = Comparator.comparing(Card::getNotation);
    public static Comparator<Card> suiteComparator = Comparator.comparing(Card::getSuite);

    public static Comparator<Player> playerPocketCardsNotationComparator = (player1, player2) -> {
        /*
        CardNotation minCard1 = player1.getWinnningHand().getCards().stream()
                .map(Card::getNotation)
                .min(CardNotation::compareTo).get();

        CardNotation minCard2 = player2.getWinnningHand().getCards().stream()
                .map(Card::getNotation)
                .min(CardNotation::compareTo).get();

        return minCard1.compareTo(minCard2);
        */

        Card player1Card1 = player1.getWinnningHand().getCards().get(0);
        Card player1Card2 = player1.getWinnningHand().getCards().get(1);
        Card player2Card1 = player2.getWinnningHand().getCards().get(0);
        Card player2Card2 = player2.getWinnningHand().getCards().get(1);

        Card comparingCard1;
        Card comparingCard2;

        if (player1Card1.getNotation().ordinal() > player1Card2.getNotation().ordinal()) {
            comparingCard1 = player1Card1;
        } else {
            comparingCard1 = player1Card2;
        }
        if (player2Card1.getNotation().ordinal() > player2Card2.getNotation().ordinal()) {
            comparingCard2 = player2Card1;
        } else {
            comparingCard2 = player2Card2;
        }

        if (comparingCard1.getNotation().ordinal() > comparingCard2.getNotation().ordinal()) {
            return 1;
        } else if (comparingCard1.getNotation().ordinal() < comparingCard2.getNotation().ordinal()) {
            return -1;
        }
        return 0;

    };


}
