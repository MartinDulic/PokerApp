package hr.dulic.pokerapp.utils.parserUtils;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import hr.dulic.pokerapp.model.GameRules;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class GameConfigParserUtils {

    private static final String GAME_SETTINGS_FILE_PATH = "Game_Rules.xml";
    public static void setGameRulesFromXML() {
        try {
            // Create a SAXParser
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Create a handler for SAX events
            CustomHandler handler = new CustomHandler();

            // Parse the XML file
            saxParser.parse(new File(GAME_SETTINGS_FILE_PATH), handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda za pisanje XML datoteke koristeci DOM
    public static void writeGameRulesXML() {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create a new Document
            Document document = builder.newDocument();

            // Create the root element
            Element rootElement = document.createElement("configurations");
            document.appendChild(rootElement);

            // Add properties as child elements
            addPropertyElement(document, rootElement, "smallBlind", String.valueOf(GameRules.smallBlind));
            addPropertyElement(document, rootElement, "turnTime", String.valueOf(GameRules.turnTime));
            addPropertyElement(document, rootElement, "betBlockIncrement", String.valueOf(GameRules.betBlockIncrement));
            addPropertyElement(document, rootElement, "numberOfPlayers", String.valueOf(GameRules.numberOfPlayers));

            // Save the document to an XML file
            saveToXmlFile(document, GAME_SETTINGS_FILE_PATH);

            System.out.println("XML file created successfully!");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void addPropertyElement(Document document, Element root, String name, String value) {
        Element propertyElement = document.createElement("property");
        root.appendChild(propertyElement);

        // Add name attribute
        propertyElement.setAttribute("name", name);

        // Add text content
        propertyElement.appendChild(document.createTextNode(value));
    }

    private static void saveToXmlFile(Document document, String filePath)
            throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));

        transformer.transform(source, result);
    }
}


