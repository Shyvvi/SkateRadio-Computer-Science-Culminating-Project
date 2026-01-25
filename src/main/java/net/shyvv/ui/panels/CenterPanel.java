package net.shyvv.ui.panels;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.shyvv.core.ShyvvButton;
import net.shyvv.core.Song;
import net.shyvv.core.Ticking;
import net.shyvv.util.StringUtils;


public class CenterPanel implements Ticking, StringUtils {
    MediaPlayer mediaPlayer;
    Pane panel;
    Stage stage;
    public CenterPanel(Stage stage, MediaPlayer mediaPlayer, Pane panel) {
        this.mediaPlayer = mediaPlayer;
        this.panel = panel;
        this.stage = stage;
        initialize();
        initTicking();
    }

    private Slider seekSlider;
    private Slider volumeSlider;
    private Label timeLabel;
    private Label statusLabel;
    public static boolean isPlaying = false;

    /// Buttons
    ShyvvButton playButton = new ShyvvButton("Play", e -> {
        // IntelliJ Idea suggested this compressed form of an if else "switch"
        isPlaying = !isPlaying;
        mediaPlayer.play();
        statusLabel.setText("Playing");
    });
    ShyvvButton pauseButton = new ShyvvButton("Pause", e -> {
        mediaPlayer.pause();
        statusLabel.setText("Paused");
    });
    ShyvvButton stopButton = new ShyvvButton("Stop", e -> {
        mediaPlayer.stop();
        statusLabel.setText("Stopped");
    });

    private void initialize() {

        seekSlider = new Slider();
        seekSlider.setDisable(true);

        volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setPrefWidth(120);

        timeLabel = new Label("00:00 / 00:00");
        statusLabel = new Label("No file loaded");
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });
        seekSlider.setOnMouseReleased(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            }
        });

        HBox controls = new HBox(10, playButton, pauseButton, stopButton);
        HBox volumeBox = new HBox(10, new Label("Volume"), volumeSlider);

        panel.getChildren().addAll(controls, seekSlider, timeLabel, volumeBox, statusLabel);
    }

    private void updateTime(Duration current, Duration total) {
        timeLabel.setText(format(current) + " / " + format(total));
    }

    public void loadAudio(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }

        mediaPlayer = new MediaPlayer(song.getMedia());

        mediaPlayer.setOnReady(() -> {
            Duration total = mediaPlayer.getTotalDuration();
            seekSlider.setMax(total.toSeconds());
            seekSlider.setDisable(false);
            updateTime(Duration.ZERO, total);
            statusLabel.setText("Loaded: " + song.getSongTitle());
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!seekSlider.isValueChanging()) {
                seekSlider.setValue(newTime.toSeconds());
            }
            updateTime(newTime, mediaPlayer.getTotalDuration());
        });

        mediaPlayer.setOnEndOfMedia(() ->
                statusLabel.setText("Playback finished")
        );

        mediaPlayer.setVolume(volumeSlider.getValue());
    }

    @Override
    public void tick() {
        if(isPlaying) {
            playButton.setText("⏸");
        } else {
            playButton.setText("▶");
        }
    }
}
