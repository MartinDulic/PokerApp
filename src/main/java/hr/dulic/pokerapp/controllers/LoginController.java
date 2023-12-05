package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.HelloApplication;
import hr.dulic.pokerapp.model.PlayerConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField tfUsername;
    @FXML
    Button btnLogin;
    FXMLLoader fxmlLoader;


    public void login(){
        try {
            PlayerConfig.playerName = tfUsername.getText();
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("client-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1800, 1000);
            Stage stage = new Stage();
            stage.setTitle(PlayerConfig.playerName + "'s CLIENT");
            stage.setScene(scene);
            stage.show();

            Stage thisStage = (Stage) btnLogin.getScene().getWindow();
            thisStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
