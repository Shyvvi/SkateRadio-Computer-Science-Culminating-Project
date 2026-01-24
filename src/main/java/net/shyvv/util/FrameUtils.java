package net.shyvv.util;

import net.shyvv.core.Vec2d;

import javax.swing.*;
import java.awt.*;

public interface FrameUtils {
    default Dimension getRelativeLocation(JFrame frame, Vec2d location) {
        Vec2d percentLocation = new Vec2d(frame.getSize()).divide(100);
        return new Dimension((int)(percentLocation.getX() * location.getX()), (int)(percentLocation.getY() * location.getY()));
    }
}
