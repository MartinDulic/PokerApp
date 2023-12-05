package hr.dulic.pokerapp.utils.viewUtils;

import hr.dulic.pokerapp.model.Card;
import hr.dulic.pokerapp.model.GameRules;
import hr.dulic.pokerapp.model.GameState;
import hr.dulic.pokerapp.model.Player;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DrawUtils {

    public static void paintCardBlank(ImageView imageView) {
        imageView.setImage(new Image("file:src/main/resources/hr/dulic/pokerapp/poker_cards_chips_2d/PNGs/decks/large/deck_3_large.png"));
    }
    public static void paintCard(Card card, ImageView imageView) {
        card.setCardImg();
        imageView.setImage(card.getImage());
    }

    public static void paintPlayerCards(Player player, ImageView ivPlayerCard1, ImageView ivPlayerCard2) {
        paintCard(player.getPocketCards()[0], ivPlayerCard1);
        paintCard(player.getPocketCards()[1], ivPlayerCard2);
    }


    private static void setLabelText(Label label, String text) {
        label.setText(text);
    }

    public static void setPlayerLblValues(Player player, Label lblUsername, Label lblBalance) {
        setLabelText(lblUsername, player.getUsername());
        setLabelText(lblBalance, Double.toString(player.getBalance()));

    }

    public static void setGameLblValues(Label lblPot, GameState gameState) {
        setLabelText(lblPot, gameState.getPot().toString());
    }
}
