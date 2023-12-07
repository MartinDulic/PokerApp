package hr.dulic.pokerapp.model;

public final class GameRules {
    public static double smallBlind;
    public static double bigBlind = 2 * smallBlind;
    public static int turnTime;
    public static double betBlockIncrement = smallBlind;
    public static int numberOfPlayers;
}
