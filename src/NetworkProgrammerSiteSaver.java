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
    }
}
