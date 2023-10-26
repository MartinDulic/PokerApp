package hr.dulic.pokerapp.utils;

import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.GameState;
import hr.dulic.pokerapp.model.Player;
import hr.dulic.pokerapp.model.enums.GameFaze;
import javafx.scene.control.Alert;


import java.io.*;
import java.util.List;

public class FileUtils {

    public static final String GAME_SAVE_FILE_NAME = "savedGame.dat";

    public static void saveGame(List<Player> players, List<Player> remainingPlayers, Player activePlayer, Player bigBlindPlayer,
                                Player smallBlindPlayer, List<Card> deck, List<Card> communityCards, GameFaze faze, int turnsToNextFaze,
                                Integer turnTime, double pot, double runningSum){

        GameState gameStateToBeSaved=new GameState(players,remainingPlayers,activePlayer,bigBlindPlayer,
                smallBlindPlayer,deck,communityCards,faze,turnsToNextFaze,turnTime,pot,runningSum);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FileUtils.GAME_SAVE_FILE_NAME)))
        {
            oos.writeObject(gameStateToBeSaved);
            DialogUtils.showDialog(Alert.AlertType.INFORMATION,
                    "Game saved!", "Your game has been successfully saved!");
        } catch (IOException e) {
            DialogUtils.showDialog(Alert.AlertType.ERROR,
                    "Game not saved!", "Your game has not been successfully saved!");
            throw new RuntimeException(e);
        }

    }

    public static GameState loadGame(){

        GameState recoveredGameState;
        try(ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FileUtils.GAME_SAVE_FILE_NAME))) {
            recoveredGameState = (GameState) ois.readObject();
        }
        catch(Exception ex) {
            DialogUtils.showDialog(Alert.AlertType.ERROR,
                    "Game not loaded!", "Your game has not been successfully loaded!");
            throw new RuntimeException(ex);
        }

        return recoveredGameState;
    }



}
