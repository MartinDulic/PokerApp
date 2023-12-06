package hr.dulic.pokerapp.controllers;

import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BetInputController{

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lblAmount;
    @FXML
    private Label lblMin;
    @FXML
    private Label lblMax;
    @FXML
    private Slider slider;
    @FXML
    private Button btnBet;

    public Slider getSlider() {
        return slider;
    }

    private ClientController controller;

    public void setHelloController(ClientController controller) {
        this.controller = controller;
    }

    public void onBtnBetClick() {
        // Pass the bet amount back to the HelloController
        controller.sendBet(slider.getValue());

        Stage stage = (Stage) btnBet.getScene().getWindow(); // Get a reference to the stage
        stage.close();
    }

    public Label getLblMax() {
        return lblMax;
    }

    public Label getLblMin() {
        return lblMin;
    }

    public Label getLblAmount() {
        return lblAmount;
    }
}
