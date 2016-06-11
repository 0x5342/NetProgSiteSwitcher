import javax.swing.*;

public class NetworkProgrammerSiteSaver {

    private static MainUI mainUI;

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainUI = new MainUI();
                mainUI.mainScreen();
            }
        });
        //Pause to allow the mainUI to get going before loading the preferences
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Load the preferences file to make sure that the directories values are current
        mainUI.runPopulateDirectoriesFromPreferences();
    }
}
