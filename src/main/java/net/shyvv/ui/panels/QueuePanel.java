package net.shyvv.ui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.shyvv.core.ShyvvButton;
import net.shyvv.core.ShyvvPanel;
import net.shyvv.core.Song;
import net.shyvv.core.Ticking;
import net.shyvv.ui.PrimaryStage;
import net.shyvv.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class QueuePanel extends ShyvvPanel implements Ticking, StringUtils {
    CenterPanel centerPanel;
    MediaPlayer mediaPlayer;
    Song loadedSong = null;
    static double DELAY_MAX_SECONDS = 180;
    public static List<Song> queuedSongs = new ArrayList<>();
    ObservableList<Song> songs = FXCollections.observableArrayList();
    ListView<Song> listView = new ListView<>(songs);
    public QueuePanel(PrimaryStage primary) {
        super(primary);
        this.centerPanel = primary.centerPanel;
        this.mediaPlayer = primary.mediaPlayer;
        initialize();
        initTicking();
    }

    ShyvvButton addToQueue = new ShyvvButton("Add to Queue", e -> {
        queuedSongs.add(new Song(MusicPanel.getSelectedSong()));
        updateList();
    });
    ShyvvButton addToQueueWithTimestamp = new ShyvvButton("Add to Queue - Timestamp", e -> {
        queueTimestampDialog(new Song(MusicPanel.getSelectedSong()));
    });
    ShyvvButton addToQueueWithDelay = new ShyvvButton("Add to Queue - Delay", e -> {
        queueDelayDialog(new Song(MusicPanel.getSelectedSong()));
    });

    private void initialize() {
        VBox queueSidebar = new VBox(new Label("Queue"), listView);
        HBox queueButtons = new HBox(addToQueue, addToQueueWithTimestamp, addToQueueWithDelay);

        // I sort of understand how these cell factories work now, big emphasis on sort of
        // I see it as an overcomplicated array iterator
        listView.setCellFactory(lv -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song song, boolean empty) {
                super.updateItem(song, empty);
                // safe check to make sure that the list cell instance is not null or empty
                if (empty || song == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // this.getIndex() is actually a lifesaver function oh my god :pray:
                    // set the value which the listView will display for this instance of the song object
                    String queueDisplayString = "";
                    if(this.getIndex() == 0) {
                        queueDisplayString += "> ";
                    } else {
                        queueDisplayString += this.getIndex()+". ";
                    }

                    if(song.isDelay()) {
                        queueDisplayString += "Delay: "+format(song.getDelayTime());
                    }else if(song.getStartTime().toSeconds() != 0) {
                        queueDisplayString += song.getSongTitle();
                        queueDisplayString += " ("+format(song.getStartTime())+")";
                    } else {
                        queueDisplayString += song.getSongTitle();
                    }

                    setText(queueDisplayString);
                }
            }
        });

        listView.setPadding(new Insets(10));
        centerPanel.getPane().getChildren().add(queueButtons);

        this.panel.getChildren().add(queueSidebar);
    }

    private void updateList() {
        songs.clear();
        songs.addAll(queuedSongs);
    }

    public void loadSong(Song song) {
        loadedSong = song;
        // dispose of the old mediaPlayer to prevent issues
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        // assign a new mediaPlayer to the mediaPlayer variable
        mediaPlayer = new MediaPlayer(song.getMedia());

        // once the file as LOADED, get all the other UI elements ready
        mediaPlayer.setOnReady(() -> {
            // set the slider configurations
            Duration total = mediaPlayer.getTotalDuration();
            centerPanel.playbackSlider.setMax(total.toSeconds());

            // init the playtime display for the playtimeLabel
            centerPanel.playtimeLabel.setText(getPlaytimeDisplay(Duration.ZERO, total));

            // load the song respectively
            centerPanel.loadedSong.setText("Loaded: " + song.getSongTitle());

            mediaPlayer.seek(song.getStartTime());
        });

        // some more JavaFX listener magic, not really sure how it works, but it does
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!centerPanel.playbackSlider.isValueChanging() && !centerPanel.playbackSlider.isPressed()) {
                centerPanel.playbackSlider.setValue(newTime.toSeconds());
            }
        });

        mediaPlayer.setOnEndOfMedia(this::finishSong);

        mediaPlayer.setVolume(centerPanel.volumeSlider.getValue());
    }

    public void queueTimestampDialog(Song song) {
        Dialog<Double> timestampDialog = new Dialog<>();
        timestampDialog.setTitle("Set Queue Timestamp");
        // IntelliJ made it accessible so it was accessible within the lambda expression listener thingymajig
        AtomicReference<Duration> timestamp = new AtomicReference<>();
        Slider timestampSlider = new Slider(0, 1, 0.5);
        Label songNameLabel = new Label(song.getSongTitle());
        Label timeValueLabel = new Label("--- / ---");

        VBox content = new VBox(10, timestampSlider, songNameLabel, timeValueLabel);
        content.setPadding(new Insets(10));

        timestampDialog.getDialogPane().setContent(content);
        timestampDialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );
        timestampDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        MediaPlayer tempMediaPlayer = new MediaPlayer(song.getMedia());
        tempMediaPlayer.setOnReady(() -> {
            queueTimestampText(timeValueLabel, tempMediaPlayer, 0.5);
            timestampSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                timestamp.set(tempMediaPlayer.getTotalDuration().multiply(newVal.doubleValue()));
                queueTimestampText(timeValueLabel, tempMediaPlayer, newVal.doubleValue());
            });
            timestampDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        });


        timestampDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                queuedSongs.add(new Song(song.getDirectory(), timestamp.get()));
                updateList();
                return timestampSlider.getValue();
            }
            return null;
        });

        timestampDialog.show();
    }
    public void queueTimestampText(Label label, MediaPlayer tempMediaPlayer, double sliderValue) {
        Duration selectedTime = tempMediaPlayer.getTotalDuration().multiply(sliderValue);
        label.setText(format(selectedTime) + " / " + format(tempMediaPlayer.getTotalDuration()));
    }

    public void queueDelayDialog(Song song) {
        Dialog<Double> delayDialog = new Dialog<>();
        delayDialog.setTitle("Set Playtime Delay");
        // IntelliJ made it an AtomicReference so it was accessible within the lambda expression listener thingymajig
        AtomicReference<Duration> delay = new AtomicReference<>(new Duration(DELAY_MAX_SECONDS*1000/6));
        Slider delaySlider = new Slider(0, DELAY_MAX_SECONDS*1000 , DELAY_MAX_SECONDS*1000/6);

        Label timeValueLabel = new Label("Delay: 0:30");

        VBox content = new VBox(10, delaySlider, timeValueLabel);
        content.setPadding(new Insets(10));

        delayDialog.getDialogPane().setContent(content);
        delayDialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );
        delayDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        MediaPlayer tempMediaPlayer = new MediaPlayer(song.getMedia());
        tempMediaPlayer.setOnReady(() -> {
            delaySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                delay.set(new Duration(newVal.doubleValue()));
                delayDisplayText(timeValueLabel, newVal.doubleValue());
            });
            delayDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        });

        delayDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                queuedSongs.add(new Song(song.getDirectory(), tempMediaPlayer.getTotalDuration().subtract(delay.get()), delay.get(), true));
                queuedSongs.add(new Song(song.getDirectory()));
                updateList();
                return delaySlider.getValue();
            }
            return null;
        });

        delayDialog.show();
    }
    public void delayDisplayText(Label label, double sliderValue) {
        Duration selectedTime = new Duration(sliderValue);
        label.setText("Delay: " + format(selectedTime));
    }

    public void finishSong() {
        if(queuedSongs.toArray().length == 1) {
            centerPanel.loadedSong.setText("Playback Finished");
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            centerPanel.pause();
            queuedSongs.clear();
            centerPanel.playbackSlider.setDisable(true);
            updateList();
        } else {
            queuedSongs.removeFirst();
            loadSong(queuedSongs.getFirst());
            mediaPlayer.play();
            updateList();
        }
    }

    public String getPlaytimeDisplay(Duration current, Duration total) {
        return format(current) + " / " + format(total);
    }

    @Override
    public void tick() {
        if(!queuedSongs.isEmpty() && loadedSong != queuedSongs.getFirst()) {
            loadSong(queuedSongs.getFirst());
        }
    }

    @Override
    public void tickThreadUI() {
        centerPanel.mediaPlayer = this.mediaPlayer;
        if(mediaPlayer != null) {
            centerPanel.playtimeLabel.setText(getPlaytimeDisplay(mediaPlayer.getCurrentTime(), mediaPlayer.getTotalDuration()));
        } else {
            centerPanel.playtimeLabel.setText(CenterPanel.NULL_TIME);
        }
    }
}
