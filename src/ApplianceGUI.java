import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ApplianceGUI implements ChangeListener {

    private final String title;
    private JSlider slider;
    private JLabel label;
    private final double maxWATT;
    private final double minWATT;

    public ApplianceGUI(String title, double maxWATT, double minWATT) {
        this.title = title;
        this.maxWATT = maxWATT;
        this.minWATT = minWATT;
        createAndShowApplianceGUI();
    }

    private void createAndShowApplianceGUI() {

        JFrame frame = new JFrame(title);
        frame.setSize(200, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocation(1100,0);

        slider = new JSlider(JSlider.VERTICAL, (int)minWATT, (int)maxWATT, 0);
        slider.setPreferredSize(new Dimension(80, 400));
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(100);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(100);
        slider.setBorder(new EmptyBorder(0,0,20,0));
        slider.addChangeListener(this);

        label = new JLabel(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'blue'>" + slider.getValue() + "</div></html>"), SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(10,0,0,0));

        frame.add(slider, BorderLayout.SOUTH);
        frame.add(label, BorderLayout.NORTH);
        frame.setVisible(true);
        frame.repaint();
        frame.revalidate();
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        if(slider.getValue() < 200) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'blue'>" + slider.getValue() + "</div></html>"));
        }
        else if(slider.getValue() < 400) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'green'>" + slider.getValue() + "</div></html>"));
        }
        else if(slider.getValue() < 700) {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'orange'>" + slider.getValue() + "</div></html>"));
        }
        else {
            label.setText(("<html><div align='center'>" + title + "<br>(W) usage: <font color = 'red'>" + slider.getValue() + "</div></html>"));
        }
    }
}
