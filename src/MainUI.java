import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Steven Brown on 5/30/2016.
 * This program will allow the user to save network setups for different sites
 * by selecting which GCC, IMS, & TSW programmers need to be loaded and saving a copy of
 * the sos.ini file to restore programmers directories that the site needs.
 */
public class MainUI extends JPanel {
    private JPanel mainPanel;
    private JButton createSiteButton, editSiteButton, restoreSiteButton, exitButton;
    private JTabbedPane tabbedPane;
    private JPanel createSiteTab;
    private JPanel editSiteTab;
    private JPanel restoreSiteTab;
    private JPanel directoriesTab;
    private JTextField nameOfCreateSite;
    private JButton chooseGCCVersionButton;
    private JTextField gccVersionCreate;
    private JButton chooseIMSVersionButton;
    private JTextField imsVersionCreate;
    private JButton chooseTSWVersionButton;
    private JTextField tswVersionCreate;
    private JCheckBox copySosCheckBoxCreate;
    private JButton createTheSiteButton;
    private JButton siteToEditButton;
    private JTextField nameOfEditSite;
    private JButton editGccVersionButton;
    private JTextField gccVersionEdit;
    private JButton editImsVersionButton;
    private JTextField imsVersionEdit;
    private JButton editTswVersionButton;
    private JTextField tswVersionEdit;
    private JCheckBox copySosCheckBoxEdit;
    private JButton saveChangesButton;
    private JButton siteToRestoreButton;
    private JTextField siteToRestoreChosen;
    private JButton changeSiteDirectoryButton;
    private JTextField siteDirectory;
    private JButton changeGCCRevsDirectoryButton;
    private JTextField gccRevsDirectory;
    private JButton changeIMSRevsDirectoryButton;
    private JTextField imsRevsDirectory;
    private JButton changeTSWRevsDirectoryButton;
    private JTextField tswRevsDirectory;
    private JButton deleteSiteButton;
    private JButton renameSiteButton;
    private JTextField renameSiteNewName;
    private JButton editSiteRenameClearButton;
    private JButton editSiteGccClearButton;
    private JButton editSiteImsClearButton;
    private JButton editSiteTswClearButton;
    private JButton createSiteGccClearButton;
    private JButton createSiteImsClearButton;
    private JButton createSiteTswClearButton;

    private static char FILE = 'F';
    private static char DIRECTORY = 'D';

