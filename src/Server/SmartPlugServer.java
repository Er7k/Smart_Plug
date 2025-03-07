package Server;

import se.mau.DA343A.VT25.projekt.ServerGUI;
import se.mau.DA343A.VT25.projekt.net.ListeningSocket;
import se.mau.DA343A.VT25.projekt.net.ListeningSocketConnectionWorker;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class SmartPlugServer {
    private final ApplianceConsumptionModel model = new ApplianceConsumptionModel();
    private final SecurityTokens securityTokens = new SecurityTokens("UPTeam");
    private final ServerGUI serverGUI;

    public SmartPlugServer(){
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
                return new ApplianceConnectionWorker(model, securityTokens);
            }
        };
        new Thread(listeningSocket).start();

        // Uppdatera GUI:t med förbrukningsvärden varje sekund
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    //System.out.println(model.getTotalConsumption());
                    // Uppdatera total förbrukning i GUI:t
                    serverGUI.setTotalConsumption(model.getTotalConsumption());
                });
            }
        }, 0, 1000); // Uppdatera varje sekund

    }

    public static void main(String[] args) {
        new SmartPlugServer();
    }
}
