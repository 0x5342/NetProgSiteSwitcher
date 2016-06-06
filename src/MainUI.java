import javafx.stage.FileChooser;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

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
                //TODO: make this start in "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/GCC/GCC REVS"
                //TODO: do you need to filter by the shortcut extension or bat extension?
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showOpenDialog(MainUI.this);
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    gccVersionCreate.setText(file.getName());
                }
            }
        });
        //Create site tab: choose the IMS version of software
        chooseIMSVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make this start in "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/IMS/IMS REVS"
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showOpenDialog(MainUI.this);
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    imsVersionCreate.setText(file.getName());
                }
            }
        });
        //Create site tab: choose the TSW version of software
        chooseTSWVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make this start in "C:/ProgramData/Microsoft/Windows/Start Menu/Programs/Simplex/Truesite/TSW/GCCREVS"
                JFileChooser fc = new JFileChooser();
                int returnValue = fc.showOpenDialog(MainUI.this);
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
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
                //TODO: start file chooser in the site directory
                //TODO: only allow directories to be selected since each site is a directory
            }
        });
        //Edit site tab: choose the GCC version of software
        editGccVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make this and the create tab launch a single method with the destination variable passed in
            }
        });
        //Edit site tab: choose the IMS version of software
        editImsVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make this and the create tab launch a single method with the destination variable passed in
            }
        });
        //Edit site tab: choose the TSW version of software
        editTswVersionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: make this and the create tab launch a single method with the destination variable passed in
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
                //TODO: can this and the edit tab share a method?
            }
        });
        //Restore site tab: run each selected GCC Rev file and then copy the sos.ini back into C:\Windows
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
                //TODO: add code
            }
        });
        //Directories tab: change where the GCC Revs directory is located
        changeGCCRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add code
            }
        });
        //Directories tab: change where the IMS Revs directory is located
        changeIMSRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add code
            }
        });
        //Directories tab: change where the TSW Revs directory is located
        changeTSWRevsDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: add code
            }
        });
        //Read the directories from the preference file and populate the textFields when the Directories tab is selected
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(tabbedPane.getSelectedComponent()==directoriesTab){
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

}
