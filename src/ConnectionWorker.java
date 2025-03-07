import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;

import javax.swing.*;
import java.io.*;
import java.net.SocketAddress;

public class ConnectionWorker implements ListeningSocketConnectionWorker {

    private final ServerGUI serverGUI;

    public ConnectionWorker(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    @Override
    public void newConnection(SocketAddress socketAddress, DataInput dataInput, DataOutput dataOutput) {
        try {
            while (true) {
                int receivedWattUsage = dataInput.readInt();
                System.out.println("Received watt usage: " + receivedWattUsage);
                SwingUtilities.invokeLater(() -> serverGUI.setTotalConsumption(receivedWattUsage));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}