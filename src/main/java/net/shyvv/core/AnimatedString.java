package net.shyvv.core;

import java.awt.*;


public class AnimatedString implements Ticking {
    // tick keeps track of the ticking
    int tick = 0;
    // which frame the AnimatedString is on
    int frameIndex = 0;

    // super constructor variables
    int threshold;
    String[] textFrames;

    /**
     * AnimatedString creates strings which change their return value over time
     * @param threshold the amount of time in ms before the string changes
     * @param args the different "frames" for the AnimatedString
     */
    public AnimatedString(int threshold, String... args) {
        this.textFrames = args;
        this.threshold = threshold;
        initTicking();
    }

    /**
     * gets the CURRENT String value of the AnimatedString instance
     * @return String value with current frameIndex
     */
    public String getString() {
        return textFrames[frameIndex];
    }

    @Override
    public void tick() {
        // increment the tick and check for whether it is at or past the threshold
        tick++;
        if(tick >= threshold) {
            // reset the tick value
            tick = 0;

            // increment the frame and if the frameIndex has surpassed the length of the textFrames array, reset the animation
            frameIndex++;
            if(frameIndex >= textFrames.length) {
                frameIndex = 0;
            }
        }
    }

    @Override
    public void tickThreadUI() {}
}
