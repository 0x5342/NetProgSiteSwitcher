import javax.swing.*;
import java.awt.*;
import java.io.File;

public class NpssFileChooser {

    public File NpssFileChooser(Component c, String dir, char fOrD){
        String startDirectory = dir;
        char fileOrDirectory = fOrD;
        JFileChooser fc = new JFileChooser(dir);
        if(fOrD=='d'||fOrD=='D'){
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        fc.setMultiSelectionEnabled(false);
        int returnValue = fc.showOpenDialog(c);
        if(returnValue==JFileChooser.APPROVE_OPTION){
            File returnFile = fc.getSelectedFile();
            return returnFile;
        } else {
            return null;
        }
    }
}
