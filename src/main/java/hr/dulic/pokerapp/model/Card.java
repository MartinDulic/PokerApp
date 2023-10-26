package hr.dulic.pokerapp.model;

import hr.dulic.pokerapp.model.enums.CardColor;
import hr.dulic.pokerapp.model.enums.CardNotation;
import hr.dulic.pokerapp.model.enums.CardSuite;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;

public class Card  implements Serializable {
    private static String IMG_DIR_PATH="file:src/main/resources/poker_cards_chips_2d/PNGs/cards/Set_A/large/card_a_";
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
            default -> ' ';
        };

        switch (this.Notation) {

            case Two:
                notationSymbol="2";
                break;
            case Three:
                notationSymbol="3";
                break;
            case Four:
                notationSymbol="4";
                break;
            case Five:
                notationSymbol="5";
                break;
            case Six:
                notationSymbol="6";
                break;
            case Seven:
                notationSymbol="7";
                break;
            case Eight:
                notationSymbol="8";
                break;
            case Nine:
                notationSymbol="9";
                break;
            case Ten:
                notationSymbol="10";
                break;
            case Jack:
                notationSymbol="j";
                break;
            case Queen:
                notationSymbol="q";
                break;
            case King:
                notationSymbol="k";
                break;
            case Ace:
                notationSymbol="a";
                break;
            default:
                notationSymbol="ERROR";
                break;
        }

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
