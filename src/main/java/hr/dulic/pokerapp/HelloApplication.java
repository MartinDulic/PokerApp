package hr.dulic.pokerapp;

import hr.dulic.pokerapp.controllers.ClientController;
import hr.dulic.pokerapp.controllers.ServerController;
import hr.dulic.pokerapp.model.enums.NetworkMessageType;
import hr.dulic.pokerapp.utils.networkUtils.NetworkConfiguration;
import hr.dulic.pokerapp.model.enums.Role;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class HelloApplication extends Application {
    public static Role role;
    FXMLLoader fxmlLoader;
    @Override
    public void start(Stage stage) throws IOException {
        if (role == Role.SERVER){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("server-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 200, 200);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
        } else {

            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-dialog.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 300);
            stage.setTitle(role.name());
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        String roleString = args[0];
        role = Role.CLIENT;
        for (Role r : Role.values()){
            if (r.name().equals(roleString)){
                role = r;
                break;
            }
        }

        launch();
    }



}