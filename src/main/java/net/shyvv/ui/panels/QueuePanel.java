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

    // button for which moves a song in queue up
    ShyvvButton moveQueueUp = new ShyvvButton("Move Queue - Up", e -> {
        moveSong(-1);
    });

    // button for which moves a song in queue down
    ShyvvButton moveQueueDown = new ShyvvButton("Move Queue - Down", e -> {
        moveSong(1);
    });

    /**
     * init function
     */
    private void initialize() {
        // create a VBox for storing the queue and ListView
        VBox queueSidebar = new VBox(new Label("Queue"), listView);
        // create an HBox for storing the different queue addition buttons
        HBox queueButtons = new HBox(10, moveQueueUp, moveQueueDown);
        // set the alignment for the queue buttons
        queueButtons.setAlignment(Pos.CENTER);
        // create an HBox for storing the different queue movement buttons
        HBox queueMovementButtons = new HBox(10, addToQueue, addToQueueWithTimestamp, addToQueueWithDelay);
        // set the alignment for the queue movement buttons
        queueMovementButtons.setAlignment(Pos.CENTER);

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

        // set the height of the queue panel
        listView.setPrefHeight(1000);
        // set the padding for the listView
        listView.setPadding(new Insets(10));
        // add the queue buttons to the CENTER panel
        centerPanel.getPane().getChildren().add(queueButtons);
        // add the queue movement buttons too
        centerPanel.getPane().getChildren().add(queueMovementButtons);

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
            // if the song has a timestamp, set the appropriate starting time
            mediaPlayer.seek(song.getStartTime());
        });

        // some more JavaFX listener magic, not really sure how it works, but it does; thanks stackoverflow
        // apologies for spaghetti code but this controls the playback slider being set to the value of the song
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            // make sure that the playback slider isn't being dragged or having its value changed
            if (!centerPanel.playbackSlider.isValueChanging() && !centerPanel.playbackSlider.isPressed()) {
                // set the value of the playback slider to the duration of the song
                centerPanel.playbackSlider.setValue(newTime.toSeconds());
            }
        });

        // method reference for calling finishSong() when a song finishes playing
        mediaPlayer.setOnEndOfMedia(this::finishSong);

        if(song.isDelay() && song.isSilenced()) {
            mediaPlayer.setVolume(0);
        } else {
            mediaPlayer.setVolume(centerPanel.volumeSlider.getValue());
        }

    }

    /**
     * handles the dialog which appears whenever the queue - timestamp button is pressed
     * @param song the song which is to be put in queue with timestamp
     */
    public void queueTimestampDialog(Song song) {
        // create the dialog and set the title
        Dialog<Double> timestampDialog = new Dialog<>();
        timestampDialog.setTitle("Set Queue Timestamp");
        // IntelliJ made it accessible so it was accessible within the lambda expression listener thingymajig
        AtomicReference<Duration> timestamp = new AtomicReference<>();
        // create the timestamp slider, song and time value labels for UI
        Slider timestampSlider = new Slider(0, 1, 0.5);
        Label songNameLabel = new Label(song.getSongTitle());
        Label timeValueLabel = new Label("--- / ---");

        // store all of these into the same VBox, and add padding to the elements
        VBox content = new VBox(10, timestampSlider, songNameLabel, timeValueLabel);
        content.setPadding(new Insets(10));

        // set the content of the dialog box to the content of the VBox
        timestampDialog.getDialogPane().setContent(content);
        // create the OK and CANCEL buttons
        timestampDialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );
        // set the OK button to be disabled, it is re-enabled after the song has been loaded
        // this is to prevent unnecessary exceptions as the queue functions are pretty sensitive
        timestampDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        // create a temporary media player to get information about the song
        MediaPlayer tempMediaPlayer = new MediaPlayer(song.getMedia());
        // create a listener for when the mediaPlayer loads
        tempMediaPlayer.setOnReady(() -> {
            // set the default value of the timestamp (50% of the song's total duration)
            timestamp.set(tempMediaPlayer.getTotalDuration().multiply(0.5));
            // set the default text for the timestamp label
            queueTimestampText(timeValueLabel, tempMediaPlayer, 0.5);
            // AFTER the mediaPlayer has loaded, create a listener for the timestampSlider
            timestampSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                // when the slider is used, set the timestamp and timestamp label accordingly
                timestamp.set(tempMediaPlayer.getTotalDuration().multiply(newVal.doubleValue()));
                queueTimestampText(timeValueLabel, tempMediaPlayer, newVal.doubleValue());
            });
            timestampDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        });

        // some more stackoverflow magic
        // get the result of the dialog box (CANCEL or OK)
        timestampDialog.setResultConverter(button -> {
            // if the user clicks ok
            if (button == ButtonType.OK) {
                // add the song to queue with the designated timestamp and update the list
                queuedSongs.add(new Song(song.getDirectory(), timestamp.get()));
                updateList();
                // also return the slider value but I don't think I used this anywhere
                return timestampSlider.getValue();
            }
            return null;
        });

        // set the css style of the dialog to the same css file as the one used in the rest of the program
        timestampDialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        // show the dialog box to the user
        timestampDialog.show();
    }

    /**
     * Sets the label's text used in queue timestamp
     * @param label the label to set the text for
     * @param tempMediaPlayer the mediaPlayer to derive the song duration from
     * @param sliderValue the value of the slider
     */
    public void queueTimestampText(Label label, MediaPlayer tempMediaPlayer, double sliderValue) {
        Duration selectedTime = tempMediaPlayer.getTotalDuration().multiply(sliderValue);
        label.setText(format(selectedTime) + " / " + format(tempMediaPlayer.getTotalDuration()));
    }

    /**
     * handles the dialog which appears whenever the queue - delay button is pressed
     * @param song the song which is to be put in queue with delay
     */
    public void queueDelayDialog(Song song) {
        // create the dialog to be shown to the user and set the name of the dialog box
        Dialog<Double> delayDialog = new Dialog<>();
        delayDialog.setTitle("Set Playtime Delay");
        // IntelliJ made it an AtomicReference so it was accessible within the lambda expression listener thingymajig
        AtomicReference<Duration> delay = new AtomicReference<>(new Duration(DELAY_MAX_SECONDS*1000/6));
        // create the slider and label with the default values
        Slider delaySlider = new Slider(0, DELAY_MAX_SECONDS*1000 , DELAY_MAX_SECONDS*1000/6);
        Label timeValueLabel = new Label("Delay: 0:30");

        // handle whether the delay instance is silenced or not
        CheckBox silenced = new CheckBox("Silence Delay");

        // create a VBox which will store the delaySlider and timeValueLabel
        VBox content = new VBox(10, delaySlider, timeValueLabel, silenced);
        // set the padding of the content
        content.setPadding(new Insets(10));

        // set the content of the dialog
        delayDialog.getDialogPane().setContent(content);
        // add the respective buttons of the dialog
        delayDialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );
        // disable the OK button by default (until the mediaPlayer has loaded the song)
        delayDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        // create a temporary media player to get information about the song
        MediaPlayer tempMediaPlayer = new MediaPlayer(song.getMedia());
        // create a listener for when the mediaPlayer loads
        tempMediaPlayer.setOnReady(() -> {
            // create a listener for the slider in the delay dialogue box
            delaySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                // set the value of the delay to the value of the slider and set the text
                delay.set(new Duration(newVal.doubleValue()));
                delayDisplayText(timeValueLabel, newVal.doubleValue());
            });
            // enable the OK button
            delayDialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        });

        // get the result for when the dialogue is closed
        delayDialog.setResultConverter(button -> {
            // if the OK button is pressed
            if (button == ButtonType.OK) {
                // add both a song which acts as a delay and the actual song afterwards into the queue
                queuedSongs.add(new Song(song.getDirectory(), tempMediaPlayer.getTotalDuration().subtract(delay.get()), delay.get(), silenced.isSelected()));
                queuedSongs.add(new Song(song.getDirectory()));
                // update the list and return the value of the slider
                updateList();
                return delaySlider.getValue();
            }
            return null;
        });

        // set the CSS of the dialog to the css of the rest of the software
        delayDialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        // show the dialog
        delayDialog.show();
    }

    /**
     * Sets the label's text used in delayed queue
     * @param label the label to set the text for
     * @param sliderValue the value of the slider
     */
    public void delayDisplayText(Label label, double sliderValue) {
        Duration selectedTime = new Duration(sliderValue);
        label.setText("Delay: " + format(selectedTime));
    }

    /**
     * called whenever a song finishes, controls playing the next song in queue and finishing the queue
     */
    public void finishSong() {
        // if the last song was played in queue
        if(queuedSongs.toArray().length == 1) {
            // set the song text to being playback finished
            centerPanel.loadedSong.setText("Playback Finished");
            // stop and dispose of the mediaPlayer entirely
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            // set the isPlaying boolean to paused
            centerPanel.pause();
            // clear the queuedSongs list
            queuedSongs.clear();
            // set the playbackSlider to being disabled, it should only be active when playing
            centerPanel.playbackSlider.setDisable(true);
            // update the list
            updateList();
        } else {
            // if there are more songs in queue, remove the current song in queue and load then play the next song in queue
            queuedSongs.removeFirst();
            loadSong(queuedSongs.getFirst());
            mediaPlayer.play();
            // update the list
            updateList();
        }
    }

    /**
     * returns a String for the formatted time display between two durations
     * @param current the current duration (smaller one)
     * @param total the total duration (larger one)
     * @return a string showing the formated version of both durations
     */
    public String getPlaytimeDisplay(Duration current, Duration total) {
        return format(current) + " / " + format(total);
    }

    /**
     * gets the currently selected song on the listView
     * @return String of the filepath of the selected item on the listView
     */
    public Song getSelectedSong() {
        return listView.getSelectionModel().getSelectedItem();
    }

    public void moveSong(int indexAmount) {
        Song selectedSong = getSelectedSong();

        if(selectedSong != null) {
            for (int i = 0; i < queuedSongs.size(); i++) {
                Song song = queuedSongs.get(i);
                if(song.getDirectory().equals(selectedSong.getDirectory()) && i != 0 && i+indexAmount != 0 && i+indexAmount < queuedSongs.size()) {
                    Song targetSong = queuedSongs.get(i + indexAmount);
                    Song movingSong = queuedSongs.get(i);
                    queuedSongs.set(i, targetSong);
                    queuedSongs.set(i + indexAmount, movingSong);
                    updateList();
                    listView.getSelectionModel().select(i + indexAmount);
                    break;
                }
            }
        }
    }

    /**
     * use this ticking function as it is on the thread where JavaFX elements CANT be modified
     * this is only used for background processes
     * refer to the core/Ticking for more info
     */
    @Override
    public void tick() {
        // if there are songs in queued songs and the first song in the array hasn't been loaded
        if(!queuedSongs.isEmpty() && loadedSong != queuedSongs.getFirst()) {
            // load the song
            loadSong(queuedSongs.getFirst());
        }
    }

    /**
     * use this ticking function as it is on the thread where JavaFX elements can be modified
     * refer to the core/Ticking for more info
     */
    @Override
    public void tickThreadUI() {
        // sync this mediaPlayer to the one in centerPanel (this is a very bandaid fix but it works)
        centerPanel.mediaPlayer = this.mediaPlayer;
        if(mediaPlayer != null) {
            // set the appropriate time display if the mediaPlayer isn't null
            centerPanel.playtimeLabel.setText(getPlaytimeDisplay(mediaPlayer.getCurrentTime(), mediaPlayer.getTotalDuration()));
        } else {
            // otherwise set it to the default value for the time display
            centerPanel.playtimeLabel.setText(CenterPanel.NULL_TIME);
        }
    }
}
