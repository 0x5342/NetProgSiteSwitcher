import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class PopulateDirectoriesFromPreferences extends SwingWorker<String[],Object>{
    private String[] directories = new String[4];
    private final JTextField siteTextField, gccTextField, imsTextField, tswTextField;
    private static int SITE = 0;
    private static int GCC = 1;
    private static int IMS = 2;
    private static int TSW = 3;

    public PopulateDirectoriesFromPreferences(JTextField site, JTextField gcc, JTextField ims, JTextField tsw) {
        siteTextField = site;
        gccTextField = gcc;
        imsTextField = ims;
        tswTextField = tsw;
    }

    public String[] doInBackground() {

        try{
            String preferencesFilePath = "files/preferences.xml";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(preferencesFilePath);
            //Normalize the XML structure
            document.getDocumentElement().normalize();
            //Get all directories nodes
            NodeList nodeList = document.getElementsByTagName("directory");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType()==Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String id = eElement.getAttribute("id");
                    String location = eElement.getElementsByTagName("location").item(0).getTextContent();
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
