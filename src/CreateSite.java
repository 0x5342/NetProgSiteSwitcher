import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class CreateSite extends SwingWorker<Boolean,Object> {
    private final String siteDirectory, siteName, gccVersion, imsVersion, tswVersion;
    private final boolean sosChecked;

    public CreateSite (String directory, String site, String gcc, String ims, String tsw, boolean sos){
        siteDirectory = directory;
        siteName = site;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
    }

    public Boolean doInBackground(){

        boolean result = false;
        // Create a path from the siteDirectory and the site name entered
        Path sitePath = Paths.get(siteDirectory+"/"+siteName);
        // Check to see if the folder already exists
        if (Files.exists(sitePath)) {
            // If the site already exists, popup a message
            JOptionPane.showMessageDialog(null,"That site name already exists.\n"+
                    "Enter a different name or use the Edit Tab to modify the existing site.");
            return false;
        } else {
            // The site doesn't already exist, so create a folder with the new site name
            try {
                Files.createDirectory(sitePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // Check to see if the site was created
        if (Files.exists(sitePath)) {
            // Site directory is available to write to
            if (sosChecked) {
                // Copy the "C:/Windows/sos.ini" to the new site directory just created
                Path source = Paths.get("C:/Windows/sos.ini");
                Path destination = Paths.get(sitePath+"/sos.ini");
                try {
                    Files.copy(source,destination);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"\bsos.ini file failed to copy!!!");
                    return false;
                }
            }
            // Create an XML document with each of the versions in it
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                Element rootElement = document.createElement("revs");
                document.appendChild(rootElement);
                Element gccChild = document.createElement("rev");
                gccChild.setAttribute("id","gcc");
                gccChild.appendChild(document.createTextNode(gccVersion));
                rootElement.appendChild(gccChild);
                Element imsChild = document.createElement("rev");
                imsChild.setAttribute("id","ims");
                imsChild.appendChild(document.createTextNode(imsVersion));
                rootElement.appendChild(imsChild);
                Element tswChild = document.createElement("rev");
                tswChild.setAttribute("id","tsw");
                tswChild.appendChild(document.createTextNode(tswVersion));
                rootElement.appendChild(tswChild);
                // Create and write the file
                //TODO: check that the file doesn't exist first
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult sResult = new StreamResult(new File(sitePath+"/revs_links.xml"));
                transformer.transform(source,sResult);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            result = true;
        }
        return result;
    }

    protected void done(){
        Boolean success = false;
        try {
            success = get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (success){
            // Site was created
            JOptionPane.showMessageDialog(null,siteName+" was created!");
        }else {
            // Site creation failed
            JOptionPane.showMessageDialog(null,"Creation of the site, "+siteName+", failed!");
            //TODO: Delete the new site directory if creation fails?
        }
    }
}
