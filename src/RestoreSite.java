import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

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
        Path dst = Paths.get("C:/Windows/sos.ini");
        // Check to see if there is an sos.ini file in this site's folder
        if (Files.exists(src)) {
            if (Files.exists(dst)){
                // Read the site's backup file into an arrayList
                Scanner siteInput = new Scanner(src);
                ArrayList<String> siteArrayList = new ArrayList<>();
                while (siteInput.hasNextLine()){
                    siteArrayList.add(siteInput.nextLine());
                }
                // Find the start and end line numbers of the [4100PROG] block
                int siteBlockStart = 0;
                int siteBlockEnd = 0;
                for (int i = 0; i < siteArrayList.size(); i++){
                    if(siteArrayList.get(i).contains("[4100PROG]")){
                        siteBlockStart = i;
                    }else if(siteBlockStart>0 && siteArrayList.get(i).isEmpty()){
                        siteBlockEnd = i;
                    }
                }

                // Read the Network Programmer's sos.ini file into an arrayList
                Scanner destInput = new Scanner(dst);
                ArrayList<String> destArrayList = new ArrayList<>();
                while(destInput.hasNextLine()){
                    destArrayList.add(destInput.nextLine());
                }
                //Find the start and end line numbers of the [4100PROG] block
                int destBlockStart = 0;
                int destBlockEnd = 0;
                for(int i = 0; i < destArrayList.size(); i++){
                    if(destArrayList.get(i).contains("[4100PROG]")){
                        destBlockStart = i;
                    }else if(destBlockStart>0 && destArrayList.get(i).isEmpty()){
                        destBlockEnd = i;
                    }
                }

                // Create a new arrayList, add the existing Network Programmer's sos.ini text up to the [4100PROG] block
                ArrayList<String> combArrayList = new ArrayList<>();
                for(int i = 0; i < destBlockStart; i++){
                    combArrayList.add(destArrayList.get(i));
                }
                // Add in the site's backup of the [4100PROG] block
                for(int i = siteBlockStart; i < siteBlockEnd; i++){
                    combArrayList.add(siteArrayList.get(i));
                }
                // Add the remainder of the existing Network Programmer text after the [4100PROG] block
                for(int i = destBlockEnd; i < destArrayList.size(); i++){
                    combArrayList.add(destArrayList.get(i));
                }

                // Overwrite the existing Network Programmer sos.ini file with the combined arrayList
                Files.write(dst,combArrayList, Charset.defaultCharset());

            }else{
                // On the condition that the sos.ini file is missing for the Network Programmer
                try {
                    // Copy the whole sos.ini file from the site backup folder to C:/Windows/sos.ini
                    Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            //TODO: Notify that there isn't a backup of the sos.ini file (directories programming)
        }
    }
}
