package Server;

import se.mau.DA343A.VT25.projekt.LiveXYSeries;
import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocket;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class SmartPlugServer {
    private final ApplianceConsumptionModel model = new ApplianceConsumptionModel();
    private final SecurityTokens securityTokens = new SecurityTokens("UPTeam");
    private final ServerGUI serverGUI;
    private final LinkedList<SimpleEntry<Long, Double>> history = new LinkedList<>();
    private final LiveXYSeries<Double> liveXYSeries = new LiveXYSeries<>("Energy Consumption", 20000);

    public SmartPlugServer() {
        // Skapa och visa GUI:t
        serverGUI = new ServerGUI("Smart Plug Server");
        try {
            SwingUtilities.invokeAndWait(serverGUI::createAndShowUI);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Starta lyssnande socket
        int port = 8888;
        ListeningSocket listeningSocket = new ListeningSocket(port) {
            @Override
            public ListeningSocketConnectionWorker createNewConnectionWorker() {
                return new ApplianceConnectionWorker(model, securityTokens, serverGUI);
            }
        };
        new Thread(listeningSocket).start();


        // Uppdatera GUI:t med förbrukningsvärden varje sekund
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    long unixTime = System.currentTimeMillis() / 1000; // Unix time in whole seconds
                    double totalConsumption = model.getTotalConsumption() / 1000;

                    // Add new data to history
                    history.add(new SimpleEntry<>(unixTime, totalConsumption));

                    // Remove entries older than 20 seconds
                    history.removeIf(entry -> (unixTime - entry.getKey()) > 20);

                    // Update total consumption in the GUI
                    serverGUI.setTotalConsumption(model.getTotalConsumption());
                    updateHistoryChart();
                });
            }
        }, 0, 1000); // Update every second
    }

    private void updateHistoryChart() {
        // Clear the series only when starting fresh or if needed

        // Add historical data to the series (this will show the last 20 seconds)
        for (SimpleEntry<Long, Double> entry : history) {
            liveXYSeries.addValue(entry.getKey().doubleValue(), entry.getValue());
        }

        // Add the series to the GUI
        serverGUI.addSeries(liveXYSeries);
    }


    public static void main(String[] args) {
        new SmartPlugServer();
    }
}
