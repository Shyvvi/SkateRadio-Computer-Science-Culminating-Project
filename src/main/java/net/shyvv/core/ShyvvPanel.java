package net.shyvv.core;

import javafx.scene.layout.VBox;
import net.shyvv.ui.PrimaryStage;

public abstract class ShyvvPanel {
    public PrimaryStage stage;
    public VBox panel = new VBox();
    public ShyvvPanel(PrimaryStage primaryStage) {
        this.stage = primaryStage;
    }

    public VBox getPane() {
        return panel;
    }
}
