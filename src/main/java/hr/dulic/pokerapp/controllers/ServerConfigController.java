package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.HelloApplication;
import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.utils.parserUtils.GameConfigParserUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static hr.dulic.pokerapp.HelloApplication.role;

public class ServerConfigController {

    @FXML
    TextField tfNumberOfPlayers;
    @FXML
    TextField tfTurnTime;
    @FXML
    TextField tfSmallBlind;
    @FXML
    TextField tfMinimumBet;
    @FXML
    Button btnStartServer;

    public void initialize() {

        GameConfigParserUtils.setGameRulesFromXML();
        displaySettingValues();
    }

    private void displaySettingValues() {
        tfNumberOfPlayers.setText(String.valueOf(GameRules.numberOfPlayers));
        tfTurnTime.setText(String.valueOf(GameRules.turnTime));
        tfSmallBlind.setText(String.valueOf(GameRules.smallBlind));
        tfMinimumBet.setText(String.valueOf(GameRules.betBlockIncrement));
    }

    public void startServer() {
        try {

            setGameRules();
            GameConfigParserUtils.writeGameRulesXML();

            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 200, 130);
            Stage stage = new Stage();
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();

            Stage thisStage = (Stage) btnStartServer.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setGameRules() {
        GameRules.numberOfPlayers = Integer.parseInt(tfNumberOfPlayers.getText());
        GameRules.turnTime = Integer.parseInt(tfTurnTime.getText());
        GameRules.smallBlind = Double.parseDouble(tfSmallBlind.getText());
        GameRules.betBlockIncrement = Double.parseDouble(tfMinimumBet.getText());
    }

}
