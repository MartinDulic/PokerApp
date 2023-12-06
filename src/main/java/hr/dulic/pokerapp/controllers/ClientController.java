package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.HelloApplication;
import hr.dulic.pokerapp.model.*;
import hr.dulic.pokerapp.model.enums.GameFaze;
import hr.dulic.pokerapp.model.enums.PlayerActionType;
import hr.dulic.pokerapp.utils.ChatUtils;
import hr.dulic.pokerapp.utils.networkUtils.ClientNetworkUtils;
import hr.dulic.pokerapp.utils.viewUtils.DrawUtils;
import hr.dulic.pokerapp.utils.viewUtils.ThreadUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.RemoteException;
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
    private Label lblWinningMessage;
    @FXML
    private TextFlow chatTextFlow;
    @FXML
    private TextField chatMessageTextField;
    @FXML
    private Button btnSendMsg;

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
    boolean startGame;
    public void initialize() {
        //create player
        ChatUtils.startChatMessagesRefreshThread(chatTextFlow);
        startGame = true;
        originalPlayer = new Player(PlayerConfig.playerName, 2000.0);
        System.out.println("CLIENT CONTROLLER: ORIGINAL PLAYER:" + originalPlayer.getUsername());

        start();
    }

    @Override
    public void run() {
        //Before starting send this clients player to server
        disableAllBtns();
        ClientNetworkUtils.sendObjectAsClient(originalPlayer);

        receiveAndDisplayGameState();
    }

    private void receiveAndDisplayGameState() {
        while (true){
            //Await server response
            gameState = ClientNetworkUtils.receiveGameStateAsClient(getName());

            System.out.println("ClientController: working player set");
            if (gameState.getWinner() != null) {
                setPlayerBalance();
                proclaimWinner(gameState);
                //restartGame();
                return;
            }
            if (gameState.getRemainingPlayers().contains(originalPlayer)) {
                workingPlayer = gameState.getRemainingPlayers().get(gameState.getRemainingPlayers().indexOf(originalPlayer));
            }

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

    private void setPlayerBalance() {
        originalPlayer = gameState.getPlayers().get(gameState.getPlayers().indexOf(originalPlayer));
        Platform.runLater(() -> lblBalanceValue.setText(originalPlayer.getBalance().toString()));
        System.out.println("Client Controller: originalPlayer balance: " + originalPlayer.getBalance());
    }

    private void proclaimWinner(GameState gameState) {

        Platform.runLater(() -> lblWinningMessage.setText(gameState.getPot() + " Winner : " + gameState.getWinner().getUsername() + " " +
                gameState.getWinner().getWinnningHand().getHandType().toString() + gameState.getWinner().getWiningInfo()));
        disableAllBtns();
    }

    private void restartGame() {
        System.out.println("GAME RESTARTING...");
        ThreadUtils.pauseThread(1000);
        System.out.println("3");
        ThreadUtils.pauseThread(1000);
        System.out.println("2");
        ThreadUtils.pauseThread(1000);
        System.out.println("1");
        ThreadUtils.pauseThread(1000);
    }

    private void displayPreflop() {
        Platform.runLater(() -> DrawUtils.paintPlayerCards(workingPlayer, ivPlayerCard1, ivPlayerCard2));
        Platform.runLater(() -> DrawUtils.setPlayerLblValues(workingPlayer, lblUsername, lblBalanceValue));
        Platform.runLater(() -> DrawUtils.setGameLblValues(lblPotAmount, gameState));
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
        disableAllBtns();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("betDialogView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Stage stage=new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            BetInputController betInputController = fxmlLoader.getController();

            // Pass a reference to this HelloController
            betInputController.setHelloController(this);

            // Define an event handler for when the new window is shown


            stage.setOnShown(event -> {

                betInputController.getSlider().setMin(GameRules.bigBlind);
                betInputController.getSlider().setMax(gameState.getActivePlayer().getBalance());
                betInputController.getSlider().setValue(GameRules.bigBlind);
                betInputController.getSlider().setBlockIncrement(GameRules.betBlockIncrement);
                betInputController.getSlider().setShowTickMarks(true);
                betInputController.getSlider().setShowTickLabels(true);
                betInputController.getSlider().setSnapToTicks(true);
                betInputController.getLblMin().setText(String.valueOf(GameRules.bigBlind));
                betInputController.getLblMax().setText(String.valueOf(gameState.getActivePlayer().getBalance()));
                betInputController.getLblAmount().setText(String.valueOf(GameRules.bigBlind));

                betInputController.getSlider().valueProperty().addListener((observable, oldValue, newValue) ->
                        betInputController.getLblAmount().setText(String.valueOf(betInputController.getSlider().getValue())));


            });

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendBet(double value) {
        timeline.stop();
        gameState.getActivePlayer().setAction(new PlayerAction(PlayerActionType.BET,value));
        ClientNetworkUtils.sendObjectAsClient(gameState);
        lblCounterTxt.setText("BET SENT!!!");
    }

    public void raise() {
        disableAllBtns();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("raiseDialogView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            Stage stage=new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            RaiseInputController raiseInputController = fxmlLoader.getController();

            // Pass a reference to this HelloController
            raiseInputController.setHelloController(this);

            // Define an event handler for when the new window is shown


            stage.setOnShown(event -> {

                raiseInputController.getSlider().setMin(GameRules.bigBlind + gameState.getRunningSum());
                raiseInputController.getSlider().setMax(gameState.getActivePlayer().getBalance());
                raiseInputController.getSlider().setValue(GameRules.bigBlind + gameState.getRunningSum());
                raiseInputController.getSlider().setBlockIncrement(GameRules.betBlockIncrement);
                raiseInputController.getSlider().setShowTickMarks(true);
                raiseInputController.getSlider().setShowTickLabels(true);
                raiseInputController.getSlider().setSnapToTicks(true);
                raiseInputController.getLblMin().setText(String.valueOf(GameRules.bigBlind + gameState.getRunningSum()));
                raiseInputController.getLblMax().setText(String.valueOf(gameState.getActivePlayer().getBalance()));
                raiseInputController.getLblAmount().setText(String.valueOf(GameRules.bigBlind + gameState.getRunningSum()));

                raiseInputController.getSlider().valueProperty().addListener((observable, oldValue, newValue) ->
                        raiseInputController.getLblAmount().setText(String.valueOf(raiseInputController.getSlider().getValue())));


            });

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void sendRaise(double value) {
        timeline.stop();
        gameState.getActivePlayer().setAction(new PlayerAction(PlayerActionType.RAISE,value));
        ClientNetworkUtils.sendObjectAsClient(gameState);
        lblCounterTxt.setText("RAISE SENT!!!");
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

    public void sendChatMessage() {
        String chatMessage = chatMessageTextField.getText();
        try {
            HelloApplication.chatRemoteService.sendChatMessage(originalPlayer.getUsername() + ":" + chatMessage);
            chatMessageTextField.clear();
            System.out.println("Chat message: " + chatMessage + " sent!");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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
        btnSendMsg.setDisable(false);
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
