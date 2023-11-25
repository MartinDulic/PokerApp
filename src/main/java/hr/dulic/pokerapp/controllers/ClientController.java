package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.model.ClientInstance;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.utils.gameUtils.GameStateUtils;
import hr.dulic.pokerapp.utils.networkUtils.NetworkConfiguration;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class ClientController {

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

        public GameState getGameState() {
                return gameState;
        }

        public void setGameState(GameState gameState) {
                this.gameState = gameState;
                System.out.println("Client: gamestate set");
        }

        public void initialize(){
                Player player = new Player("Player" + NetworkConfiguration.CLIENT_PORT,2000.0);
                ClientInstance clientInstance = new ClientInstance(player, NetworkConfiguration.CLIENT_PORT);
                GameStateUtils.sendClientInstanceState(clientInstance);


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

        }

        public void saveGame() {

        }

        public void loadGame(){

        }

        public  void  createDocumentation() {

        }
}
