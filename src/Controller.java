import se.mau.DA343A.VT25.projekt.Buffer;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Controller { // commit test

    private Buffer<Double> buffer;
    private Client client;
    private List<ApplianceGUI> appliances;
    final private int xLocationStart = 1030;
    final private int xOffset = 192;
    final private int yLocationStart = 0;
    final private int yOffset = 500;


    public Controller() {

        appliances = new ArrayList<>(); // lagrar alla klienter
        buffer = new Buffer<>();
        client = new Client(buffer);
        Thread thread = new Thread(client);
        thread.start();
        initializeAppliances();
        getTotalConsumption();

    }

    // alla klienter för appliances enligt tabellen
    private void initializeAppliances() {

        appliances.add(new ApplianceGUI("LED Light Bulb", 15, xLocationStart, yLocationStart));
        appliances.add(new ApplianceGUI("Incandescent Bulb", 40, xLocationStart + xOffset, yLocationStart));
        appliances.add(new ApplianceGUI("Laptop", 30, xLocationStart + (xOffset * 2), yLocationStart));
        appliances.add(new ApplianceGUI("Desktop Computer", 200, xLocationStart + (xOffset * 3), yLocationStart));
        appliances.add(new ApplianceGUI("TV (LED)", 150, xLocationStart, yOffset));
        appliances.add(new ApplianceGUI("Refrigerator", 250, xLocationStart + xOffset, yOffset));
        appliances.add(new ApplianceGUI("Microwave Oven", 1000, xLocationStart + (xOffset * 2), yOffset));
    }

    private void getTotalConsumption() {

        while (true) {
            int totalConsumption = 0;
            for (ApplianceGUI client : appliances) {
                totalConsumption += client.getCurrentConsumption();
            }
            System.out.println("Total consumption: " + totalConsumption); // debug - stämmer överens med buffern

            buffer.put((double) totalConsumption);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}