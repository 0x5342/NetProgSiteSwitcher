import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sbrown on 5/30/2016.
 * This program will allow the user to save network setups for different sites
 * by selecting which TSW programmer needs to be loaded and saving a copy of
 * the sos.ini file to restore programmers directories that the site needs.
 */
public class MainUI extends JPanel {
    private JPanel mainPanel;
    private JButton createSiteButton, editSiteButton, restoreSiteButton, exitButton;
    private JTabbedPane tabbedPanel;
    private JPanel createSitePanel;
    private JPanel editSitePanel;
    private JPanel restoreSitePanel;
    private JLabel siteNameLabel;
    private JTextField nameOfCreateSite;
    private JLabel tswVersionCreate;
    private JButton chooseIMSVersionButton;
    private JLabel imsVersionCreate;
    private JButton chooseGCCVersionButton;
    private JButton chooseTSWVersionButton;
    private JLabel gccVersionCreate;
    private JCheckBox copySosCheckBoxCreate;
    private JButton createTheSiteButton;
    private JLabel nameOfEditSite;
    private JButton siteToEditButton;
    private JButton editGccVersionButton;
    private JButton editImsVersionButton;
    private JButton editTswVersionButton;
    private JLabel gccVersionEdit;
    private JLabel imsVersionEdit;
    private JLabel tswVersionEdit;
    private JCheckBox copySosCheckBoxEdit;
    private JButton saveChangesButton;
    private JButton siteToRestoreButton;
    private JLabel siteToRestoreChosen;

    public MainUI() {

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Create the main GUI and show it.
     */
    private static void createAndShowMainGUI(){

        JFrame frame = new JFrame("Network Programmer Site Saver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new MainUI().mainPanel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowMainGUI();
            }
        });
    }

}
