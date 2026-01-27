package net.shyvv.ui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    // --------- Constants ---------
    // the maximum amount of time a Delay in queue can have in seconds (3m)
    static double DELAY_MAX_SECONDS = 180;

    // --------- Variables ---------
    // variable for storing the currently loaded song
    Song loadedSong = null;
    ObservableList<Song> songs = FXCollections.observableArrayList();
    // list for storing the currently queued songs
    public static List<Song> queuedSongs = new ArrayList<>();
    // ListView for showing the currently queued songs
    ListView<Song> listView = new ListView<>(songs);
    // super constructor variables storing the centerPanel and universal mediaPlayer objects
    CenterPanel centerPanel;
    MediaPlayer mediaPlayer;

    /**
     * this panel handles the backend of playing, pausing and controlling the songs in queue
     * @param primary primaryStage
     */
    public QueuePanel(PrimaryStage primary) {
        super(primary);
        this.centerPanel = primary.centerPanel;
        this.mediaPlayer = primary.mediaPlayer;
        initialize();
        initTicking();
    }

    // --------- Buttons ---------
    // button for adding the currently selected song into queue
    ShyvvButton addToQueue = new ShyvvButton("Add to Queue", e -> {
        // simply add this song into the list
        queuedSongs.add(new Song(MusicPanel.getSelectedSong()));
        // update the list
        updateList();
    });
    // button for adding the currently selected song into queue with a timestamp
    ShyvvButton addToQueueWithTimestamp = new ShyvvButton("Add to Queue - Timestamp", e -> {
        // call the function which controls queues with timestamp
        queueTimestampDialog(new Song(MusicPanel.getSelectedSong()));
    });
    // button for adding the currently selected song into queue with a delay
    ShyvvButton addToQueueWithDelay = new ShyvvButton("Add to Queue - Delay", e -> {
        // call the function which controls queues with delays beforehand
        queueDelayDialog(new Song(MusicPanel.getSelectedSong()));
    });

    /**
     * init function
     */
    private void initialize() {
        // create a VBox for storing the queue and ListView
        VBox queueSidebar = new VBox(new Label("Queue"), listView);
        // create an HBox for storing the different queue addition buttons
        HBox queueButtons = new HBox(10, addToQueue, addToQueueWithTimestamp, addToQueueWithDelay);
        // set the alignment for the queue buttons
        queueButtons.setAlignment(Pos.CENTER);

        // I UNDERSTAND CELL FACTORIES NOW
        // set the cell factory for the list view to show the songs queued, and the different states they're in
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
                    // create a base String to be concatenated
                    String queueDisplayString = "";
                    // make the song that's currently playing have a > symbol next to it
                    if(this.getIndex() == 0) {
                        queueDisplayString += "> ";
                    } else {
                        // make the song that's in queue have a number beside of it
                        queueDisplayString += this.getIndex()+". ";
                    }

                    // if the song is a delay, show the appropriate formatting in queue
                    if(song.isDelay()) {
                        queueDisplayString += "Delay: "+format(song.getDelayTime());
                    }else if(song.getStartTime().toSeconds() != 0) {
                        // if the song has a timestamp, show the appropriate formatting in queue
                        // in this case it's the timestamp to the right of the song name
                        queueDisplayString += song.getSongTitle();
                        queueDisplayString += " ("+format(song.getStartTime())+")";
                    } else {
                        // elsewise the song is simply in queue, just show its name
                        queueDisplayString += song.getSongTitle();
                    }

                    // set the text of the ListCell to the concatenated String
                    setText(queueDisplayString);
                }
            }
        });

        // set the padding for the listView
        listView.setPadding(new Insets(10));
        // add the queue buttons to the CENTER panel
        centerPanel.getPane().getChildren().add(queueButtons);

        // add the queue sidebar to THIS panel
        this.panel.getChildren().add(queueSidebar);
    }

    /**
     * updates the listView by clearing the listView and adding all the elements in queuedSongs to the listView (effectively synchronizing the two)
     */
    private void updateList() {
        songs.clear();
        songs.addAll(queuedSongs);
    }

    /**
     * loads the song in the arguements
     * @param song the song to be loaded by the universal mediaPlayer
     */
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
            timestamp.set(tempMediaPlayer.getTotalDuration().multiply(0.5));
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

        timestampDialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
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
                queuedSongs.add(new Song(song.getDirectory(), tempMediaPlayer.getTotalDuration().subtract(delay.get()), delay.get()));
                queuedSongs.add(new Song(song.getDirectory()));
                updateList();
                return delaySlider.getValue();
            }
            return null;
        });

        delayDialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
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
