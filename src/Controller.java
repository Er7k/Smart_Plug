import se.mau.DA343A.VT25.projekt.ServerGUI;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ServerGUI gui;
    private List<ApplianceGUI> clients;

    public Controller() {
        gui = new ServerGUI("Server");
        startGUIOnNewThread();
        clients = new ArrayList<>(); // lagrar alla klienter
        initializeClients();
        updateServerGUI();
    }

    private void startGUIOnNewThread() {
        try {
            SwingUtilities.invokeAndWait(() -> gui.createAndShowUI());
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // alla klienter för appliances enligt tabellen
    private void initializeClients() {
        clients.add(new ApplianceGUI("LED Light Bulb", 15));
        clients.add(new ApplianceGUI("Incandescent Bulb", 40));
        clients.add(new ApplianceGUI("Laptop", 30));
        clients.add(new ApplianceGUI("Desktop Computer", 200));
        clients.add(new ApplianceGUI("TV (LED)", 150));
        clients.add(new ApplianceGUI("Refrigerator", 250));
        clients.add(new ApplianceGUI("Microwave Oven", 1000));
    }

    private void updateServerGUI() {
        while (true) {
            int totalConsumption = 0;
            for (ApplianceGUI client : clients) {
                totalConsumption += client.getCurrentConsumption();
            }

            final int finalTotalConsumption = totalConsumption; // Fick nån "lambda" error om jag inte gjorde detta
            SwingUtilities.invokeLater(() -> {
                gui.setTotalConsumption(finalTotalConsumption);
            });
            try {
                Thread.sleep(1000); // 1 sekund frekvens för senaste consumption
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}