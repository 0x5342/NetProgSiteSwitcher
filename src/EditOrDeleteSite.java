import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditOrDeleteSite extends SwingWorker<Boolean,Object> {
    private final String oldSitePathName, newSitePathName, gccVersion, imsVersion, tswVersion;
    private final boolean toModify, sosChecked;

    // This constructor is used when changing data other than the site name or deleting the site.
    // The newSitePathName is set to the same as the oldSitePathName since the name is not changing.
    public EditOrDeleteSite(String oldSite, String gcc, String ims, String tsw, boolean sos, boolean modify){
        oldSitePathName = oldSite;
        newSitePathName = oldSite;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
        toModify = modify;
    }

    // This constructor is used when renaming a site
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
        //TODO: if there is an sos file and the copy new sos is not checked, keep the old sos file
        // Check to see if the folder already exists
        if (Files.exists(oldSitePath)) {
            new DeleteFileOrFolder(oldSitePath);
            if (toModify){
                CreateSite createSite = new CreateSite(newSitePath, gccVersion, imsVersion, tswVersion, sosChecked);
                createSite.execute();
            }
        }
            return true;
    }
}
