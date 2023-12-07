package hr.dulic.pokerapp.utils.parserUtils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

class XMLHandler extends DefaultHandler {
    private ArrayList<String> gameConfig;
    private StringBuilder data;

    public void startDocument() {
        gameConfig = new ArrayList<>();
        data = new StringBuilder();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        data.setLength(0);
    }

    public void characters(char[] ch, int start, int length) {
        data.append(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) {
        if (!data.toString().trim().isEmpty()) {
            gameConfig.add(qName + ": " + data.toString().trim());
        }
    }

    public ArrayList<String> getGameConfig() {
        return gameConfig;
    }
}