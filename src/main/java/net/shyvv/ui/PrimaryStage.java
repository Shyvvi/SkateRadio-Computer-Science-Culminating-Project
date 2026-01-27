package net.shyvv.ui;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
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
import net.shyvv.ui.panels.QueuePanel;

import java.awt.*;

public class PrimaryStage {
    public MediaPlayer mediaPlayer;
    public CenterPanel centerPanel = new CenterPanel(this);
    public QueuePanel queuePanel = new QueuePanel(this);
    public MusicPanel musicPanel = new MusicPanel(this);
    Stage stage;
    public PrimaryStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        /// all of this stuff needs to go into their respective classes

        // SplitPane which will split all the other sections of the app into 3 panes
        SplitPane mainPane = new SplitPane();

        // ------------------ Build the app ------------------
        mainPane.getItems().addAll(musicPanel.getPane(), centerPanel.getPane(), queuePanel.getPane());

        centerPanel.getPane().setPadding(new Insets(10));
        musicPanel.getPane().setPadding(new Insets(10));
        queuePanel.getPane().setPadding(new Insets(10));

        mainPane.setDividerPositions(0.33, 0.66);

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        Scene scene = new Scene(mainPane, screenSize.getWidth()*0.9, screenSize.getHeight()*0.9);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());


        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/skateradio_icon.png")));
        stage.setTitle(App.DISPLAY_NAME);
        stage.setScene(scene);
        stage.show();
    }
}
