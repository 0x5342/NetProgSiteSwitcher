import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditOrDeleteSite extends SwingWorker<Boolean,Object> {
    private final String sitePathName, gccVersion, imsVersion, tswVersion;
    private final boolean toModify, sosChecked;

    public EditOrDeleteSite(String site, String gcc, String ims, String tsw, boolean sos, boolean modify){
        sitePathName = site;
        gccVersion = gcc;
        imsVersion = ims;
        tswVersion = tsw;
        sosChecked = sos;
        toModify = modify;
    }

    public Boolean doInBackground(){
        Path sitePath = Paths.get(sitePathName);
        // Check to see if the folder already exists
        if (Files.exists(sitePath)) {
            new DeleteFileOrFolder(sitePath);
            if (toModify){
                CreateSite createSite = new CreateSite(sitePath, gccVersion, imsVersion, tswVersion, sosChecked);
                createSite.execute();
            }
        }
            return true;
    }
}
