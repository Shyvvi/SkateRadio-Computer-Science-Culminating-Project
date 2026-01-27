package net.shyvv.ui.panels;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.shyvv.core.AnimatedString;
import net.shyvv.core.ShyvvButton;
import net.shyvv.core.ShyvvPanel;
import net.shyvv.core.Ticking;
import net.shyvv.ui.PrimaryStage;
import net.shyvv.util.StringUtils;


public class CenterPanel extends ShyvvPanel implements Ticking, StringUtils {
    // --------- Constants ---------
    public static String NULL_TIME = "00:00 / 00:00";
    public static String NO_FILE = "No File Loaded";
    AnimatedString PLAYING_TEXT = new AnimatedString(100,"Playing.", "Playing..", "Playing...");

    // --------- Variables ---------
    // sliders which control the playback manipulation and volume
    public Slider playbackSlider;
    public Slider volumeSlider;
    // labels which display the current playtime and the currently loaded song
    public Label playtimeLabel;
    public Label loadedSong;
    // whether the song is playing or not
    public static boolean isPlaying = false;
    // super constructor variable which simply stores the universal mediaPlayer
    MediaPlayer mediaPlayer;

    /**
     * the panel which controls playing, pausing, skipping, seeking, etc
     * @param stage primaryStage
     */
    public CenterPanel(PrimaryStage stage) {
        super(stage);
        this.mediaPlayer = stage.mediaPlayer;
        initialize();
        initTicking();
    }

    // --------- Buttons ---------
    // the play/pause button, the text String is null because that is controlled by the AnimatedString above
    ShyvvButton playButton = new ShyvvButton("", e -> {
        play();
    });
    // the skip song button
    ShyvvButton nextSongButton = new ShyvvButton("⏭", e -> {
        // sometimes the mediaPlayer likes to be null and that's never a good thing
        if(mediaPlayer != null) {
            stage.queuePanel.finishSong();
        }
    });
    // the previous song button (it really only goes to the beginning of the song because playback history isn't saved)
    ShyvvButton lastSongButton = new ShyvvButton("⏮", e -> {
        // sometimes the mediaPlayer likes to be null and that's never a good thing
        if(mediaPlayer != null) {
            mediaPlayer.seek(new Duration(0));
        }
    });

    /**
     * initializing function, I feel like these are pretty self-explanatory, these are always spaghetti code to some degree
     */
    private void initialize() {
        // set the playback slider and make sure it is disabled upon initializing the program
        // the playback slider should only be enabled while a song is playing
        playbackSlider = new Slider();
        playbackSlider.setDisable(true);

        // set the volume slider and then set it's preferred dimensions
        volumeSlider = new Slider(0, 1, 1);
        // I'm honestly not sure if this worked or not but I can change its visual height in CSS regardless
        volumeSlider.setPrefWidth(120);
        volumeSlider.setPrefHeight(20);

        // labels for the current playtime and loaded song, just set their values to the default ones upon init
        playtimeLabel = new Label(NULL_TIME);
        loadedSong = new Label(NO_FILE);

        // create a listener for whenever the slider is being dragged or clicked on
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // if the mediaPlayer is not null, set the volume of the song to the value of the slider
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });

        // create a listener for whenever the mouse is released on the playback slider
        playbackSlider.setOnMouseReleased(e -> {
            // if the mediaPlayer is not null and the song IS playing, set the current playtime to the value of the slider
            if (mediaPlayer != null && isPlaying) {
                mediaPlayer.seek(Duration.seconds(playbackSlider.getValue()));
            }
        });

        // put all the control buttons into the same HBox for organizational purposes
        HBox controls = new HBox(10, lastSongButton, playButton, nextSongButton);
        // bulk set the size of the buttons within the controls HBox, so I don't need to set each one manually
        for(Node node : controls.getChildren()) {
            // make sure to derive the object type from instanceof
            if(node instanceof ShyvvButton b) {
                b.setPrefWidth(140);
                b.setPrefHeight(60);
            }
        }

        // create an HBox for the volume shenanigans
        HBox volumeBox = new HBox(10, new Label("Volume"), volumeSlider);

        // set the alignment for everything within this panel (in this case everything should ideally be centered)
        panel.setAlignment(Pos.CENTER);
        // add all the different panel components for this panel
        panel.getChildren().addAll(controls, playbackSlider, playtimeLabel, loadedSong, volumeBox);
        // set the spacing between each component
        panel.setSpacing(30);

        // set the alignment for each HBox inside of this panel and make sure everything is centered
        // use the for loop again so I don't need to set everything manually
        for(Node node : panel.getChildren()) {
            if(node instanceof HBox h) {
                h.setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     * the function to be called whenever playing / pausing a song
     */
    public void play() {
        // make sure the silly mediaPlayer isn't null
        if(mediaPlayer != null) {
            // toggle the isPlaying boolean
            // IntelliJ Idea suggested this compressed form of an if else "switch"
            isPlaying = !isPlaying;
            if(isPlaying) {
                // enable the playback slider and play the song inside the mediaPlayer
                playbackSlider.setDisable(false);
                mediaPlayer.play();
            } else {
                // disable the playback slider and pause the song inside the mediaPlayer
                playbackSlider.setDisable(true);
                mediaPlayer.pause();
            }
        }
    }

    /**
     * the function to be called to only pause a song
     */
    public void pause() {
        // making sure the mediaPlayer is not null pause the song inside the mediaPlayer
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
        // change the isPlaying boolean
        isPlaying = false;
    }

    @Override
    public void tick() {}

    /**
     * use this ticking function as it is on the thread where JavaFX elements can be modified
     * refer to the core/Ticking for more info
     */
    @Override
    public void tickThreadUI() {
        // change the text for the play / pause button accordingly
        if(isPlaying) {
            // use the AnimatedString
            playButton.setText(PLAYING_TEXT.getString());
        } else {
            playButton.setText("Paused");
        }
    }
}