    public MainUI() {

        // Load the directories from the preference file after startup
        tabbedPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                runPopulateDirectoriesFromPreferences();
            }
        });
        // Master exit button to quit the application
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /**
         * CREATE SITE TAB
         */
        // Create site tab: choose the GCC version of software
        chooseGCCVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/GCC/GCC REVS"
                String directory = verifyPath(gccRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if (file!=null) {
                    try {
                        gccVersionCreate.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        createSiteGccClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gccVersionCreate.setText("");
            }
        });
        // Create site tab: choose the IMS version of software
        chooseIMSVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/IMS/IMS REVS"
                String directory = verifyPath(imsRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if (file!=null) {
                    try {
                        imsVersionCreate.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        createSiteImsClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imsVersionCreate.setText("");
            }
        });
        // Create site tab: choose the TSW version of software
        chooseTSWVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/Truesite/TSW/GCCREVS"
                String directory = verifyPath(tswRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    try {
                        tswVersionCreate.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        createSiteTswClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tswVersionCreate.setText("");
            }
        });
        // Create site tab: create the new site with the selected options
        createTheSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if text has been entered into the siteName text field
                if(nameOfCreateSite.getText().isEmpty()){
                    // If the text field is empty, display a popup
                    JOptionPane.showMessageDialog(null, "Please enter a name for the site.");
                }else{
                    Path sitePath = Paths.get(siteDirectory.getText()+"/"+nameOfCreateSite.getText());
                    if (Files.exists(sitePath)) {
                        // If the site already exists, popup a message
                        JOptionPane.showMessageDialog(null, "That site name already exists.\n" +
                                "Enter a different name or use the Edit Tab to modify the existing site.");
                    }else {
                        runCreateSite(sitePath,
                                gccVersionCreate.getText(),
                                imsVersionCreate.getText(),
                                tswVersionCreate.getText(),
                                copySosCheckBoxCreate.isSelected()
                        );
                        clearCreateTabFields();
                    }
                }
            }
        });
        /**
         * EDIT SITE TAB
         */
        // Edit site tab: choose the site to edit
        siteToEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                String editSiteCanonicalPath;
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        nameOfEditSite.setText(file.getName());
                        editSiteCanonicalPath = file.getCanonicalPath();
                        //TODO: Check that it is a recognized site file
                        runReadSiteRevsFile(editSiteCanonicalPath);
                        // Enable form buttons once data is read in
                        editGccVersionButton.setEnabled(true);
                        editImsVersionButton.setEnabled(true);
                        editTswVersionButton.setEnabled(true);
                        copySosCheckBoxEdit.setEnabled(true);
                        saveChangesButton.setEnabled(true);
                        deleteSiteButton.setEnabled(true);
                        renameSiteButton.setEnabled(true);
                        editSiteRenameClearButton.setEnabled(true);
                        editSiteGccClearButton.setEnabled(true);
                        editSiteImsClearButton.setEnabled(true);
                        editSiteTswClearButton.setEnabled(true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        // Edit site tab: clear the site rename field
        editSiteRenameClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameSiteNewName.setText("");
                renameSiteNewName.setEnabled(false);
                renameSiteNewName.setEditable(false);
            }
        });
        // Edit site tab: choose the GCC version of software
        editGccVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(gccRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    try {
                        gccVersionEdit.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        editSiteGccClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gccVersionEdit.setText("");
            }
        });
        // Edit site tab: choose the IMS version of software
        editImsVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(imsRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    try {
                        imsVersionEdit.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        editSiteImsClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imsVersionEdit.setText("");
            }
        });
        // Edit site tab: choose the TSW version of software
        editTswVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(tswRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    try {
                        tswVersionEdit.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        editSiteTswClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tswVersionEdit.setText("");
            }
        });
        // Edit site tab: update the site with the changed information
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newSite=(renameSiteNewName.getText().isEmpty())?renameSiteNewName.getText():
                        siteDirectory.getText()+"\\"+renameSiteNewName.getText();
                runEditOrDeleteSite(
                        siteDirectory.getText()+"\\"+nameOfEditSite.getText(),
                        newSite,
                        gccVersionEdit.getText(),
                        imsVersionEdit.getText(),
                        tswVersionEdit.getText(),
                        copySosCheckBoxEdit.isSelected(),
                        true);
                clearEditTabFields();
            }
        });
        // Edit site tab: rename the selected site
        renameSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameSiteNewName.setEnabled(true);
                renameSiteNewName.setEditable(true);
                renameSiteNewName.setText(nameOfEditSite.getText());
                renameSiteNewName.requestFocusInWindow();
            }
        });
        // Edit site tab: delete the selected site
        deleteSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this site?",
                        "Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    runEditOrDeleteSite(
                            siteDirectory.getText()+"/"+nameOfEditSite.getText(),
                            renameSiteNewName.getText(),
                            gccVersionEdit.getText(),
                            imsVersionEdit.getText(),
                            tswVersionEdit.getText(),
                            copySosCheckBoxEdit.isSelected(),
                            false);
                    clearEditTabFields();
                }
            }
        });
        /**
         * RESTORE SITE TAB
         */
        // Restore site tab: choose the site to restore
        siteToRestoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        siteToRestoreChosen.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                restoreSiteButton.setEnabled(true);
            }
        });
        // Restore site tab: run each selected Rev file and then copy the sos.ini back into C:\Windows
        restoreSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReadSiteRevsFile readSiteRevsFile = new ReadSiteRevsFile(siteToRestoreChosen.getText());
                readSiteRevsFile.execute();
                siteToRestoreChosen.setText("");
            }
        });
        /**
         * DIRECTORIES TAB
         */
        // Directories tab: change the directory where the sites are stored
        changeSiteDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        siteDirectory.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runWriteDirectoriesFromPreferences();
                }
            }
        });
        // Directories tab: change where the GCC Revs directory is located
        changeGCCRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(gccRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        gccRevsDirectory.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runWriteDirectoriesFromPreferences();
                }
            }
        });
        // Directories tab: change where the IMS Revs directory is located
        changeIMSRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(imsRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        imsRevsDirectory.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runWriteDirectoriesFromPreferences();
                }
            }
        });
        // Directories tab: change where the TSW Revs directory is located
        changeTSWRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(tswRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        tswRevsDirectory.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    runWriteDirectoriesFromPreferences();
                }
            }
        });
        // When changing tabs, either clear the entry fields or populate fields from the preferences file
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Component tabSelected = tabbedPane.getSelectedComponent();
                if(tabSelected==createSiteTab){
                    //when the tab is selected clear the entry fields and check box
                    clearCreateTabFields();
                }else if(tabSelected==editSiteTab){
                    //when the tab is selected clear the entry fields and check box and disable buttons
                    clearEditTabFields();
                }else if(tabSelected==restoreSiteTab){
                    //when the tab is selected clear the entry field and disable the restore button
                    clearRestoreTabFields();
                }else if(tabSelected==directoriesTab){
                    //when the tab is selected populate fields from preferences file
                    runPopulateDirectoriesFromPreferences();
                }
            }
        });
    }

    /**
     * Create the main GUI and show it.
     */
    private void createAndShowMainGUI(){
        JFrame frame = new JFrame("Network Programmer Site Saver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainUI().mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void runWriteDirectoriesFromPreferences(){
        WriteDirectoriesToPreferences writeDirectoriesToPreferences =
                new WriteDirectoriesToPreferences(
                        siteDirectory,gccRevsDirectory,
                        imsRevsDirectory,tswRevsDirectory);
        writeDirectoriesToPreferences.execute();
    }

    private void runReadSiteRevsFile(String site){
        ReadSiteRevsFile readSiteRevsFile = new ReadSiteRevsFile(
                site,
                gccVersionEdit,
                imsVersionEdit,
                tswVersionEdit);
        readSiteRevsFile.execute();
    }

    private void runCreateSite(Path path, String gcc, String ims, String tsw, boolean sos){
        CreateSite createSite = new CreateSite(path, gcc, ims, tsw, sos);
        createSite.execute();
    }

    private void runEditOrDeleteSite(String oldSite, String newSite,
                                     String gcc, String ims, String tsw, boolean sos, boolean modify){
        if(newSite.isEmpty()) {
            EditOrDeleteSite editOrDeleteSite = new EditOrDeleteSite(oldSite, gcc, ims, tsw, sos, modify);
            editOrDeleteSite.execute();
        } else {
            EditOrDeleteSite editOrDeleteSite = new EditOrDeleteSite(oldSite, newSite, gcc, ims, tsw, sos, modify);
            editOrDeleteSite.execute();
        }
    }

    private void clearCreateTabFields(){
        nameOfCreateSite.setText(null);
        gccVersionCreate.setText(null);
        imsVersionCreate.setText(null);
        tswVersionCreate.setText(null);
        copySosCheckBoxCreate.setSelected(true);
    }

    private void clearEditTabFields(){
        nameOfEditSite.setText(null);
        gccVersionEdit.setText(null);
        imsVersionEdit.setText(null);
        tswVersionEdit.setText(null);
        copySosCheckBoxEdit.setSelected(true);
        editGccVersionButton.setEnabled(false);
        editImsVersionButton.setEnabled(false);
        editTswVersionButton.setEnabled(false);
        copySosCheckBoxEdit.setEnabled(false);
        saveChangesButton.setEnabled(false);
        deleteSiteButton.setEnabled(false);
        renameSiteButton.setEnabled(false);
        renameSiteNewName.setText(null);
        renameSiteNewName.setEditable(false);
        renameSiteNewName.setEnabled(false);
        editSiteRenameClearButton.setEnabled(false);
        editSiteGccClearButton.setEnabled(false);
        editSiteImsClearButton.setEnabled(false);
        editSiteTswClearButton.setEnabled(false);
    }

    private void clearRestoreTabFields(){
        siteToRestoreChosen.setText(null);
        restoreSiteButton.setEnabled(false);
    }

    private String verifyPath(JTextField revsDirectory){
        // Check that the directory stored in the preferences file exists and return the path as a String
        // if it does. Otherwise return null
        Path path = Paths.get(revsDirectory.getText());
        return (Files.isDirectory(path))? path.toString() : null;
    }

    void mainScreen(){
        createAndShowMainGUI();
    }

    void runPopulateDirectoriesFromPreferences(){
        PopulateDirectoriesFromPreferences populateDirectoriesFromPreferences =
                new PopulateDirectoriesFromPreferences(
                        siteDirectory,gccRevsDirectory,
                        imsRevsDirectory,tswRevsDirectory);
        populateDirectoriesFromPreferences.execute();
    }
}
