package net.shyvv.core;

import net.shyvv.util.FrameUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ShyvvPanel extends JPanel implements Ticking, FrameUtils {
    ShyvvPanel.Builder builder;
    // todo set functionality for this later
    List<Initializable> panelElements;
    Vec2d size;
    Vec2d location;
    JFrame frame;

    //
    public ShyvvPanel(JFrame jFrame, Vec2d size, Vec2d location, boolean ticks, ShyvvPanel.Builder builder) {
        super();
        this.frame = jFrame;
        this.size = size;
        this.location = location;
        this.builder = builder;
        initialize();
        if(ticks) {
            initTicking();
        }
    }

    protected ShyvvPanel(JFrame jFrame, Vec2d size, Vec2d location) {
        super();
        this.frame = jFrame;
        this.size = size;
        this.location = location;
        initialize();
    }

    /**
     * Create a builder system which iterates through an array to set different properties whilst staying in the same object
     *
     */
    private void initialize() {
        /// the following code controls all of the different possibilities and parameters with the builder
        if(builder.color != null) {
            setBackground(builder.color);
        } else {
            setBackground(Color.WHITE);
        }
        if(builder.image != null) {
            JLabel label;
            if(builder.imageSize != null) {
                Image image = builder.image.getImage();
                Image scaledImage = image.getScaledInstance(builder.imageSize.getIntX(), builder.imageSize.getIntX(), Image.SCALE_DEFAULT);
                label = new JLabel(new ImageIcon(scaledImage));
            } else {
                label = new JLabel(builder.image);
            }
            add(label);
        }

        updateSize();
        updateLocation();
        frame.add(this);
    }

    public void updateSize() {
        setSize(size.getDimension());
    }
    public void updateLocation() {
        setLocation((int) location.getX(), (int) location.getY());
    }

    public static class Builder {
        private static final List<Initializable> elements = new ArrayList<>();
        private Color color = null;
        private ImageIcon image = null;
        private Vec2d imageSize = null;

        public Builder addButton(Initializable spe) {
            elements.add(spe);
            return this;
        }

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder setImage(ImageIcon image, Vec2d imageSize) {
            this.image = image;
            this.imageSize = imageSize;
            return this;
        }
    }
}
