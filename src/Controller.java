import se.mau.DA343A.VT25.projekt.ServerGUI;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Controller { // commit test

    private ServerGUI gui;
    private List<ApplianceGUI> clients;
    final private int xLocationStart = 1030;
    final private int xOffset = 192;
    final private int yLocationStart = 0;
    final private int yOffset = 500;

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
        clients.add(new ApplianceGUI("LED Light Bulb", 15, xLocationStart, yLocationStart));
        clients.add(new ApplianceGUI("Incandescent Bulb", 40, xLocationStart + xOffset, yLocationStart));
        clients.add(new ApplianceGUI("Laptop", 30, xLocationStart + (xOffset * 2), yLocationStart));
        clients.add(new ApplianceGUI("Desktop Computer", 200, xLocationStart + (xOffset * 3), yLocationStart));
        clients.add(new ApplianceGUI("TV (LED)", 150, xLocationStart, yOffset));
        clients.add(new ApplianceGUI("Refrigerator", 250, xLocationStart + xOffset, yOffset));
        clients.add(new ApplianceGUI("Microwave Oven", 1000, xLocationStart + (xOffset * 2), yOffset));
    }

    private void updateServerGUI() {
        while (true) {
            int totalConsumption = 0;
            for (ApplianceGUI client : clients) {
                totalConsumption += client.getCurrentConsumption();
            }

            final int finalTotalConsumption = totalConsumption; // Fick nån "lambda" error om jag inte gjorde detta

            if(finalTotalConsumption <= 1000) {
                SwingUtilities.invokeLater(() -> {
                    gui.setTotalConsumption(finalTotalConsumption);
                });
                try {
                    Thread.sleep(1000); // 1 sekund frekvens för senaste consumption
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                SwingUtilities.invokeLater(() -> {
                    gui.setTotalConsumption(1000);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}