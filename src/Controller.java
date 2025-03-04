import se.mau.DA343A.VT25.projekt.ServerGUI;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Controller {

    ServerGUI gui;
    ApplianceGUI applianceGUI;

    public Controller() {
        gui = new ServerGUI("Server");
        startGUIOnNewThread();
        applianceGUI = new ApplianceGUI("Computer", 1000, 0);
    }

    private void startGUIOnNewThread() {
        try {
            SwingUtilities.invokeAndWait(() -> gui.createAndShowUI());
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
