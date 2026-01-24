package net.shyvv.ui;

import net.shyvv.core.ShyvvPanel;
import net.shyvv.core.Ticking;
import net.shyvv.core.Vec2d;
import net.shyvv.util.FrameUtils;
import net.shyvv.Main;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame implements Ticking, FrameUtils {

    public BaseFrame() {
        super();
        initialize();
        initTicking();
    }

    private void initialize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Main.DEFAULT_BACKGROUND_COLOR);
        setSize((int) screenSize.getWidth()/2, (int) screenSize.getHeight());
        setLayout(null);

        System.out.println(getWidth()+", "+getHeight());

        new BasePanel(this, new Vec2d(20, 100), new Vec2d(10, 10), true, new ShyvvPanel.Builder());
    }

    @Override
    public void tick() {
        setVisible(true);
        revalidate();
        repaint();
    }
}
