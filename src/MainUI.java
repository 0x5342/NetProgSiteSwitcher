import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Steven Brown on 5/30/2016.
 * This program will allow the user to save network setups for different sites
 * by selecting which TSW programmer needs to be loaded and saving a copy of
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

        //Master exit button to quit the application
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //Create site tab: choose the GCC version of software
        chooseGCCVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/GCC/GCC REVS"
                String directory = verifyPath(gccRevsDirectory);
                //TODO: do you need to filter by the shortcut extension or bat extension?
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if (file!=null) {
                    gccVersionCreate.setText(file.getName());
                }
            }
        });
        //Create site tab: choose the IMS version of software
        chooseIMSVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/IMS/IMS REVS"
                String directory = verifyPath(imsRevsDirectory);
                //TODO: do you need to filter by the shortcut extension or bat extension?
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if (file!=null) {
                    imsVersionCreate.setText(file.getName());
                }
            }
        });
        //Create site tab: choose the TSW version of software
        chooseTSWVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Default should be "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/Truesite/TSW/GCCREVS"
                String directory = verifyPath(tswRevsDirectory);
                //TODO: do you need to filter by the shortcut extension or bat extension?
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    tswVersionCreate.setText(file.getName());
                }
            }
        });
        //Create site tab: create the new site with the selected options
        createTheSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameOfCreateSite.getText().isEmpty()){
                    //TODO: create a dialog popup to say that a name must be entered
                }else{
                    //TODO: check to see if a folder with that name already exists
                    //TODO: if it does, create a dialog popup stating so and suggestion the edit site tab
//                    Path sitePath = Paths.get("/"+nameOfCreateSite.getText());
//                    try {
//                        Files.createDirectory(sitePath);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
                    //create a directory, within the directory where the program resides, with the new site name
                    boolean success = (new File(nameOfCreateSite.getText())).mkdir();
                    if (!success) {
                        // Directory creation failed
                        //TODO: some kind of notification that the creation failed
                    }else {//directory creation was a success
                        if (copySosCheckBoxCreate.isSelected()) {
                            //TODO: copy "C:/Windows/sos.ini" to the new site directory just created
                        }
                        //TODO: create file_links.txt with the GCC version, IMS version, & TSW version on separate lines
                        //TODO: positive feedback that the site was created
                    }
                }
            }
        });
        //Edit site tab: choose the site to edit
        siteToEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    nameOfEditSite.setText(file.getName());
                }
            }
        });
        //Edit site tab: choose the GCC version of software
        editGccVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(gccRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    gccVersionEdit.setText(file.getName());
                }
            }
        });
        //Edit site tab: choose the IMS version of software
        editImsVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(imsRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    imsVersionEdit.setText(file.getName());
                }
            }
        });
        //Edit site tab: choose the TSW version of software
        editTswVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(tswRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,FILE);
                if(file!=null) {
                    tswVersionEdit.setText(file.getName());
                }
            }
        });
        //Edit site tab: update the site with the changed information
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: can this and the create site button share a method?
            }
        });
        //Restore site tab: choose the site to restore
        siteToRestoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    siteToRestoreChosen.setText(file.getName());
                }
            }
        });
        //Restore site tab: run each selected Rev file and then copy the sos.ini back into C:\Windows
        restoreSiteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add code
            }
        });
        //Directories tab: change the directory where the sites are stored
        changeSiteDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(siteDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    siteDirectory.setText(file.getName());
                    //TODO: update the preferences file
                }
            }
        });
        //Directories tab: change where the GCC Revs directory is located
        changeGCCRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(gccRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    gccRevsDirectory.setText(file.getName());
                    //TODO: update the preferences file
                }
            }
        });
        //Directories tab: change where the IMS Revs directory is located
        changeIMSRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(imsRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    imsRevsDirectory.setText(file.getName());
                    //TODO: update the preferences file
                }
            }
        });
        //Directories tab: change where the TSW Revs directory is located
        changeTSWRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String directory = verifyPath(tswRevsDirectory);
                NpssFileChooser nFC = new NpssFileChooser();
                File file = nFC.NpssFileChooser(MainUI.this,directory,DIRECTORY);
                if(file!=null) {
                    tswRevsDirectory.setText(file.getName());
                    //TODO: update the preferences file
                }
            }
        });
        //When changing tabs, either clear the entry fields or populate fields from the preferences file
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Component tabSelected = tabbedPane.getSelectedComponent();
                if(tabSelected==createSiteTab){  //when the tab is selected clear the entry fields and check box
                    nameOfCreateSite.setText(null);
                    gccVersionCreate.setText(null);
                    imsVersionCreate.setText(null);
                    tswVersionCreate.setText(null);
                    copySosCheckBoxCreate.setSelected(true);
                }else if(tabSelected==editSiteTab){  //when the tab is selected clear the entry fields and check box
                    nameOfEditSite.setText(null);
                    gccVersionEdit.setText(null);
                    imsVersionEdit.setText(null);
                    tswVersionEdit.setText(null);
                    copySosCheckBoxEdit.setSelected(true);
                }else if(tabSelected==restoreSiteTab){  //when the tab is selected clear the entry fields
                    siteToRestoreChosen.setText(null);
                }else if(tabSelected==directoriesTab){  //when the tab is selected populate fields from preferences file
                    PopulatePreferences populatePreferences = new PopulatePreferences(siteDirectory,gccRevsDirectory,
                            imsRevsDirectory,tswRevsDirectory);
                    populatePreferences.execute();
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

    void mainScreen(){
        createAndShowMainGUI();
    }

    void runPopulatePreferences(){
        PopulatePreferences populatePreferences = new PopulatePreferences(siteDirectory,gccRevsDirectory,
                imsRevsDirectory,tswRevsDirectory);
        populatePreferences.execute();
    }

    private String verifyPath(JTextField gccRevsDirectory){
        //Check that the directory stored in the preferences file exists and return the path as a String
        // if it does. Otherwise return null
        Path path = Paths.get(gccRevsDirectory.getText());
        return (Files.isDirectory(path))? path.toString() : null;
    }
}
