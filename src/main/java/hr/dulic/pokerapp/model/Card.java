package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.CardColor;
import hr.dulic.pokerapp.model.enums.CardNotation;
import hr.dulic.pokerapp.model.enums.CardSuite;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;

public class Card  implements Serializable {
    private static final String IMG_DIR_PATH="file:src/main/resources/poker_cards_chips_2d/PNGs/cards/Set_A/large/card_a_";
    public CardNotation Notation;
    public CardSuite Suite;
    public CardColor Color;
    transient
    public Image image;

    public Card(CardNotation  notation, CardSuite suite){

        this.Notation=notation;
        this.Suite=suite;

        setCardImg();

    }

    public void setCardImg() {
        char suitSymbol;
        String notationSymbol;

        suitSymbol = switch (this.Suite) {
            case CardSuite.Diamonds -> {
                this.Color = CardColor.Red;
                yield 'd';
            }
            case CardSuite.Hearths -> {
                this.Color = CardColor.Red;
                yield 'h';
            }
            case CardSuite.Spades -> {
                this.Color = CardColor.Black;
                yield 's';
            }
            case CardSuite.Clubs -> {
                this.Color = CardColor.Black;
                yield 'c';
            }

        };

        notationSymbol = switch (this.Notation) {
            case Two -> "2";
            case Three -> "3";
            case Four -> "4";
            case Five -> "5";
            case Six -> "6";
            case Seven -> "7";
            case Eight -> "8";
            case Nine -> "9";
            case Ten -> "10";
            case Jack -> "j";
            case Queen -> "q";
            case King -> "k";
            case Ace -> "a";
        };

        String imgPath=IMG_DIR_PATH + suitSymbol + notationSymbol + "_large.png";
        this.image=new Image(imgPath);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card otherCard = (Card) obj;
        return Notation == otherCard.Notation && Suite == otherCard.Suite;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Notation, Suite);
    }

    @Override
    public String toString(){
        return this.Notation + " " + this.Suite;
    }


    public CardColor getColor() {
        return Color;
    }

    public CardNotation getNotation() {
        return Notation;
    }

    public CardSuite getSuite() {
        return Suite;
    }

    public Image getImage() {
        return image;
    }


}
