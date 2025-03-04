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
    private Buffer<Double> buffer; // lagrar datan för värdena
    private int maxPower; // alla Appliances har olika maxpower

    public ApplianceGUI(String title, int maxPower) {
        this.title = title;
        this.buffer = new Buffer<>();
        this.maxPower = maxPower;
        createAndShowApplianceGUI();
    }

    private void createAndShowApplianceGUI() {

        JFrame frame = new JFrame(title);
        frame.setSize(200, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(1100, 0);

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
        buffer.put((double) value); // Sätter in värdet
    }

    public int getCurrentConsumption() { // hämtar värdet för appliance's användning
        return slider.getValue();
    }
}
