package hr.dulic.pokerapp.utils.gameUtils;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.Hand;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.CardNotation;
import hr.dulic.pokerapp.model.enums.CardSuite;
import hr.dulic.pokerapp.model.enums.HandType;
import hr.dulic.pokerapp.utils.ComparatorUtilities;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WinnerUtils {

    public Player determineWinner(GameState gameState, Timeline timeline) {

        List<Player> testPlayers= getStraightFlushPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getFourOfAKindPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);


        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getFullHousePlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getFlushPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getStraightPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getThreeOfAKindPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getTwoPairsPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        testPlayers=getOnePairPlayers(gameState);
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            timeline.stop();
            return testPlayers.getLast();
        }

        gameState.getRemainingPlayers().sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        timeline.stop();
        return gameState.getRemainingPlayers().getLast();

    }

    private List<Player> getOnePairPlayers(GameState gameState){

        List<Player> onePairPlayers= new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);
            List<Card> winningCards = new ArrayList<>();

            for (int i = 0; i < cards.size() - 1; i++) {

                if (cards.get(i).getNotation()==cards.get(i+1).getNotation()){

                    winningCards.add(cards.get(i));
                    winningCards.add(cards.get(i+1));
                    player.setWinningHand(new Hand(winningCards, HandType.ONE_PAIR));
                    onePairPlayers.add(player);

                }

            }

        }
        onePairPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return onePairPlayers;
    }

    private List<Player> getTwoPairsPlayers(GameState gameState){

        List<Player> twoPairPlayers=new ArrayList<>();
        List<Player> testPlayers=new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);
            List<Card> winningCards=new ArrayList<>();

            for (int i = 0; i < cards.size() - 1; i++) {

                if (cards.get(i).getNotation()==cards.get(i+1).getNotation()){

                    winningCards.add(cards.get(i));
                    winningCards.add(cards.get(i+1));

                }

            }

            cards.removeAll(winningCards);
            cards.sort(ComparatorUtilities.notationComparator);

            if (!winningCards.isEmpty()){

                for (int i = 0; i < cards.size() - 1; i++) {

                    if (cards.get(i).getNotation()==cards.get(i+1).getNotation()){

                        winningCards.add(cards.get(i));
                        winningCards.add(cards.get(i+1));
                        player.setWinningHand(new Hand(winningCards,HandType.TWO_PAIRS));
                        twoPairPlayers.add(player);

                    }

                }

            }

        }
        twoPairPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return twoPairPlayers;
    }

    private List<Player> getThreeOfAKindPlayers(GameState gameState){

        List<Player> threeOfAKindPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);


            for (int i = 0; i < cards.size()-2; i++) {

                if(cards.get(i).getNotation()==cards.get(i+1).getNotation() &&
                        cards.get(i+1).getNotation()==cards.get(i+2).getNotation() )
                {

                    if (player.getWinnningHand().getHandType().ordinal()<3){
                        List<Card> pairs = new ArrayList<>();
                        pairs.add(cards.get(i));
                        pairs.add(cards.get(i+1));
                        pairs.add(cards.get(i+2));
                        Hand hand= new Hand(pairs,HandType.THREE_OF_A_KIND);
                        player.setWinningHand(hand);
                        threeOfAKindPlayers.add(player);
                        break;
                    }

                }

            }

        }
        return threeOfAKindPlayers;
    }

    private List<Player> getFullHousePlayers(GameState gameState) {

        List<Player> fullHousePlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){

            List<Card> winningCards=new ArrayList<>();

            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);


            for (int i = 0; i < cards.size()-2; i++) {


                if (cards.get(i).getNotation()==cards.get(i+1).getNotation() &&
                        cards.get(i+1).getNotation() == cards.get(i+2).getNotation() && player.getWinnningHand().getHandType().ordinal()<6){

                    winningCards.add(cards.get(i));
                    winningCards.add(cards.get(i+1));
                    winningCards.add(cards.get(i+2));
                    break;

                }

            }

            if (!winningCards.isEmpty()){

                CardNotation notation=winningCards.getFirst().getNotation();

                List<Card> testCards=new ArrayList<>(cards);

                testCards.removeAll(winningCards);
                testCards.removeIf(testCard -> testCard.getNotation() == notation);
                testCards.sort(ComparatorUtilities.notationComparator);

                for (int i = 0; i < testCards.size()-1; i++) {

                    if (testCards.get(i).getNotation()==testCards.get(i+1).getNotation()){

                        winningCards.add(testCards.get(i));
                        winningCards.add(testCards.get(i+1));
                        player.setWinningHand(new Hand(winningCards,HandType.FULL_HOUSE));
                        fullHousePlayers.add(player);
                        break;


                    }

                }

            }

        }
        fullHousePlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return fullHousePlayers;
    }

    private List<Player> getFourOfAKindPlayers(GameState gameState) {

        List<Player> fourOfAKindPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers) {

            List<Card> testCards = new ArrayList<>(gameState.getCommunityCards());
            testCards.addAll(Arrays.stream(player.getPocketCards()).toList());
            testCards.sort(ComparatorUtilities.notationComparator);

            for (int i = 0; i <= testCards.size() - 4; i++) {
                if (testCards.get(i).getNotation() == testCards.get(i + 1).getNotation() &&
                        testCards.get(i + 1).getNotation() == testCards.get(i + 2).getNotation() &&
                        testCards.get(i + 2).getNotation() == testCards.get(i + 3).getNotation()) {

                    List<Card> winningHand = new ArrayList<>(testCards.subList(i, i + 4));
                    player.setWinningHand(new Hand(winningHand, HandType.FOUR_OF_A_KIND));
                    fourOfAKindPlayers.add(player);
                    break;
                }
            }
        }

        return fourOfAKindPlayers;
    }

    private List<Player> getStraightFlushPlayers(GameState gameState) {

        List<Player> straightFlushPlayers=new ArrayList<>();
        List<Player> straightPlayers=new ArrayList<>(getStraightPlayers(gameState));

        for (Player player : straightPlayers){
            List<Card> cards = new ArrayList<>(player.getWinnningHand().getCards());


            if (!cards.isEmpty()){

                System.out.println("cards not empty");
                if (cards.get(0).getSuite()==cards.get(1).getSuite() && cards.get(1).getSuite()==cards.get(2).getSuite() &&
                        cards.get(2).getSuite()==cards.get(3).getSuite() && cards.get(3).getSuite()==cards.get(4).getSuite()){

                    System.out.println("player inserted");
                    List<Card> winningHand=new ArrayList<>(player.getWinnningHand().getCards());
                    player.setWinningHand(new Hand(winningHand,HandType.STRAIGHT_FLUSH));
                    straightFlushPlayers.add(player);

                }

            }

        }
        straightFlushPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return straightFlushPlayers;
    }

    private List<Player> getFlushPlayers(GameState gameState){

        List<Player> flushPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.suiteComparator);

            for (int i = 0; i < cards.size() - 4; i++) {
                if (cards.get(i).getSuite() == cards.get(i + 1).getSuite() &&
                        cards.get(i + 1).getSuite() == cards.get(i + 2).getSuite() &&
                        cards.get(i + 2).getSuite() == cards.get(i + 3).getSuite() &&
                        cards.get(i + 3).getSuite() == cards.get(i + 4).getSuite()) {
                    if (player.getWinnningHand().getHandType().ordinal() < 5) {
                        List<Card> flush = new ArrayList<>(cards.subList(i, i + 5));
                        Hand hand = new Hand(flush, HandType.FLUSH);
                        player.setWinningHand(hand);
                        flushPlayers.add(player);
                    }
                    break; // Exit the loop after a flush is found.
                }
            }

        }
        return flushPlayers;
    }

    private List<Player> getStraightPlayers(GameState gameState){

        List<Player> straightPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(gameState.getRemainingPlayers());

        for (Player player : testPlayers){
            List<Card> cards=new ArrayList<>(gameState.getCommunityCards());
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);

            int spadesCount=0;
            int hearthsCount=0;
            int diamondCount=0;
            int clubsCount=0;

            for (Card card : cards){

                if (card.getSuite()== CardSuite.Clubs) clubsCount++;
                if (card.getSuite()==CardSuite.Spades) spadesCount++;
                if (card.getSuite()==CardSuite.Hearths) hearthsCount++;
                if (card.getSuite()==CardSuite.Diamonds) diamondCount++;

            }

            for (int i = 0; i < cards.size()-1; i++) {

                if (cards.get(i).getNotation()==cards.get(i+1).getNotation()){

                    if (clubsCount>4 && cards.get(i).getSuite()==CardSuite.Clubs){

                        cards.remove(cards.get(i+1));


                    }else if(clubsCount>4 && cards.get(i+1).getSuite()==CardSuite.Clubs){

                        cards.remove(cards.get(i));

                    }

                    else if (spadesCount>4 && cards.get(i).getSuite()==CardSuite.Spades){

                        cards.remove(cards.get(i+1));


                    }else if(spadesCount>4 && cards.get(i+1).getSuite()==CardSuite.Spades){

                        cards.remove(cards.get(i));

                    }
                    else if (hearthsCount>4 && cards.get(i).getSuite()==CardSuite.Hearths){

                        cards.remove(cards.get(i+1));


                    }else if(hearthsCount>4 && cards.get(i+1).getSuite()==CardSuite.Hearths){

                        cards.remove(cards.get(i));

                    }
                    else if (diamondCount>4 && cards.get(i).getSuite()==CardSuite.Diamonds){

                        cards.remove(cards.get(i+1));


                    }else if(diamondCount>4 && cards.get(i+1).getSuite()==CardSuite.Diamonds){

                        cards.remove(cards.get(i));

                    } else {

                        cards.remove(cards.get(i));

                    }

                }

            }

            for (int i = 0; i < cards.size()-4 ; i++) {

                int counter=0;

                for (int j = i; j < i + 4; j++) {

                    if (cards.get(j).getNotation().ordinal() + 1 == cards.get(j + 1).getNotation().ordinal()) {
                        counter++;

                    } else {
                        break;
                    }
                }

                if (counter==4){

                    List<Card> winningHand=new ArrayList<>();
                    for (int j = i; j < i + 5; j++) {

                        winningHand.add(cards.get(j));

                    }

                    player.setWinningHand(new Hand(winningHand,HandType.STRAIGHT));
                    straightPlayers.add(player);

                }

            }

        }
        return straightPlayers;
    }

}
