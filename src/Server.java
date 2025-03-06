import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocket;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Server extends ListeningSocket {

    public final static int port = 8888;
    private final ServerGUI gui;

    public static void main(String[] args) {
        Server server = new Server(port);
        new Thread(server).start();
    }

    public Server(int listeningPort) {
        super(listeningPort);
        gui = new ServerGUI("Server");
        startGUIOnNewThread();
    }

    @Override
    public ListeningSocketConnectionWorker createNewConnectionWorker() {
        return new ConnectionWorker(gui);
    }

    private void startGUIOnNewThread() {
        try {
            SwingUtilities.invokeAndWait(() -> gui.createAndShowUI());
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
