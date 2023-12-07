package hr.dulic.pokerapp.utils.parserUtils;
import hr.dulic.pokerapp.model.GameRules;
import org.xml.sax.Attributes;

import org.xml.sax.helpers.DefaultHandler;

class CustomHandler extends DefaultHandler {
    private String currentProperty;
    private StringBuilder currentPropertyValue = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("property")) {
            currentProperty = attributes.getValue("name");
            currentPropertyValue = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        currentPropertyValue.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("property")) {
            System.out.println("Property: " + currentProperty + ", Value: " + currentPropertyValue.toString());

            switch (currentProperty) {
                case "smallBlind":
                    GameRules.smallBlind = Double.parseDouble(currentPropertyValue.toString());
                    GameRules.bigBlind = GameRules.smallBlind * 2;
                    System.out.println("SMALL BLIND SET TO" + GameRules.smallBlind);
                    System.out.println("BIG BLIND SET TO" + GameRules.bigBlind);
                    break;
                case "turnTime":
                    GameRules.turnTime = Integer.parseInt(currentPropertyValue.toString());
                    break;
                case "betBlockIncrement":
                    GameRules.betBlockIncrement = Double.parseDouble(currentPropertyValue.toString());
                    break;
                case "numberOfPlayers":
                    GameRules.numberOfPlayers = Integer.parseInt(currentPropertyValue.toString());
                    break;
            }
        }
    }
}