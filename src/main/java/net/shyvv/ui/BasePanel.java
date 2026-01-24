package net.shyvv.ui;

import net.shyvv.core.ShyvvPanel;
import net.shyvv.core.Vec2d;

import javax.swing.*;

public class BasePanel extends ShyvvPanel  {
    public BasePanel(JFrame jFrame, Vec2d size, Vec2d location, boolean ticks, Builder builder) {
        super(jFrame, size, location, ticks, builder);
    }

    @Override
    public void tick() {

    }
}
