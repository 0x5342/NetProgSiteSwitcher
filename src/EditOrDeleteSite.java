import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EditOrDeleteSite extends SwingWorker<Boolean,Object> {
    private final String oldSitePathName, newSitePathName, gccVersion, imsVersion, tswVersion;
    private final boolean toModify, sosChecked;
    private boolean copyTempSos;

    /**
     * This constructor is used when changing data other than the site name or deleting the site.
     * The newSitePathName is set to the same as the oldSitePathName since the name is not changing.
     * @param oldSite a string representing the path to the site that is being edited
     * @param gcc a string representing the path to the GCC version shortcut
     * @param ims a string representing the path to the IMS version shortcut
     * @param tsw a string representing the path to the TSW version shortcut
     * @param sos TRUE if a new copy of the sos.ini file is to be made. FALSE if the sos.ini should be let be.
     * @param modify TRUE if this site is being modified. FALSE if the site to just be deleted.
     */
    public EditOrDeleteSite(String oldSite, String gcc, String ims, String tsw, boolean sos, boolean modify){
        oldSitePathName = oldSite;
        newSitePathName = oldSite;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
        toModify = modify;
    }

    /**
     * This constructor is used when renaming a site along with any other modifications.
     * @param oldSite a string representing the path to the site that is being edited
     * @param newSite a string representing the path to the site that is being edited with its new name
     * @param gcc a string representing the path to the GCC version shortcut
     * @param ims a string representing the path to the IMS version shortcut
     * @param tsw a string representing the path to the TSW version shortcut
     * @param sos TRUE if a new copy of the sos.ini file is to be made. FALSE if the sos.ini should be let be.
     * @param modify TRUE if this site is being modified. FALSE if the site to just be deleted.
     */
    public EditOrDeleteSite (String oldSite, String newSite,
                             String gcc, String ims, String tsw, boolean sos, boolean modify){
        oldSitePathName = oldSite;
        newSitePathName = newSite;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
        toModify = modify;
    }

    public Boolean doInBackground(){
        Path oldSitePath = Paths.get(oldSitePathName);
        Path newSitePath = Paths.get(newSitePathName);
        Path tempDestination = null;
        copyTempSos = false;
        // If copy new sos is not checked and the site is being modified...
        if(!sosChecked&&toModify){
            Path oldSiteSosPath = Paths.get(oldSitePathName+"/sos.ini");
            // and a sos.ini file does exist in the site folder...
            if (Files.exists(oldSiteSosPath)){
                tempDestination = Paths.get("c:/sos.temp");
                try {
                    // make a temporary copy of the sos.ini file to be brought back after the other changes
                    Files.copy(oldSiteSosPath,tempDestination,StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // This boolean is set to know that a temp copy is to be put back after the other changes
                copyTempSos = true;
            }
        }
        // Check to see if the site folder already exists
        if (Files.exists(oldSitePath)) {
            // Delete the old site folder and its contents
            new DeleteFileOrFolder(oldSitePath);
            // If this is a site modification, create the site with the modifications
            if (toModify){
                CreateSite createSite = new CreateSite(newSitePath, gccVersion, imsVersion, tswVersion, sosChecked);
                createSite.execute();
                // If a temporary copy of the sos.ini was made, copy it back into the modified site folder
                if(copyTempSos) {
                    Path newSiteSosPath = Paths.get(newSitePath+"/sos.ini");
                    //Pause for 1 seconds
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Files.copy(tempDestination,newSiteSosPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Delete the temporary copy of the sos.ini file
                    new DeleteFileOrFolder(tempDestination);
                }
            }
        }
            return true;
    }
}
