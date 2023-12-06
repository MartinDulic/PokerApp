package hr.dulic.pokerapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RaiseInputController {

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
    private Button btnRaise;

    public Slider getSlider() {
        return slider;
    }

    private ClientController controller;

    public void setHelloController(ClientController controller) {
        this.controller = controller;
    }

    public void onBtnRaiseClick() {
        // Pass the bet amount back to the HelloController
        controller.sendRaise(slider.getValue());

        Stage stage = (Stage) btnRaise.getScene().getWindow(); // Get a reference to the stage
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
