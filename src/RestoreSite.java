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

    public RestoreSite(String site, String gcc, String ims, String tsw){
        sitePath = site;
        gccPath = gcc;
        imsPath = ims;
        tswPath = tsw;
    }

    public void restore() throws IOException, ParseException {

        if (gccPath != null) {
            File gccShortcut = Paths.get(gccPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(gccShortcut);
            if(windowsShortcut.isPotentialValidLink(gccShortcut)) {
                gccVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start " + gccVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }

        if (imsPath != null) {

            File imsShortcut = Paths.get(imsPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(imsShortcut);
            if(windowsShortcut.isPotentialValidLink(imsShortcut)) {
                imsVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start " + imsVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }

        if (tswPath != null) {

            File tswShortcut = Paths.get(tswPath).toFile();
            WindowsShortcut windowsShortcut = new WindowsShortcut(tswShortcut);
            if(windowsShortcut.isPotentialValidLink(tswShortcut)) {
                tswVersion = windowsShortcut.getRealFilename().toString();
                try {
                    Process p = Runtime.getRuntime().exec("cmd /c start " + tswVersion);
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accesed (not enought permissions)

                } catch (InterruptedException ex) {
                    //Validate the case the process is being stopped by some external situation

                }
            }
        }

        Path src = Paths.get(sitePath+"sos.ini");
        if (Files.exists(src)) {
            Path dst = Paths.get("C:/Windows/sos.ini");
            try {
                Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
