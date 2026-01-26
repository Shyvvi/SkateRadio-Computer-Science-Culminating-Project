package net.shyvv.core;

import com.sun.tools.javac.Main;
import net.shyvv.App;

public interface Ticking {
    default void initTicking() {
        App.beginTickingObject(this);
    }
    default void tickingError() {
        App.stopTickingObject(this);
    }

    void tick();
    void tickThreadUI();
}
