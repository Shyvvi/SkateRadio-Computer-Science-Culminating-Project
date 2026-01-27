package net.shyvv.core;

import javafx.scene.layout.VBox;
import net.shyvv.ui.PrimaryStage;

public abstract class ShyvvPanel {
    // super constructor variables
    public PrimaryStage stage;
    public VBox panel = new VBox();

    /**
     * ShyvvPanel is used by the 3 primary panels: CenterPanel, MusicPanel and QueuePanel
     * provides universal variables between the three panels which eases and bridges access of public variables between each panel
     * @param primaryStage just the PrimaryStage
     */
    public ShyvvPanel(PrimaryStage primaryStage) {
        this.stage = primaryStage;
    }

    /**
     * returns the VBox which stores all the content within this panel
     * @return JavaFX pane
     */
    public VBox getPane() {
        return panel;
    }
}
