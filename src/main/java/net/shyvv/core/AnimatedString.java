package net.shyvv.core;

import java.awt.*;

public class AnimatedString implements Ticking {
    int tick = 0;
    int frameIndex = 0;
    int threshold;
    String[] textFrames;
    public AnimatedString(int threshold, String... args) {
        this.textFrames = args;
        this.threshold = threshold;
        initTicking();
    }

    public String getString() {
        return textFrames[frameIndex];
    }

    @Override
    public void tick() {
        tick++;
        if(tick >= threshold) {
            tick = 0;

            frameIndex++;
            if(frameIndex >= textFrames.length) {
                frameIndex = 0;
            }
        }
    }

    @Override
    public void tickThreadUI() {}
}
