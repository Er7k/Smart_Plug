package Client;

import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private List<SmartPlugClient> clients;

    public ClientController() {
        clients = new ArrayList<>(); // lagrar alla klienter
        initializeClients();
    }

    // alla klienter för appliances enligt tabellen
    private void initializeClients() {
        clients.add(new SmartPlugClient("LED Light Bulb", 15));
        clients.add(new SmartPlugClient("Incandescent Bulb", 40));
        clients.add(new SmartPlugClient("Laptop", 30));
        clients.add(new SmartPlugClient("Desktop Computer", 200));
        clients.add(new SmartPlugClient("TV (LED)", 150));
        clients.add(new SmartPlugClient("Refrigerator", 250));
        clients.add(new SmartPlugClient("Microwave Oven", 1000));
    }
}