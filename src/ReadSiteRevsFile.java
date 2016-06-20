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

public class ReadSiteRevsFile extends SwingWorker<String[],Object>{
    private String[] revs = new String[4];
    private String sitePath;
    private final JTextField gccTextField, imsTextField, tswTextField;
    private static int GCC = 1;
    private static int IMS = 2;
    private static int TSW = 3;

    public ReadSiteRevsFile(String site, JTextField gcc, JTextField ims, JTextField tsw) {
        sitePath = site;
        gccTextField = gcc;
        imsTextField = ims;
        tswTextField = tsw;
    }

    public String[] doInBackground() {

        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(sitePath+"/revs_links.xml");
            //Normalize the XML structure
            document.getDocumentElement().normalize();
            //Get all directories nodes
            NodeList nodeList = document.getElementsByTagName("rev");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType()==Node.ELEMENT_NODE){
                    Element eElement = (Element) node;
                    String id = eElement.getAttribute("id");
                    String revPath = eElement.getElementsByTagName("path").item(0).getTextContent();
                    switch (id){
                        case "gcc":
                            revs[GCC]=revPath;
                            break;
                        case "ims":
                            revs[IMS]=revPath;
                            break;
                        case "tsw":
                            revs[TSW]=revPath;
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
        return revs;
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
        gccTextField.setText(result[GCC]);
        imsTextField.setText(result[IMS]);
        tswTextField.setText(result[TSW]);
    }
}

