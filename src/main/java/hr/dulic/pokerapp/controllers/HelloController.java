package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.HelloApplication;
import hr.dulic.pokerapp.model.*;
import hr.dulic.pokerapp.utils.ComparatorUtilities;
import hr.dulic.pokerapp.model.enums.CardNotation;
import hr.dulic.pokerapp.model.enums.CardSuite;
import hr.dulic.pokerapp.model.enums.GameFaze;
import hr.dulic.pokerapp.model.enums.HandType;
import hr.dulic.pokerapp.utils.DialogUtils;
import hr.dulic.pokerapp.utils.DocumentationUtils;
import hr.dulic.pokerapp.utils.FileUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView ivPlayerCard1;
    @FXML
    private ImageView ivPlayerCard2;
    @FXML
    private ImageView ivCommunityCard1;
    @FXML
    private ImageView ivCommunityCard2;
    @FXML
    private ImageView ivCommunityCard3;
    @FXML
    private ImageView ivCommunityCard4;
    @FXML
    private ImageView ivCommunityCard5;


    @FXML
    private Button btnCheck;
    @FXML
    private Button btnFold;
    @FXML
    private Button btnBet;
    @FXML
    private Button btnCall;
    @FXML
    private Button btnRaise;
    @FXML
    private Label lblCounter;
    @FXML
    private Label lblCounterTxt;
    @FXML
    private Label lblBalanceValue;
    @FXML
    private Label lblBalanceTxt;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPotTxt;
    @FXML
    private Label lblPotAmount;


    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menu;
    @FXML
    private MenuItem mniNew;
    @FXML
    private MenuItem mniSave;
    @FXML
    private MenuItem mniLoad;
    @FXML
    private MenuItem mniCreateDocs;


    List<Player> players;
    List<Player> remainingPlayers;
    Player activePlayer;
    Player bigBlindPlayer;
    Player smallBlindPlayer;
    List<Card> deck;
    List<Card> communityCards;
    GameFaze faze;
    Integer turnTime;
    int turnsToNextFaze;
    Timeline timeline;
    double pot;
    double runningSum;

    public void initialize(){

        setGameStateStart();

        //preflop
        startPlayerTurn(activePlayer);
/*
        Player p1=new Player("Player1",1000);
        Player p2=new Player("Player2",2000);

        Card c1=new Card(CardNotation.Ace,CardSuite.Spades);
        Card c2=new Card(CardNotation.Ten,CardSuite.Spades);
        Card c3=new Card(CardNotation.Nine,CardSuite.Spades);
        Card c4=new Card(CardNotation.Ten,CardSuite.Spades);

        Card[] cards=new Card[2];
        cards[0]=c1;
        cards[1]=c2;
        p1.setPocketCards(cards);
        Card[] cards2=new Card[2];
        cards2[0]=c3;
        cards2[1]=c4;
        p2.setPocketCards(cards2);

        Card cc1=new Card(CardNotation.King,CardSuite.Spades);
        Card cc2=new Card(CardNotation.Queen,CardSuite.Spades);
        Card cc3=new Card(CardNotation.Jack,CardSuite.Spades);
        Card cc4=new Card(CardNotation.Six,CardSuite.Spades);
        Card cc5=new Card(CardNotation.Five,CardSuite.Spades);

        communityCards.add(cc1);
        communityCards.add(cc2);
        communityCards.add(cc3);
        communityCards.add(cc4);
        communityCards.add(cc5);

        remainingPlayers.add(p1);
        remainingPlayers.add(p2);


        proclaimWinner(determineWinner());
*/

    }


    private void executeBlindBets() {

       smallBlindPlayer.setRoundBet(GameRules.smallBlind);
       double balance = smallBlindPlayer.getBalance() - smallBlindPlayer.getRoundBet();
       smallBlindPlayer.setBalance(balance);

       bigBlindPlayer.setRoundBet(GameRules.bigBlind);
       balance = bigBlindPlayer.getBalance() - bigBlindPlayer.getRoundBet();
       bigBlindPlayer.setBalance(balance);

       runningSum=bigBlindPlayer.getRoundBet();

    }

    public void onAnyBtnPressed(){
        timeline.stop();
        endPlayerTurn(activePlayer);
    }
    private void endPlayerTurn(Player player) {

        int nextIndex=remainingPlayers.indexOf(player) + 1;

        if(nextIndex==remainingPlayers.size()){
            nextIndex=0;
        }

        turnsToNextFaze--;
        startPlayerTurn(remainingPlayers.get(nextIndex));

    }

    private void disableAllBtns() {
        for(Button button : getAllButtons(anchorPane) ){
            button.setDisable(true);

        }
    }

    public void startPlayerTurn(Player player){

        activePlayer=player;

        if(turnsToNextFaze<1){

            setGameStateNext();

            return;
        }

        paintPlayerCards(activePlayer);

        setPlayerLblValues(player);

        turnTime=GameRules.turnTime;
        AtomicInteger secondsLeft = new AtomicInteger(turnTime);
        timeline = new Timeline(

                new KeyFrame(Duration.seconds(1), event -> {
                    turnTime=secondsLeft.getAndDecrement();
                    lblCounter.setText(String.valueOf(secondsLeft.get()));
                })
        );

        timeline.setCycleCount(GameRules.turnTime);
        timeline.setOnFinished(e -> fold());
        timeline.play();
    }

    private void setPlayerLblValues(Player player) {
        lblUsername.setText(player.getUsername());
        lblBalanceValue.setText(Double.toString(player.getBalance()));
    }

    private void setGameStateNext() {

        switch (faze) {
            case GameFaze.PREFLOP:

                faze=GameFaze.FLOP;

                enableFlopBtns();

                paintCommunityCard(communityCards.get(0),ivCommunityCard1);
                paintCommunityCard(communityCards.get(1),ivCommunityCard2);
                paintCommunityCard(communityCards.get(2),ivCommunityCard3);

                break;

            case GameFaze.FLOP:
                faze=GameFaze.TURN;

                enableFlopBtns();

                paintCommunityCard(communityCards.get(3),ivCommunityCard4);
                break;

            case GameFaze.TURN:
                faze=GameFaze.RIVER;

                enableFlopBtns();

                paintCommunityCard(communityCards.get(4),ivCommunityCard5);
                break;
            default:
                proclaimWinner(determineWinner());
                faze=GameFaze.PREFLOP;
                return;

        }

        for(Player player : remainingPlayers){

            pot+=player.getRoundBet();
            player.setRoundBet(0);

        }

        lblPotAmount.setText(String.valueOf(pot));

        turnsToNextFaze=remainingPlayers.size();
        runningSum=0;

        startPlayerTurn(activePlayer);

    }

    private Player determineWinner() {

        List<Player> testPlayers= getStraightFlushPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {

            return testPlayers.getLast();

        }

        testPlayers=getFourOfAKindPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);


        if (!testPlayers.isEmpty()) {

            return testPlayers.getLast();
        }

        testPlayers=getFullHousePlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        testPlayers=getFlushPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        testPlayers=getStraightPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        testPlayers=getThreeOfAKindPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        testPlayers=getTwoPairsPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        testPlayers=getOnePairPlayers();
        testPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);

        if (!testPlayers.isEmpty()) {
            return testPlayers.getLast();
        }

        remainingPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return remainingPlayers.getLast();

    }

    private List<Player> getOnePairPlayers(){

        List<Player> onePairPlayers= new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(communityCards);
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);
            List<Card> winningCards = new ArrayList<>();

            for (int i = 0; i < cards.size() - 1; i++) {

                if (cards.get(i).getNotation()==cards.get(i+1).getNotation()){

                    winningCards.add(cards.get(i));
                    winningCards.add(cards.get(i+1));
                    player.setWinningHand(new Hand(winningCards,HandType.ONE_PAIR));
                    onePairPlayers.add(player);

                }

            }

        }
        onePairPlayers.sort(ComparatorUtilities.playerPocketCardsNotationComparator);
        return onePairPlayers;
    }

    private List<Player> getTwoPairsPlayers(){

        List<Player> twoPairPlayers=new ArrayList<>();
        List<Player> testPlayers=new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(communityCards);
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

    private List<Player> getThreeOfAKindPlayers(){

        List<Player> threeOfAKindPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(communityCards);
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

    private List<Player> getFullHousePlayers() {

        List<Player> fullHousePlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){

            List<Card> winningCards=new ArrayList<>();

            List<Card> cards=new ArrayList<>(communityCards);
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

    private List<Player> getFourOfAKindPlayers() {

        List<Player> fourOfAKindPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers) {

            List<Card> testCards = new ArrayList<>(communityCards);
            testCards.addAll(Arrays.stream(player.getPocketCards()).toList());
            testCards.sort(ComparatorUtilities.notationComparator);

            for (int i = 0; i <= testCards.size() - 4; i++) {
                if (testCards.get(i).getNotation() == testCards.get(i + 1).getNotation() &&
                        testCards.get(i + 1).getNotation() == testCards.get(i + 2).getNotation() &&
                        testCards.get(i + 2).getNotation() == testCards.get(i + 3).getNotation()) {

                    List<Card> winningHand = new ArrayList<>(testCards.subList(i, i + 4));
                    player.setWinningHand(new Hand(winningHand, HandType.FOUR_OF_A_KIND));
                    fourOfAKindPlayers.add(player);
                    break; // Exit the loop after a four-of-a-kind is found.
                }
            }
        }

        return fourOfAKindPlayers;
    }

    private List<Player> getStraightFlushPlayers() {

        List<Player> straightFlushPlayers=new ArrayList<>();
        List<Player> straightPlayers=new ArrayList<>(getStraightPlayers());

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

    private List<Player> getFlushPlayers(){

        List<Player> flushPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){

            List<Card> cards=new ArrayList<>(communityCards);
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

    private List<Player> getStraightPlayers(){

        List<Player> straightPlayers=new ArrayList<>();
        List<Player> testPlayers = new ArrayList<>(remainingPlayers);

        for (Player player : testPlayers){
            List<Card> cards=new ArrayList<>(communityCards);
            cards.addAll(Arrays.stream(player.getPocketCards()).toList());
            cards.sort(ComparatorUtilities.notationComparator);

            int spadesCount=0;
            int hearthsCount=0;
            int diamondCount=0;
            int clubsCount=0;

            for (Card card : cards){

                if (card.getSuite()==CardSuite.Clubs) clubsCount++;
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
    private void enableFlopBtns() {
        disableAllBtns();
        btnCheck.setDisable(false);
        btnBet.setDisable(false);
        btnFold.setDisable(false);
    }

    private void paintCommunityCard(Card card,ImageView imageView) {

        imageView.setImage(card.getImage());

    }
    private void paintCommunityCardBlank(ImageView imageView) {

        imageView.setImage(new Image("file:src/main/resources/hr/dulic/pokerapp/poker_cards_chips_2d/PNGs/decks/large/deck_3_large.png"));

    }

    public void paintPlayerCards(Player player){
        ivPlayerCard1.setImage(player.getPocketCards()[0].getImage());
        ivPlayerCard2.setImage(player.getPocketCards()[1].getImage());
    }
    private void setGameStateStart(){

        disableAllBtns();

        faze=GameFaze.PREFLOP;
        deck=createShuffledDeck();
        communityCards=createCommunityCards();
        players=getPlayers();
        remainingPlayers=new ArrayList<>(players);
        activePlayer=remainingPlayers.get(3);
        smallBlindPlayer=remainingPlayers.get(1);
        bigBlindPlayer=remainingPlayers.get(2);
        turnsToNextFaze=remainingPlayers.size();
        pot=0;
        runningSum=0;

        dealCards();
        enablePreflopBtns();
        executeBlindBets();

    }

    private void dealCards() {
        for(Player player : remainingPlayers){
            Card[] cards = new Card[2];
            cards[0] = drawCard(deck);
            cards[1] = drawCard(deck);
            player.setPocketCards(cards);
        }
    }

    private List<Card> createShuffledDeck(){

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

    private List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();

        players.add(new Player("player1", 1000.0));
        players.add(new Player("player2", 2000.0));
        players.add(new Player("player3", 3000.0));
        players.add(new Player("player4", 4000.0));
        players.add(new Player("player5", 5000.0));
        players.add(new Player("player6", 6000.0));

        return players;
    }
    private Card drawCard(List<Card> cards){
        return cards.removeFirst();
    }

    private List<Button> getAllButtons(AnchorPane container) {
        List<Button> buttons = new ArrayList<>();

        for (javafx.scene.Node node : container.getChildren()) {
            if (node instanceof Button) {
                buttons.add((Button) node);
            }
        }

        return buttons;
    }

    private void enablePreflopBtns(){
        disableAllBtns();
        btnCall.setDisable(false);
        btnRaise.setDisable(false);
        btnFold.setDisable(false);
    }

    public void call(){

        double balance=activePlayer.getBalance();
        double currentRoundBet=activePlayer.getRoundBet();
        double callAmount=runningSum - currentRoundBet;

        activePlayer.setRoundBet(currentRoundBet + callAmount);
        activePlayer.setBalance(balance - callAmount);

        onAnyBtnPressed();
    }

    public void fold(){

        pot+=activePlayer.getRoundBet();
        activePlayer.setRoundBet(0);
        players.get(players.indexOf(activePlayer)).setBalance(activePlayer.getBalance());

        onAnyBtnPressed();
        remainingPlayers.remove(activePlayer);

        if (remainingPlayers.size()==1){
            proclaimWinner(remainingPlayers.getFirst());
        }

    }

    private void proclaimWinner(Player player) {
        timeline.stop();
        lblPotAmount.setText(pot + " Winner : " + player.getUsername() + " " + player.getWinnningHand().getHandType().toString() + player.getWiningInfo());
        disableAllBtns();
    }

    public void check(){

        onAnyBtnPressed();
    }

    public void bet() throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("betDialogView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Stage stage=new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            BetInputController betInputController = fxmlLoader.getController();

            // Pass a reference to this HelloController
           // betInputController.setHelloController(this);

            // Define an event handler for when the new window is shown


            stage.setOnShown(event -> {

                betInputController.getSlider().setMin(GameRules.bigBlind);
                betInputController.getSlider().setMax(activePlayer.getBalance());
                betInputController.getSlider().setValue(GameRules.bigBlind);
                betInputController.getSlider().setBlockIncrement(GameRules.betBlockIncrement);
                betInputController.getSlider().setShowTickMarks(true);
                betInputController.getSlider().setShowTickLabels(true);
                betInputController.getSlider().setSnapToTicks(true);
                betInputController.getLblMin().setText(String.valueOf(GameRules.bigBlind));
                betInputController.getLblMax().setText(String.valueOf(activePlayer.getBalance()));
                betInputController.getLblAmount().setText(String.valueOf(GameRules.bigBlind));

                betInputController.getSlider().valueProperty().addListener((observable, oldValue, newValue) ->
                        betInputController.getLblAmount().setText(String.valueOf(betInputController.getSlider().getValue())));


            });

            stage.show();

    }

    public void doBet(double betAmount) {

        double balance=activePlayer.getBalance();
        activePlayer.setBalance(balance-betAmount);
        double roundBet=activePlayer.getRoundBet();
        activePlayer.setRoundBet(roundBet+betAmount);

        if (activePlayer.getRoundBet()>runningSum){

            runningSum=activePlayer.getRoundBet();

        }

        turnsToNextFaze=remainingPlayers.size();
        enablePreflopBtns();
        onAnyBtnPressed();

    }

    public void raise() throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("raiseDialogView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        Stage stage=new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);

        RaiseInputController raiseInputController = fxmlLoader.getController();

        // Pass a reference to this HelloController
        //raiseInputController.setHelloController(this);

        // Define an event handler for when the new window is shown


        stage.setOnShown(event -> {

            raiseInputController.getSlider().setMin(GameRules.bigBlind + runningSum);
            raiseInputController.getSlider().setMax(activePlayer.getBalance());
            raiseInputController.getSlider().setValue(GameRules.bigBlind + runningSum);
            raiseInputController.getSlider().setBlockIncrement(GameRules.betBlockIncrement);
            raiseInputController.getSlider().setShowTickMarks(true);
            raiseInputController.getSlider().setShowTickLabels(true);
            raiseInputController.getSlider().setSnapToTicks(true);
            raiseInputController.getLblMin().setText(String.valueOf(GameRules.bigBlind + runningSum));
            raiseInputController.getLblMax().setText(String.valueOf(activePlayer.getBalance()));
            raiseInputController.getLblAmount().setText(String.valueOf(GameRules.bigBlind + runningSum));

            raiseInputController.getSlider().valueProperty().addListener((observable, oldValue, newValue) ->
                    raiseInputController.getLblAmount().setText(String.valueOf(raiseInputController.getSlider().getValue())));


        });

        stage.show();


    }

    public void doRaise(double raiseAmount){

        double balance=activePlayer.getBalance();
        activePlayer.setBalance(balance-raiseAmount);
        double roundBet=activePlayer.getRoundBet();
        activePlayer.setRoundBet(roundBet+raiseAmount);

        if (activePlayer.getRoundBet()>runningSum){

            runningSum=activePlayer.getRoundBet();

        }

        turnsToNextFaze=remainingPlayers.size();
        enablePreflopBtns();
        onAnyBtnPressed();

    }

    private List<Card> createCommunityCards() {

        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            cards.add(drawCard(deck));

        }

        return cards;

    }

    public void saveGame(){

        FileUtils.saveGame(players,remainingPlayers,activePlayer,bigBlindPlayer,smallBlindPlayer,deck,communityCards,faze,turnsToNextFaze,
                turnTime,pot,runningSum
        );

    }

    public void loadGame(){

        GameState recoveredGameState=FileUtils.loadGame();

        if (recoveredGameState!=null){
            timeline.stop();
            timeline=new Timeline();


            disableAllBtns();

            faze=recoveredGameState.getFaze();
            deck=recoveredGameState.getDeck();
            communityCards=recoveredGameState.getCommunityCards();
            players=recoveredGameState.getPlayers();
            remainingPlayers=recoveredGameState.getRemainingPlayers();

            deck.forEach(Card::setCardImg);
            communityCards.forEach(Card::setCardImg);
            for (Player player : remainingPlayers){

                for (Card pocketCard : player.getPocketCards()) {

                    pocketCard.setCardImg();

                }

            }

            activePlayer=recoveredGameState.getActivePlayer();
            smallBlindPlayer=recoveredGameState.getSmallBlindPlayer();
            bigBlindPlayer=recoveredGameState.getBigBlindPlayer();
            turnsToNextFaze=recoveredGameState.getTurnsToNextFaze();
            pot=recoveredGameState.getPot();
            runningSum=recoveredGameState.getRunningSum();
            turnTime=recoveredGameState.getTurnTime();

            paintPlayerCards(activePlayer);
            setPlayerLblValues(activePlayer);
            lblPotAmount.setText(String.valueOf(pot));

            switch (faze){
                case GameFaze.PREFLOP:
                    enablePreflopBtns();
                    paintCommunityCardBlank(ivCommunityCard1);
                    paintCommunityCardBlank(ivCommunityCard2);
                    paintCommunityCardBlank(ivCommunityCard3);
                    paintCommunityCardBlank(ivCommunityCard4);
                    paintCommunityCardBlank(ivCommunityCard5);
                    break;
                case GameFaze.FLOP:
                    paintCommunityCard(communityCards.get(0),ivCommunityCard1);
                    paintCommunityCard(communityCards.get(1),ivCommunityCard2);
                    paintCommunityCard(communityCards.get(2),ivCommunityCard3);
                    paintCommunityCardBlank(ivCommunityCard4);
                    paintCommunityCardBlank(ivCommunityCard5);
                    enableFlopBtns();
                    break;
                case GameFaze.TURN:
                    paintCommunityCard(communityCards.get(0),ivCommunityCard1);
                    paintCommunityCard(communityCards.get(1),ivCommunityCard2);
                    paintCommunityCard(communityCards.get(2),ivCommunityCard3);
                    paintCommunityCard(communityCards.get(3),ivCommunityCard4);
                    paintCommunityCardBlank(ivCommunityCard5);
                    enableFlopBtns();
                    break;
                case GameFaze.RIVER:
                    paintCommunityCard(communityCards.get(0),ivCommunityCard1);
                    paintCommunityCard(communityCards.get(1),ivCommunityCard2);
                    paintCommunityCard(communityCards.get(2),ivCommunityCard3);
                    paintCommunityCard(communityCards.get(3),ivCommunityCard4);
                    paintCommunityCard(communityCards.get(4),ivCommunityCard5);
                    enableFlopBtns();
                    break;
                default:
                    break;

            }

            AtomicInteger secondsLeft = new AtomicInteger(turnTime);
            System.out.println(turnTime);
            timeline = new Timeline(

                    new KeyFrame(Duration.seconds(1), event -> {
                        lblCounter.setText(String.valueOf(secondsLeft.get()));
                        turnTime=secondsLeft.getAndDecrement();
                    })
            );
            timeline.setCycleCount(recoveredGameState.getTurnTime());

            timeline.setOnFinished(e -> endPlayerTurn(activePlayer));
            timeline.play();


        }

        DialogUtils.showDialog(Alert.AlertType.INFORMATION,
                "Game loaded!", "Your game has been successfully loaded!"
        );

    }

    public void createDocumentation(){

        try {
            String HTMLHeader= """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Document</title>
                    </head>
                    <body>
                        \s
                       """;

            String HTMLFooter = """
                    </body>
                    </html>""\";
        
                        """;

            StringBuilder stringBuilder= new StringBuilder();


            List<String> listOfClassFilePaths = Files.walk(Paths.get("target"))
                    .map(Path::toString)
                    .filter(f -> f.endsWith(".class"))
                    .filter(f -> !f.endsWith("module-info.class"))
                    .toList();

            for(String classFilePath : listOfClassFilePaths) {
                String[] pathTokens = classFilePath.split("classes");
                String secondToken = pathTokens[1];
                String fqnWithSlashes = secondToken.substring(1, secondToken.lastIndexOf('.'));
                String fqn = fqnWithSlashes.replace('\\', '.');
                Class<?> deserializedClass = Class.forName(fqn);

                DocumentationUtils.readClassAndMembersInfo(deserializedClass,stringBuilder);
                System.out.println(stringBuilder);
            }

            String docs= HTMLHeader + "<div>" + stringBuilder + "</div>" + HTMLFooter;
            Path htmlDocumentationFile = Path.of("documentation.html");
            if (!Files.exists(htmlDocumentationFile)){
                Files.createFile(htmlDocumentationFile);
            }
            Files.write(htmlDocumentationFile, docs.getBytes());

            DialogUtils.showDialog(Alert.AlertType.INFORMATION,
                    "Documentation created!", "Documentation has been successfully created!"
            );

        } catch (IOException | ClassNotFoundException e) {
            DialogUtils.showDialog(Alert.AlertType.INFORMATION,
                    "Documentation not created!", "Error : Documentation has not been created!"
            );
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }





}