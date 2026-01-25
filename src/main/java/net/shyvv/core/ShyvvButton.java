package net.shyvv.core;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class ShyvvButton extends Button {
    public ShyvvButton(String text, EventHandler<ActionEvent> eventHandler) {
        super(text);
        setOnAction(eventHandler);
    }
}
