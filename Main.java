package GPopEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("GPop Editor (Version 0.02)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1400, 800);

            // Hier können Sie Ihre Audiodatei laden und die Wellenform zeichnen
            // ...

            JPanel panel = new JPanel();
            panel.addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int notches = e.getWheelRotation();
                    double zoomFactor = (notches < 0) ? 0.9 : 1.1;

                    // Hier können Sie den Zoom Ihrer Wellenform anpassen
                    // ...
                }
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
