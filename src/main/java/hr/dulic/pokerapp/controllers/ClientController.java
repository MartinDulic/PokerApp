package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.utils.DialogUtils;
import hr.dulic.pokerapp.utils.gameUtils.PlayerActionUtils;
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

import java.util.concurrent.atomic.AtomicInteger;

public class ClientController extends Thread{

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
        Player player;

		public void initialize(){
				//create player
				player = new Player("Matko" ,2000.0);

				start();

		}

		@Override
		public void run() {

			ClientNetworkUtils.sendPlayerAsClient(player);
			gameState = ClientNetworkUtils.receiveGameStateAsClient(getName());

			Player player1 = gameState.getRemainingPlayers().get(gameState.getRemainingPlayers().indexOf(player));

			Platform.runLater(() -> DrawUtils.paintPlayerCards(player1, ivPlayerCard1, ivPlayerCard2));
			Platform.runLater(() -> DrawUtils.setPlayerLblValues(player1, lblUsername, lblBalanceValue));

			Timeline timeline;
			AtomicInteger secondsLeft = new AtomicInteger(GameRules.turnTime);
			timeline = new Timeline(
				new KeyFrame(Duration.seconds(1), event -> {gameState.setTurnTime(secondsLeft.getAndDecrement());
					Platform.runLater(() -> lblCounter.setText(secondsLeft.toString()));
				}));

			timeline.setCycleCount(GameRules.turnTime);
			timeline.setOnFinished(e -> fold());
			timeline.play();

		}

		public void call(){

		}

		public void  bet() {

		}

		public void raise() {

		}

		public void check() {

		}

		public void fold() {
			lblCounterTxt.setText("FOLDED!!!");

		}

		public void saveGame() {

		}

		public void loadGame(){

		}

		public  void  createDocumentation() {

		}
}
