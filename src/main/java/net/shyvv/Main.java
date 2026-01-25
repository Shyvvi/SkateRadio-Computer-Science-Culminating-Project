package net.shyvv;

import javafx.application.Application;

public class Main {
    // this is some JavaFX trickery because it was quite finicky to get working
    // some stackoverflow post said that splitting the Main classes into their own thing worked and it did for me
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}