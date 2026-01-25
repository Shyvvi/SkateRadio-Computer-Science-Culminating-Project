package net.shyvv.ui;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import net.shyvv.App;
import net.shyvv.core.ShyvvButton;
import net.shyvv.ui.panels.CenterPanel;
import net.shyvv.ui.panels.MusicPanel;
import net.shyvv.util.FileManager;

public class PrimaryStage {
    public MediaPlayer mediaPlayer;
    Stage stage;
    public PrimaryStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        /// all of this stuff needs to go into their respective classes

        // SplitPane which will split all the other sections of the app into 3 panes
        SplitPane mainPane = new SplitPane();

        // ------------------ Center Panel (middle) ------------------

        VBox centerPanel = new VBox();
        new CenterPanel(stage, mediaPlayer, centerPanel);
        // ------------------ Music Panel (left) ------------------
        VBox musicPanel = new VBox();
        new MusicPanel(musicPanel);

        // ------------------ Queue Panel (right) ------------------
        VBox queuePanel = new VBox();

        // ------------------ Build the app ------------------
        mainPane.getItems().addAll(musicPanel, centerPanel, queuePanel);


        centerPanel.setPadding(new Insets(10));
        musicPanel.setPadding(new Insets(10));
        queuePanel.setPadding(new Insets(10));

        mainPane.setDividerPositions(0.25, 0.85);

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        Scene scene = new Scene(mainPane, screenSize.getWidth()*0.9, screenSize.getHeight()*0.9);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle(App.DISPLAY_NAME);
        stage.setScene(scene);
        stage.show();
    }
}
