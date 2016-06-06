import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class PopulatePreferences extends SwingWorker<String[],Object>{
    private String[] directories = new String[4];
    private final JTextField siteTextField, gccTextField, imsTextField, tswTextField;
    private static int SITE = 0;
    private static int GCC = 1;
    private static int IMS = 2;
    private static int TSW = 3;

    public PopulatePreferences(JTextField site, JTextField gcc, JTextField ims, JTextField tsw) {

        siteTextField = site;
        gccTextField = gcc;
        imsTextField = ims;
        tswTextField = tsw;
    }

    public String[] doInBackground() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(new File("files/preferences.xml"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Normalize the XML structure
        document.getDocumentElement().normalize();
        //Get all directories
        NodeList nodeList = document.getElementsByTagName("directory");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE){
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String location = element.getElementsByTagName("location").item(0).getTextContent();
                switch (id){
                    case "site":
                        directories[SITE]=location;
                        break;
                    case "gcc":
                        directories[GCC]=location;
                        break;
                    case "ims":
                        directories[IMS]=location;
                        break;
                    case "tsw":
                        directories[TSW]=location;
                        break;
                }
            }
        }
        return directories;
    }

    protected void done(){
        String[] result = null;
        try {
            result = get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        siteTextField.setText(result[SITE]);
        gccTextField.setText(result[GCC]);
        imsTextField.setText(result[IMS]);
        tswTextField.setText(result[TSW]);
    }
}
