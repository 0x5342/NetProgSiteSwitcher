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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public class ReadSiteRevsFile extends SwingWorker<String[],Object>{
    private String[] revs = new String[4];
    private String sitePath;
    private JTextField gccTextField=null, imsTextField=null, tswTextField=null;
    private boolean restore;
    private static int GCC = 1;
    private static int IMS = 2;
    private static int TSW = 3;

    /**
     * This constructor will be used if the site's xml file is being read the data into JTextFields in Edit Site tab
     * @param site a string representing the path to the site that contains the xml file to be read
     * @param gcc the JTextField that will contain a string representing the path to the GCC version shortcut
     * @param ims the JTextField that will contain a string representing the path to the IMS version shortcut
     * @param tsw the JTextField that will contain a string representing the path to the TSW version shortcut
     */
    public ReadSiteRevsFile(String site, JTextField gcc, JTextField ims, JTextField tsw) {
        sitePath = site;
        gccTextField = gcc;
        imsTextField = ims;
        tswTextField = tsw;
        restore = false;
    }

    /**
     * This constructor will be used if the site is being read to restore a site
     * @param site a string representing the path to the site that contains the xml file to be read and restored
     */
    public ReadSiteRevsFile(String site){
        sitePath = site;
        restore = true;
    }

    public String[] doInBackground() {
        Path sp = Paths.get(sitePath+"/revs_links.xml");
        if (Files.exists(sp)) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(sitePath + "/revs_links.xml");
                //Normalize the XML structure
                document.getDocumentElement().normalize();
                //Get all directories nodes
                Node revsNode = document.getFirstChild();
                NodeList nodeList = revsNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node subNode = nodeList.item(i);
                    if (subNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (subNode.getNodeName().equals("gcc")) {
                            if (subNode.hasChildNodes()) {
                                Node subNodeChild = subNode.getFirstChild();
                                if (subNodeChild.getNodeType() == Node.TEXT_NODE) {
                                    revs[GCC] = subNodeChild.getNodeValue();
                                }
                            }
                        }
                        if (subNode.getNodeName().equals("ims")) {
                            if (subNode.hasChildNodes()) {
                                Node subNodeChild = subNode.getFirstChild();
                                if (subNodeChild.getNodeType() == Node.TEXT_NODE) {
                                    revs[IMS] = subNodeChild.getNodeValue();
                                }
                            }
                        }
                        if (subNode.getNodeName().equals("tsw")) {
                            if (subNode.hasChildNodes()) {
                                Node subNodeChild = subNode.getFirstChild();
                                if (subNodeChild.getNodeType() == Node.TEXT_NODE) {
                                    revs[TSW] = subNodeChild.getNodeValue();
                                }
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "No existing link file was found in the site folder.\n" +
                        "Just choose \"Create Site\" if no GCC, IMS, or TSW is on this network.\n" +
                        "Otherwise, choose links to add to the site or delete the site.");
                // Since the xml file does not exist, set the result array strings to ""
                revs[GCC] = "";
                revs[IMS] = "";
                revs[TSW] = "";
                return revs;
            }
        } else {
            revs[GCC] = "";
            revs[IMS] = "";
            revs[TSW] = "";
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
        if(!restore) {
            gccTextField.setText(result[GCC]);
            imsTextField.setText(result[IMS]);
            tswTextField.setText(result[TSW]);
        } else if(restore){
            RestoreSite restoreSite = new RestoreSite(sitePath,result[GCC],result[IMS],result[TSW]);
            try {
                restoreSite.restore();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

