package Server;

import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import javax.swing.*;
import java.io.*;
import java.net.SocketAddress;

public class ApplianceConnectionWorker implements ListeningSocketConnectionWorker {

    private final ApplianceConsumptionModel model;
    private final SecurityTokens securityTokens;
    private final ServerGUI serverGUI;

    public ApplianceConnectionWorker(ApplianceConsumptionModel model, SecurityTokens securityTokens,
                                     ServerGUI serverGUI) {
        this.model = model;
        this.securityTokens = securityTokens;
        this.serverGUI = serverGUI;
    }

    @Override
    public void newConnection(SocketAddress socketAddress, DataInput in, DataOutput out) {
        try {
            System.out.println("trying to receive data from client");
            String token = in.readUTF();
            System.out.println("Received token: " + token);
            String applianceName = in.readUTF();
            System.out.println("Received appliance name: " + applianceName);
            double initialConsumption = in.readDouble();
            System.out.println("Received initial consumption: " + initialConsumption);

            // Validera token
            if (!securityTokens.verifyToken(token)) {
                System.err.println("Invalid token from client: " + socketAddress);
                return;
            }

            // Uppdatera modellen med initial förbrukning
            model.updateConsumption(applianceName, initialConsumption);
            SwingUtilities.invokeLater(() -> serverGUI.addLogMessage("Appliance " + applianceName + " consumption: " + initialConsumption + " W"));

            // Läs och uppdatera förbrukning i en loop
            while (true) {
                double consumption = in.readDouble();
                System.out.println(consumption);
                model.updateConsumption(applianceName, consumption);
                SwingUtilities.invokeLater(() -> serverGUI.addLogMessage("Updated " + applianceName + " consumption: " + consumption + " W"));
            }

        } catch (IOException e) {
            System.err.println("Client disconnected: " + socketAddress);
        }
    }
}
