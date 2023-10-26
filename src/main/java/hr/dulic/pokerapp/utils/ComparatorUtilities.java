package hr.dulic.pokerapp.utils;

import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.CardNotation;

import java.util.Comparator;

public class ComparatorUtilities {
    public static Comparator<Card> notationComparator = Comparator.comparing(Card::getNotation);
    public static Comparator<Card> suiteComparator = Comparator.comparing(Card::getSuite);

    public static Comparator<Player> playerPocketCardsNotationComparator = (player1, player2) -> {
        CardNotation minCard1 = player1.getWinnningHand().getCards().stream()
                .map(Card::getNotation)
                .min(CardNotation::compareTo)
                .orElse(CardNotation.Two);

        CardNotation minCard2 = player2.getWinnningHand().getCards().stream()
                .map(Card::getNotation)
                .min(CardNotation::compareTo)
                .orElse(CardNotation.Two);

        return minCard1.compareTo(minCard2);
    };


}
