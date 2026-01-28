package net.shyvv.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.shyvv.App;
import net.shyvv.ui.panels.CenterPanel;
import net.shyvv.ui.panels.MusicPanel;
import net.shyvv.ui.panels.QueuePanel;

import java.awt.*;

public class PrimaryStage {
    // --------- Variables ---------
    // the universal mediaPlayer object
    public MediaPlayer mediaPlayer;
    // the 3 different primary panels used in the application
    public CenterPanel centerPanel = new CenterPanel(this);
    public MusicPanel musicPanel = new MusicPanel(this);
    public QueuePanel queuePanel = new QueuePanel(this);
    // stage object to be transferred from the super constructor
    Stage stage;

    /**
     * stage object which can be seen as sort of the "core" of the application
     * it is where everything is pieced together
     * @param stage stage from the initializing function
     */
    public PrimaryStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        // SplitPane which will split all the other sections of the app into 3 panes
        SplitPane mainPane = new SplitPane();
        // create the leftmost splitPane and set it to being vertical
        // this is on request of my coach as they said that they wanted these two boxes to be moveable
        SplitPane leftPanel = new SplitPane(queuePanel.getPane(), musicPanel.getPane());
        leftPanel.setOrientation(Orientation.VERTICAL);
        // ------------------ Build the app ------------------
        // put everything into the primary container which will be displayed on the application
        mainPane.getItems().addAll(leftPanel, centerPanel.getPane());

        // set the padding for the 3 panels (this could have been done elsewhere but oh well)
        centerPanel.getPane().setPadding(new Insets(10));
        musicPanel.getPane().setPadding(new Insets(10));
        queuePanel.getPane().setPadding(new Insets(10));

        // set the divider position for the moveable bar
        mainPane.setDividerPositions(0.4);

        // get the bounds of the screen and make it so whenever the application opens it uses 90% of the screen space on both x and y axis
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        Scene scene = new Scene(mainPane, screenSize.getWidth()*0.9, screenSize.getHeight()*0.9);
        // set the CSS of the application to the one provided in resources
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // set the icon of the application
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/skateradio_icon.png")));
        // set the title of the application
        stage.setTitle(App.DISPLAY_NAME);
        // set the scene of the application and bundle it all up together and show it to the user
        stage.setScene(scene);
        stage.show();
    }
}
