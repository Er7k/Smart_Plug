package Client;

import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private List<SmartPlugClient> clients;
    final private int xLocationStart = 1030;
    final private int xOffset = 192;
    final private int yLocationStart = 0;
    final private int yOffset = 500;

    public ClientController() {
        clients = new ArrayList<>(); // lagrar alla klienter
        initializeClients();
    }

    // alla klienter för appliances enligt tabellen
    private void initializeClients() {
        clients.add(new SmartPlugClient("LED Light Bulb", 15, xLocationStart, yLocationStart));
        clients.add(new SmartPlugClient("Incandescent Bulb", 40, xLocationStart + xOffset, yLocationStart));
        clients.add(new SmartPlugClient("Laptop", 30, xLocationStart + (xOffset * 2), yLocationStart));
        clients.add(new SmartPlugClient("Desktop Computer", 200, xLocationStart + (xOffset * 3), yLocationStart));
        clients.add(new SmartPlugClient("TV (LED)", 150, xLocationStart, yOffset));
        clients.add(new SmartPlugClient("Refrigerator", 250, xLocationStart + xOffset, yOffset));
        clients.add(new SmartPlugClient("Microwave Oven", 1000, xLocationStart + (xOffset * 2), yOffset));
    }
}