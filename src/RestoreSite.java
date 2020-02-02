import javax.swing.*;
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
    // TODO: copy the existing Windows/sos.ini file to working directory
    // TODO: read the [4100PROG] section from xml and replace that section in copy of sos.ini file
    // TODO: copy the modified sos.ini file back into the Windows directory
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
                    String []commands = {"cmd.exe","/C","start /wait " + gccVersion};
                    Process p = Runtime.getRuntime().exec(commands);
                    // Wait for the commands executed by this process to complete before continuing
                    p.waitFor();

                } catch (IOException ex) {
                    //Validate the case the file can't be accessed (not enough permissions)

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
                    String []commands = {"cmd.exe","/C","start /wait " + imsVersion};
                    Process p = Runtime.getRuntime().exec(commands);
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
                    String []commands = {"cmd.exe","/C","start /wait " + tswVersion};
                    Process p = Runtime.getRuntime().exec(commands);
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
                int i = 0;
                while (i < siteArrayList.size()) {
                    if (siteArrayList.get(i).contains("[4100PROG]")) {
                        siteBlockStart = i;
                        break;
                    }
                    i++;
                }
                i = siteBlockStart;
                while (i < siteArrayList.size()){
                    if(siteArrayList.get(i).isEmpty()){
                        siteBlockEnd = i;
                        break;
                    }
                    i++;
                }

                // Read the Network Programmer's sos.ini file into an arrayList
                Scanner sosInput = new Scanner(dst);
                ArrayList<String> sosArrayList = new ArrayList<>();
                while(sosInput.hasNextLine()){
                    sosArrayList.add(sosInput.nextLine());
                }
                //Find the start and end line numbers of the [4100PROG] block
                int sosBlockStart = 0;
                int sosBlockEnd = 0;
                i =0;
                while (i < sosArrayList.size()) {
                    if (sosArrayList.get(i).contains("[4100PROG]")) {
                        sosBlockStart = i;
                        break;
                    }
                    i++;
                }
                i = sosBlockStart;
                while (i < sosArrayList.size()){
                    if(sosArrayList.get(i).isEmpty()){
                        sosBlockEnd = i;
                        break;
                    }
                    i++;
                }

                // Create a new arrayList that will be used to write the new sos.ini file
                ArrayList<String> combArrayList = new ArrayList<>();
                // Add the existing Network Programmer's sos.ini text up to the [4100PROG] block
                for(i = 0; i < sosBlockStart; i++){
                    combArrayList.add(sosArrayList.get(i));
                }
                // Add in the site's backup of the [4100PROG] block
                for(i = siteBlockStart; i < siteBlockEnd; i++){
                    combArrayList.add(siteArrayList.get(i));
                }
                // Add the remainder of the existing Network Programmer text after the [4100PROG] block
                for(i = sosBlockEnd; i < sosArrayList.size(); i++){
                    combArrayList.add(sosArrayList.get(i));
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

            // Notify that the site has been restored
            File file = new File(sitePath);
            String siteName = file.getName();
            JOptionPane.showMessageDialog(null,"Site \"" + siteName + "\" was restored.");

        }else{

            // Notify that there isn't a backup of the sos.ini file in the site directory
            File file = new File(sitePath);
            String siteName = file.getName();
            JOptionPane.showMessageDialog(null,"Site \"" + siteName +
                    "\" doesn't have a backup of the sos.ini file to restore.");

        }
    }
}
