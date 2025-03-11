package Client;

import se.mau.DA343A.VT25.projekt.Buffer;
import se.mau.DA343A.VT25.projekt.net.SecurityTokens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SmartPlugClient extends JFrame implements PropertyChangeListener {
    private JSlider powerSlider;
    private JLabel applianceLabel;
    private Buffer<Double> buffer;
    private String applianceName;
    private int maxPowerConsumption;
    private Socket socket;
    private DataOutputStream outputStream;
    private SecurityTokens securityTokens;
    private int xLocation;
    private int yLocation;

    public SmartPlugClient(String applianceName, int maxPowerConsumption, int xLocation, int yLocation) {
        this.applianceName = applianceName;
        this.maxPowerConsumption = maxPowerConsumption;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.buffer = new Buffer<>();
        this.securityTokens = new SecurityTokens("UPTeam");

        setupFrame();
        setupComponents();
        setVisible(true);

        connectToServer();
        new Thread(this::sendPowerConsumptionToServer).start();
    }

    private void setupFrame() {
        setTitle("Smart Plug - " + applianceName);
        setSize(200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(xLocation, yLocation);
    }

    private void setupComponents() {
        powerSlider = new JSlider(JSlider.VERTICAL, 0, maxPowerConsumption, 0);
        powerSlider.setPreferredSize(new Dimension(80, 400));
        powerSlider.setMajorTickSpacing(100);
        powerSlider.setMinorTickSpacing(100);
        powerSlider.setPaintTrack(true);
        powerSlider.setPaintTicks(true);
        powerSlider.setPaintLabels(true);
        powerSlider.setBorder(new EmptyBorder(0, 0, 20, 0));

        // changelistener to PropertyChangeListener as req 6
        powerSlider.addChangeListener(e -> {
            int value = powerSlider.getValue();
            powerSlider.firePropertyChange("value", -1, value);
        });

        powerSlider.addPropertyChangeListener("value", this);

        // Appliance Label
        applianceLabel = new JLabel(getFormattedLabel(0), SwingConstants.CENTER);
        applianceLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(powerSlider, BorderLayout.SOUTH);
        add(applianceLabel, BorderLayout.NORTH);
    }

    // changelistener to PropertyChangeListener as req 6
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("value".equals(evt.getPropertyName())) {
            int value = (int) evt.getNewValue();
            applianceLabel.setText(getFormattedLabel(value));
            buffer.put((double) value);
        }
    }

    private String getFormattedLabel(int value) {
        String color;
        if (value < 200) color = "blue";
        else if (value < 400) color = "green";
        else if (value < 700) color = "orange";
        else color = "red";

        return String.format("<html><div align='center'>%s<br>(W) usage: <font color='%s'>%d</font></div></html>", applianceName, color, value);
    }

    private void connectToServer() {
        while (true) {
            try {
                socket = new Socket("localhost", 8888);
                outputStream = new DataOutputStream(socket.getOutputStream());

                String token = securityTokens.generateToken();
                outputStream.writeUTF(token);
                System.out.println("Sending token: " + token);
                outputStream.writeUTF(applianceName);
                System.out.println("Sending appliance name: " + applianceName);
                outputStream.writeDouble(0.0);
                System.out.println("Sending Initial consumption ");
                outputStream.flush();
                System.out.println("Data sent!");
                break; // Exit the loop if connection is successful
            } catch (IOException e) {
                System.err.println("Connection failed: " + e.getMessage());
                System.err.println("Retrying in 5 seconds...");
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("Retry interrupted: " + ie.getMessage());
                }
            }
        }
    }

    private void sendPowerConsumptionToServer() {
        try {
            while (true) {
                double powerConsumption = buffer.get();
                outputStream.writeDouble(powerConsumption);
                outputStream.flush();
                System.out.println("Sending power consumption: " + powerConsumption + "W for " + applianceName);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
