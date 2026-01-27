package net.shyvv.core;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class ShyvvButton extends Button {
    /**
     * extension of the JavaFX Button class which simply creates button with a predetermined function, useful for initialization
     * @param text the text to be displayed within the button
     * @param eventHandler the event which will happen when the button is clicked
     */
    public ShyvvButton(String text, EventHandler<ActionEvent> eventHandler) {
        super(text);
        setOnAction(eventHandler);
    }
}
