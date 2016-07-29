import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RestoreSite {
    private final String sitePath, gccPath, imsPath, tswPath;
    private final String gccVersion="", imsVersion="", tswVersion="";

    public RestoreSite(String site, String gcc, String ims, String tsw){
        sitePath = site;
        gccPath = gcc;
        imsPath = ims;
        tswPath = tsw;
    }

    public void restore() {
        if (gccPath != null) {
            try{
                Process p = Runtime.getRuntime().exec("cmd /c start " + gccPath);
                p.waitFor();

            }catch( IOException ex ){
                //Validate the case the file can't be accesed (not enought permissions)

            }catch( InterruptedException ex ){
                //Validate the case the process is being stopped by some external situation

            }
        }

        if (imsPath != null) {
            try{
                Process p = Runtime.getRuntime().exec("cmd /c start " + imsPath);
                p.waitFor();

            }catch( IOException ex ){
                //Validate the case the file can't be accesed (not enought permissions)

            }catch( InterruptedException ex ){
                //Validate the case the process is being stopped by some external situation

            }
        }

        if (tswPath != null) {
            try{
                Process p = Runtime.getRuntime().exec("cmd /c start " + tswPath);
                p.waitFor();

            }catch( IOException ex ){
                //Validate the case the file can't be accesed (not enought permissions)

            }catch( InterruptedException ex ){
                //Validate the case the process is being stopped by some external situation

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
