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
        // Create site tab: create the new site with the selected options
        createTheSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if text has been entered into the siteName text field
                if(nameOfCreateSite.getText().isEmpty()){
                    // If the text field is empty, display a popup
                    JOptionPane.showMessageDialog(null, "Please enter a name for the site.");
                }else{
                    runCreateSite();
                }
            }
        });
        /**
         * EDIT SITE TAB
         */
        //TODO: Add a way to change the site name
        // Edit site tab: choose the site to edit
        siteToEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    try {
                        nameOfEditSite.setText(file.getCanonicalPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    //TODO: Check that it is a recognized site file
                    runReadSiteRevsFile(nameOfEditSite.getText());
                    editGccVersionButton.setEnabled(true);
                    editImsVersionButton.setEnabled(true);
                    editTswVersionButton.setEnabled(true);
                    copySosCheckBoxEdit.setEnabled(true);
                    saveChangesButton.setEnabled(true);
                }
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
        // Edit site tab: update the site with the changed information
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Run update site
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
            }
        });
        // Restore site tab: run each selected Rev file and then copy the sos.ini back into C:\Windows
        restoreSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add code
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
                    nameOfCreateSite.setText(null);
                    gccVersionCreate.setText(null);
                    imsVersionCreate.setText(null);
                    tswVersionCreate.setText(null);
                    copySosCheckBoxCreate.setSelected(true);
                }else if(tabSelected==editSiteTab){
                    //when the tab is selected clear the entry fields and check box and disable buttons
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
                }else if(tabSelected==restoreSiteTab){
                    //when the tab is selected clear the entry field and disable the restore button
                    siteToRestoreChosen.setText(null);
                    restoreSiteButton.setEnabled(false);
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

    private void runCreateSite(){
        CreateSite createSite = new CreateSite(
                siteDirectory.getText(),
                nameOfCreateSite.getText(),
                gccVersionCreate.getText(),
                imsVersionCreate.getText(),
                tswVersionCreate.getText(),
                copySosCheckBoxCreate.isSelected());
        createSite.execute();
    }

    private String verifyPath(JTextField revsDirectory){
        // Check that the directory stored in the preferences file exists and return the path as a String
        // if it does. Otherwise return null
        Path path = Paths.get(revsDirectory.getText());
//        if (path.toString().equalsIgnoreCase("")){
//            runPopulateDirectoriesFromPreferences();
//            repaint();
//            path = Paths.get(revsDirectory.getText());
//        }
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
