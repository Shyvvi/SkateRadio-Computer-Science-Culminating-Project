package net.shyvv.core;

import net.shyvv.App;

/**
 * the Ticking interface is to be used to make instances of an object tick
 * it is used by simply implementing this class and then calling initTicking() inside the super constructor
 * (or anywhere else where the object is initialized)
 */
public interface Ticking {
    /**
     * initializes ticking for this object
     */
    default void initTicking() {
        App.beginTickingObject(this);
    }

    /**
     * function called when an object throws an exception while ticking
     * it is then exempted from ticking
     */
    default void tickingError() {
        App.stopTickingObject(this);
    }

    // --------- Ticking functions for the object ---------

    /**
     * !! this function should only contain BACKGROUND TASKS as it runs on a different thread from JavaFX elements
     * JavaFX elements cannot be referred to or modified through this thread
     */
    void tick();

    /**
     * !! this function should only contain affect JavaFX elements
     * JavaFX elements MUST be referred to or modified through this thread (otherwise an exception will be thrown)
     */
    void tickThreadUI();
}
