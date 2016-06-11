import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class WriteDirectoriesToPreferences extends SwingWorker<Boolean,Object>{
    private final JTextField siteTextField, gccTextField, imsTextField, tswTextField;

    public WriteDirectoriesToPreferences(JTextField site, JTextField gcc, JTextField ims, JTextField tsw) {
        siteTextField = site;
        gccTextField = gcc;
        imsTextField = ims;
        tswTextField = tsw;
    }

    public Boolean doInBackground() {

        try{
            String preferencesFilePath = "files/preferences.xml";
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(preferencesFilePath);
            //Normalize the XML structure
            document.getDocumentElement().normalize();
            //Get all directories
            NodeList nodeList = document.getElementsByTagName("directory");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType()==Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String id = eElement.getAttribute("id");
                    String location = eElement.getElementsByTagName("location").item(0).getTextContent();
                    switch (id){
                        case "site":
                            eElement.getElementsByTagName("location").item(0).setTextContent(siteTextField.getText());
                            break;
                        case "gcc":
                            eElement.getElementsByTagName("location").item(0).setTextContent(gccTextField.getText());
                            break;
                        case "ims":
                            eElement.getElementsByTagName("location").item(0).setTextContent(imsTextField.getText());
                            break;
                        case "tsw":
                            eElement.getElementsByTagName("location").item(0).setTextContent(tswTextField.getText());
                            break;
                    }
                }
            }
            // Write the changes into the xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(preferencesFilePath));
            transformer.transform(source,result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void done(){
        Boolean result = false;
        try {
            result = get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result){
            //TODO: popup a message about file updated?
        }
    }
}
