import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;

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

            int receivedWattUsage = dataInput.readInt();
            System.out.println("Received watt usage: " + receivedWattUsage);
            serverGUI.setTotalConsumption(receivedWattUsage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            dataOutput.writeUTF("Server: Current (W) Usage: " + receivedWattUsage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
