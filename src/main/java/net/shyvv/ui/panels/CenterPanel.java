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
    MediaPlayer mediaPlayer;
    public Slider playbackSlider;
    public Slider volumeSlider;
    public Label playtimeLabel;
    public Label loadedSong;
    public static boolean isPlaying = false;

    public static String NULL_TIME = "00:00 / 00:00";
    public static String NO_FILE = "No File Loaded";
    AnimatedString playingText = new AnimatedString(100,"Playing.", "Playing..", "Playing...");

    public CenterPanel(PrimaryStage stage) {
        super(stage);
        this.mediaPlayer = stage.mediaPlayer;
        initialize();
        initTicking();
    }

    /// Buttons
    ShyvvButton playButton = new ShyvvButton("Play", e -> {
        play();
    });

    ShyvvButton nextSongButton = new ShyvvButton("⏭", e -> {
        if(mediaPlayer != null) {
            stage.queuePanel.finishSong();
        }
    });
    ShyvvButton lastSongButton = new ShyvvButton("⏮", e -> {
        if(mediaPlayer != null) {
            mediaPlayer.seek(new Duration(0));
        }
    });

    private void initialize() {
        playbackSlider = new Slider();
        playbackSlider.setDisable(true);

        volumeSlider = new Slider(0, 1, 1);
        volumeSlider.setPrefWidth(120);
        volumeSlider.setPrefHeight(20);

        playtimeLabel = new Label(NULL_TIME);
        loadedSong = new Label(NO_FILE);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });

        playbackSlider.setOnMouseReleased(e -> {
            if (mediaPlayer != null && isPlaying) {
                mediaPlayer.seek(Duration.seconds(playbackSlider.getValue()));
            }
        });

        HBox controls = new HBox(10, lastSongButton, playButton, nextSongButton);
        HBox volumeBox = new HBox(10, new Label("Volume"), volumeSlider);
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(controls, playbackSlider, playtimeLabel, loadedSong, volumeBox);
        panel.setSpacing(30);

        for(Node node : panel.getChildren()) {
            if(node instanceof HBox h) {
                h.setAlignment(Pos.CENTER);
            }
        }
    }

    public void play() {
        if(mediaPlayer != null) {
            // IntelliJ Idea suggested this compressed form of an if else "switch"
            isPlaying = !isPlaying;
            if(isPlaying) {
                playbackSlider.setDisable(false);
                mediaPlayer.play();
            } else {
                playbackSlider.setDisable(true);
                mediaPlayer.pause();
            }
        }
    }

    public void pause() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
        }
        isPlaying = false;
    }

    @Override
    public void tick() {}

    @Override
    public void tickThreadUI() {
        if(isPlaying) {
            playButton.setText(playingText.getString());
        } else {
            playButton.setText("Paused");
        }
    }
}
