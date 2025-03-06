import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;

import javax.swing.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.SocketAddress;

public class ConnectionWorker implements ListeningSocketConnectionWorker {

    ServerGUI serverGUI;

    public ConnectionWorker(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    @Override
    public void newConnection(SocketAddress socketAddress, DataInput dataInput, DataOutput dataOutput) {

        try {
            while (true) {// håll anslutning öppen
                double receivedWattUsage = dataInput.readDouble(); // int --> double.
                System.out.println("Received watt usage: " + receivedWattUsage); // debug - Stämmer ej? recieved watt usage är inte överens med totalConsumption
                serverGUI.setTotalConsumption(receivedWattUsage);

                dataOutput.writeUTF("Server: Current (W) Usage: " + receivedWattUsage);
                break;
            }

        } catch (IOException e) {
            System.out.println("Client disconnected: " + socketAddress);
            e.printStackTrace();
        }
    }
}

