package Server;

import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import java.io.*;
import java.net.SocketAddress;

public class ApplianceConnectionWorker implements ListeningSocketConnectionWorker {

    private final ApplianceConsumptionModel model;
    private final SecurityTokens securityTokens;

    public ApplianceConnectionWorker(ApplianceConsumptionModel model, SecurityTokens securityTokens) {
        this.model = model;
        this.securityTokens = securityTokens;
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

            // Läs och uppdatera förbrukning i en loop
            while (true) {
                double consumption = in.readDouble();
                System.out.println(consumption);
                model.updateConsumption(applianceName, consumption);
            }

        } catch (IOException e) {
            System.err.println("Client disconnected: " + socketAddress);
        }
    }
}
