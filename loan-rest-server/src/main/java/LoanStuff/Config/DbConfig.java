package LoanStuff.Config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DbConfig {
    public String HOST;
    public String DB_NAME;
    public String USER;
    public String PASS;

    public static DbConfig Instance = new DbConfig();

    private DbConfig() {
        try {
            ReadSettings();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReadSettings() throws IOException {
        File inputFile = new File(System.getProperty("user.dir")+ "\\src\\main\\resources\\settings.xml");
        InputStream input = getClass().getResource("/settings.xml").openStream();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(input);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("settings");
        Node nNode = nList.item(0);
        Element eElement = (Element) nNode;
        this.HOST = eElement.getElementsByTagName("db-host").item(0).getTextContent();
        this.DB_NAME = eElement.getElementsByTagName("db-name").item(0).getTextContent();
        this.USER = eElement.getElementsByTagName("db-user").item(0).getTextContent();
        this.PASS = eElement.getElementsByTagName("db-password").item(0).getTextContent();
    }
}
