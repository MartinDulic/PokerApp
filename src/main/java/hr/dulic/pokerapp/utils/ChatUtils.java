package hr.dulic.pokerapp.utils;

import hr.dulic.pokerapp.model.enums.Role;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import hr.dulic.pokerapp.HelloApplication;

import java.rmi.RemoteException;
import java.util.List;

public class ChatUtils {

    public static void startChatMessagesRefreshThread(TextFlow chatTextFlow) {
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis(1000),
                event -> {
                    try {
                        chatTextFlow.getChildren().clear();
                        List<String> chatMessages = HelloApplication.chatRemoteService.getAllChatMessages();

                        for (String message : chatMessages) {
                            Text chatMessageText = new Text(message + "\n");
                            if (message.startsWith(Role.CLIENT.name())) {
                                chatMessageText.setFill(Color.GREEN);
                            }
                            else {
                                chatMessageText.setFill(Color.GREEN);
                            }
                            chatTextFlow.getChildren().add(chatMessageText);
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
