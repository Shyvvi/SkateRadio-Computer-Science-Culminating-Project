package net.shyvv.core;

import net.shyvv.Main;

public interface Ticking {
    default void initTicking() {
        Main.beginTickingObject(this);
    }

    default void tickingError() {
        Main.stopTickingObject(this);
    }

    void tick();
}
