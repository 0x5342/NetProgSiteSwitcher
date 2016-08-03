import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;

public class RestoreSite {
    private final String sitePath, gccPath, imsPath, tswPath;
    private String gccVersion="", imsVersion="", tswVersion="";

    /**
     * Steps through executing the revision changing batch files for GCC, IMS, and TSW one at a time, and
     * copies the sos.ini file into the Windows directory.
     * @param site a string representing the path of the site to restore
     * @param gcc a string representing the GCC version shortcut path if this site has a GCC
     * @param ims a string representing the IMS version shortcut path if this site has an IMS
     * @param tsw a string representing the TSW version shortcut path if this site has a TSW
     */
    public RestoreSite(String site, String gcc, String ims, String tsw){
        sitePath = site;
        gccPath = gcc;
        imsPath = ims;
        tswPath = tsw;
    }

    public void restore() throws IOException, ParseException {
        // Check if there is a GCC version, find the file that the shortcut points to, and execute the file
        if (gccPath != null) {
            File gccShortcut = Paths.get(gccPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(gccShortcut);
            if(windowsShortcut.isPotentialValidLink(gccShortcut)) {
                gccVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start /wait " + gccVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }
        // Check if there is an IMS version, find the file that the shortcut points to, and execute the file
        if (imsPath != null) {

            File imsShortcut = Paths.get(imsPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(imsShortcut);
            if(windowsShortcut.isPotentialValidLink(imsShortcut)) {
                imsVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start /wait " + imsVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }
        // Check if there is a TSW version, find the file that the shortcut points to, and execute the file
        if (tswPath != null) {

            File tswShortcut = Paths.get(tswPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(tswShortcut);
            if(windowsShortcut.isPotentialValidLink(tswShortcut)) {
                tswVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start /wait " + tswVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }

        Path src = Paths.get(sitePath+"/sos.ini");
        // Check to see if there is an sos.ini file in this site's folder
        if (Files.exists(src)) {
            Path dst = Paths.get("C:/Windows/sos.ini");
            try {
                // Copy the sos.ini file from the folder to C:/Windows/sos.ini
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
