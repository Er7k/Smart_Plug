package Client;

import se.mau.DA343A.VT25.projekt.Buffer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SmartPlugClient extends JFrame implements ChangeListener {
    private JSlider powerSlider;
    private JLabel applianceLabel;
    private Buffer<Double> buffer;
    private String applianceName;
    private int maxPowerConsumption;
    private Socket socket;
    private ObjectOutputStream outputStream;

    public SmartPlugClient(String applianceName, int maxPowerConsumption) {
        this.applianceName = applianceName;
        this.maxPowerConsumption = maxPowerConsumption;
        this.buffer = new Buffer<>();

        setupFrame();
        setupComponents();
        setVisible(true);
        //repaint();
        //revalidate();

        connectToServer();
        new Thread(this::sendPowerConsumptionToServer).start();
    }

    private void setupFrame() {
        setTitle("Smart Plug - " + applianceName);
        setSize(200, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocation(1100, 0);
    }

    private void setupComponents() {
        // Power Slider
        powerSlider = new JSlider(JSlider.VERTICAL, 0, maxPowerConsumption, 0);
        powerSlider.setPreferredSize(new Dimension(80, 400));
        powerSlider.setMajorTickSpacing(100);
        powerSlider.setMinorTickSpacing(100);
        powerSlider.setPaintTrack(true);
        powerSlider.setPaintTicks(true);
        powerSlider.setPaintLabels(true);
        powerSlider.setBorder(new EmptyBorder(0, 0, 20, 0));
        powerSlider.addChangeListener(this);

        // Appliance Label
        applianceLabel = new JLabel(getFormattedLabel(0), SwingConstants.CENTER);
        applianceLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(powerSlider, BorderLayout.SOUTH);
        add(applianceLabel, BorderLayout.NORTH);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int value = powerSlider.getValue();
        applianceLabel.setText(getFormattedLabel(value));
        buffer.put((double) value);
    }

    private String getFormattedLabel(int value) {
        String color;
        if (value < 200) color = "blue";
        else if (value < 400) color = "green";
        else if (value < 700) color = "orange";
        else color = "red";

        return String.format("<html><div align='center'>%s<br>(W) usage: <font color='%s'>%d</font></div></html>", applianceName, color, value);
    }

    // TCP communication to send power consumption to the server
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345); // Just for test, need to be replaced with server IP and port
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Send initial data (security token, appliance name, initial consumption)
            outputStream.writeObject("SECURITY_TOKEN"); // Need to be replaced with actual token
            outputStream.writeObject(applianceName);
            outputStream.writeInt(0); // Initial consumption
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPowerConsumptionToServer() {
        try {
            while (true) {
                double powerConsumption = buffer.get(); // Block until data is available
                outputStream.writeDouble(powerConsumption);
                outputStream.flush();
                //System.out.println("Sending power consumption: " + powerConsumption + "W for " + applianceName);
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

    public static void main(String[] args) {
        new SmartPlugClient("LED Light Bulb", 1000);
    }
}
