import se.mau.DA343A.VT25.projekt.Buffer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
public class ApplianceGUI implements ChangeListener {

    private final String title;
    private JSlider slider;
    private JLabel label;
    private int maxPower; // alla Appliances har olika maxpower
    private int xLocation;
    private int yLocation;
    private Buffer<Double> buffer; // tillagd

    public ApplianceGUI(String title, int maxPower, int xLocation, int yLocation) {
        this.title = title;
        this.maxPower = maxPower;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.buffer = new Buffer<>();
        createAndShowApplianceGUI();
    }

    private void createAndShowApplianceGUI() {

        JFrame frame = new JFrame(title);
        frame.setSize(200, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(xLocation, yLocation);

        slider = new JSlider(JSlider.VERTICAL, 0, maxPower, 0);
        slider.setPreferredSize(new Dimension(80, 400));
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(100);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(100);
        slider.setBorder(new EmptyBorder(0, 0, 20, 0));
        slider.addChangeListener(this);

        label = new JLabel(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'blue'>" + slider.getValue() + "</div></html>"), SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(10, 0, 0, 0));

        frame.add(slider, BorderLayout.SOUTH);
        frame.add(label, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.repaint();
        frame.revalidate();
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        int value = slider.getValue();

        if (value < 200) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'blue'>" + slider.getValue() + "</div></html>"));
        } else if (value < 400) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'green'>" + slider.getValue() + "</div></html>"));
        } else if (value < 700) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'orange'>" + slider.getValue() + "</div></html>"));
        } else {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'red'>" + slider.getValue() + "</div></html>"));
        }
        buffer.put((double) value); // lägger till värden i buffern när användaren ändrar värden.
        System.out.println("Added value to buffer: " + value); // debug
    }

    public double getCurrentConsumption() { // hämtar värdet för appliance's användning
        return slider.getValue();
    }

    public Buffer<Double> getBuffer() {
        return buffer;
    }
}
