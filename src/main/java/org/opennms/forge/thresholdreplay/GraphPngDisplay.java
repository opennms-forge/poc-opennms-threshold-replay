package org.opennms.forge.thresholdreplay;

/**
 *
 * @author tak
 */
import java.awt.*;
import javax.swing.*;

public class GraphPngDisplay extends JFrame {

    public GraphPngDisplay(String argx) {
        if (argx == null) {
            argx = "a.png";
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 640);
        JPanel panel = new JPanel();
        //panel.setSize(500,640);
        panel.setBackground(Color.CYAN);
        ImageIcon icon = new ImageIcon(argx);
        JLabel label = new JLabel();
        label.setIcon(icon);
        panel.add(label);
        this.getContentPane().add(panel);
    }

    public static void main(String[] args) {
        new GraphPngDisplay(args.length == 0 ? null : args[0]).setVisible(true);
    }
}