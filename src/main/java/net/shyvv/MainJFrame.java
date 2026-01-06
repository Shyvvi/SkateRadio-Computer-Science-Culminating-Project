package net.shyvv;

import javax.swing.*;
import java.awt.*;

public class MainJFrame extends JFrame {

    public static String DISPLAY_NAME = "SkateRadio";
    public static Color DEFAULT_BACKGROUND_COLOR = new Color(55, 75, 74);
    public static Color DEFAULT_PANEL_COLOR_PRIMARY = new Color(82, 103, 96);
    public static Color DEFAULT_PANEL_COLOR_SECONDARY = new Color(139, 139, 174);
    public static Color DEFAULT_TEXT_COLOR_PRIMARY = new Color(136, 217, 230);
    public static Color DEFAULT_TEXT_COLOR_SECONDARY = new Color(197, 255, 253);
    public MainJFrame() {
        super();
        initialize();
    }

    private void initialize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame(DISPLAY_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
        frame.setSize((int) screenSize.getWidth()/2, (int) screenSize.getHeight());
        frame.setVisible(true);
    }
}
