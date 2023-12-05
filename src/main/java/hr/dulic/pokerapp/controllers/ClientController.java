package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.*;
import hr.dulic.pokerapp.model.enums.GameFaze;
import hr.dulic.pokerapp.model.enums.PlayerActionType;
import hr.dulic.pokerapp.utils.networkUtils.ClientNetworkUtils;
import hr.dulic.pokerapp.utils.viewUtils.DrawUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientController extends Thread {

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

    GameState gameState;
    Player originalPlayer;
    Player workingPlayer;
    Timeline timeline;
    public void initialize() {
        //create player
        originalPlayer = new Player("Matko", 2000.0);
        System.out.println("CLIENT CONTROLLER: ORIGINAL PLAYER ID:" + originalPlayer.getId());
        start();

    }

    @Override
    public void run() {

        //Before starting send this clients player to server
        disableAllBtns();
        ClientNetworkUtils.sendObjectAsClient(originalPlayer);

        while (true){
            //Await server response
            gameState = ClientNetworkUtils.receiveGameStateAsClient(getName());
            workingPlayer = gameState.getRemainingPlayers().get(gameState.getRemainingPlayers().indexOf(originalPlayer));
            System.out.println("ClientController: working player set");

            //if it is this players turn
            if (gameState.getActivePlayer().equals(originalPlayer)) {
                if (gameState.getFaze() == GameFaze.PREFLOP){
                    displayPreflop();
                    enablePreflopBtns();
                    startCounter();
                } else if (gameState.getFaze() == GameFaze.FLOP) {
                    displayFlop();
                    enableFlopBtns();
                    startCounter();
                } else if (gameState.getFaze() == GameFaze.TURN) {
                    displayTurn();
                    enableFlopBtns();
                    startCounter();
                } else {
                    displayRiver();
                    enableFlopBtns();
                    startCounter();
                }
            } else {
                if (gameState.getFaze() == GameFaze.PREFLOP) {
                    displayPreflop();
                } else if (gameState.getFaze() == GameFaze.FLOP) {
                    displayFlop();
                } else if (gameState.getFaze() == GameFaze.TURN) {
                    displayTurn();
                } else {
                    displayRiver();
                }
            }
        }




    }

    private void displayPreflop() {
        Platform.runLater(() -> DrawUtils.paintPlayerCards(workingPlayer, ivPlayerCard1, ivPlayerCard2));
        Platform.runLater(() -> DrawUtils.setPlayerLblValues(workingPlayer, lblUsername, lblBalanceValue));
    }

    private void displayFlop() {
        displayPreflop();
        //Paint flop cards
        ImageView[] ivs = {ivCommunityCard1, ivCommunityCard2, ivCommunityCard3};
        Card[] cards = {gameState.getCommunityCards().get(0), gameState.getCommunityCards().get(1), gameState.getCommunityCards().get(2)};
        Platform.runLater(() ->
                {
                    for (int i = 0; i < 3; i++) {
                        DrawUtils.paintCard(cards[i], ivs[i]);
                    }
                }
        );
    }
    private void displayTurn() {
        displayPreflop();
        DrawUtils.paintCard(gameState.getCommunityCards().get(3), ivCommunityCard4);
    }
    private void displayRiver() {
        displayPreflop();
        DrawUtils.paintCard(gameState.getCommunityCards().get(4), ivCommunityCard5);
    }



    private void startCounter() {

        AtomicInteger secondsLeft = new AtomicInteger(GameRules.turnTime);
        timeline = new Timeline();
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    gameState.setTurnTime(secondsLeft.getAndDecrement());
                    Platform.runLater(() -> lblCounter.setText(secondsLeft.toString()));
                }));

        timeline.setCycleCount(GameRules.turnTime);
        timeline.setOnFinished(e -> fold());
        timeline.play();
    }

    public void call() {
        timeline.stop();
        lblCounterTxt.setText("CALLED!!!");
        disableAllBtns();

        gameState.getActivePlayer().setAction(new PlayerAction(PlayerActionType.CALL,0.0));
        ClientNetworkUtils.sendObjectAsClient(gameState);
    }

    public void bet() {
        timeline.stop();
        lblCounterTxt.setText("BET DONE!!!");
        disableAllBtns();
    }

    public void raise() {
        timeline.stop();
        lblCounterTxt.setText("RAISED!!!");
        disableAllBtns();
    }

    public void check() {
        timeline.stop();
        lblCounterTxt.setText("CHECKED!!!");
        disableAllBtns();
        gameState.getActivePlayer().setAction(new PlayerAction(PlayerActionType.CHECK,0.0));
        ClientNetworkUtils.sendObjectAsClient(gameState);
    }

    public void fold() {
        timeline.stop();
        lblCounterTxt.setText("FOLDED!!!");
        disableAllBtns();
        gameState.getActivePlayer().setAction(new PlayerAction(PlayerActionType.FOLD,0.0));
        ClientNetworkUtils.sendObjectAsClient(gameState);
    }

    public void saveGame() {

    }

    public void loadGame() {

    }

    public void createDocumentation() {

    }




    private List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();
        for (javafx.scene.Node node : anchorPane.getChildren()) {
            if (node instanceof Button) {
                buttons.add((Button) node);
            }
        }
        return buttons;
    }
    private void disableAllBtns() {
        for(Button button : getAllButtons() ){
            button.setDisable(true);
        }
    }
    private void enablePreflopBtns(){
        disableAllBtns();
        btnCall.setDisable(false);
        btnRaise.setDisable(false);
        btnFold.setDisable(false);
    }

    private void enableFlopBtns() {
        disableAllBtns();
        btnCheck.setDisable(false);
        btnBet.setDisable(false);
        btnFold.setDisable(false);
    }
}
