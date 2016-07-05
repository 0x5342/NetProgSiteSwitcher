import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class SiteManipulator {
    //TODO: constructor not needed - these should all be used in the calling class
    private final String oldSitePathName, newSitePathName, gccVersion, imsVersion, tswVersion;
    private final boolean toDelete, sosChecked;

    /**
     *
     * @param newSite new site path if creating a new site or renaming an old site
     * @param oldSite existing site path if modifying or deleting an existing site
     * @param gcc path to the gcc version for this site
     * @param ims path to the ims version for this site
     * @param tsw path to the tsw version for this site
     * @param sos TRUE if the sos.ini file is to be copied into this site
     * @param deleteModify TRUE if the existing site is to be deleted; FALSE if the existing site is to be modified
     */
    public SiteManipulator (String newSite, String oldSite,
                            String gcc, String ims, String tsw, boolean sos, boolean deleteModify){
        oldSitePathName = oldSite;
        newSitePathName = newSite;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
        toDelete = deleteModify;
    }

    /**
     * Verify that a directory does exist
     * @param path the directory to check for an existence of
     * @return TRUE if it exists; FALSE if does not exist
     */
    public boolean verifyDirectoryExists(Path path){
        return (Files.isDirectory(path));
    }

    /**
     * Create a directory from a given path
     * @param path the directory path to create
     * @return TRUE if the directory was created; FALSE if the directory failed to be created
     */
    public boolean createDirectory(Path path){
        // Create a folder with the path
        try {
            Files.createDirectory(path);
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Copy the sos.ini file to the site directory
     * @param path the path to the site where the sos.ini file is to be copied
     * @return TRUE if the copy was successful; FALSE if the copy failed
     */
    public boolean copySOS(Path path){
        // Copy the "C:/Windows/sos.ini" to the new site directory just created
        Path source = Paths.get("C:/Windows/sos.ini");
        Path destination = Paths.get(path+"/sos.ini");
        try {
            Files.copy(source,destination);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Create an XML document (revs_links.xml) with paths to each of the computer versions in it
     * @param path the path to the site where the revs_links.xml file is to be created
     * @param gccVersion path to the gcc version for this site
     * @param imsVersion path to the ims version for this site
     * @param tswVersion path to the tsw version for this site
     * @return TRUE if the file was created successfully; FALSE if the creation failed
     */
    public boolean createRevsLinksFile(Path path, String gccVersion, String imsVersion, String tswVersion){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("revs");
            document.appendChild(rootElement);
            Element gccChild = document.createElement("gcc");
            gccChild.appendChild(document.createTextNode(gccVersion));
            rootElement.appendChild(gccChild);
            Element imsChild = document.createElement("ims");
            imsChild.appendChild(document.createTextNode(imsVersion));
            rootElement.appendChild(imsChild);
            Element tswChild = document.createElement("tsw");
            tswChild.appendChild(document.createTextNode(tswVersion));
            rootElement.appendChild(tswChild);
            // Create and write the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            StreamResult sResult = new StreamResult(new File(path+"/revs_links.xml"));
            transformer.transform(source,sResult);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Deletes either a directory or a file
     * @param path the path to either a file or a directory to delete
     * @throws IOException
     */
    public static void deleteFileOrFolder(final Path path) throws IOException {
        boolean result = false;
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return CONTINUE;
            }

            @Override public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                return handleException(e);
            }

            private FileVisitResult handleException(final IOException e) {
                e.printStackTrace(); //TODO: replace with more robust error handling
                return TERMINATE;
            }

            @Override public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                    throws IOException {
                if(e!=null)return handleException(e);
                Files.delete(dir);
                return CONTINUE;
            }
        });
    }
}
