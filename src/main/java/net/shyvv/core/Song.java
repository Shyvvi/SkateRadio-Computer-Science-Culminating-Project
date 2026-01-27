package net.shyvv.core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.shyvv.util.StringUtils;

import java.io.File;

public class Song implements StringUtils {
    Media media;
    String name;
    String fileDirectory;
    Duration startTime = new Duration(0);
    Duration delayTime;
    boolean isDelay = false;

    /**
     * the Song class stores all the information used for playing the song, and it's playtime behavior
     * this super constructor is used for simply adding a song into queue
     * @param fileDirectory the complete filepath of the song file
     */
    public Song(String fileDirectory) {
        this.fileDirectory = fileDirectory;
        // just derive the File and Media objects from the fileDirectory
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    /**
     * the Song class stores all the information used for playing the song, and it's playtime behavior
     * this super constructor is used for adding a song into queue with a start time
     * @param fileDirectory the complete filepath of the song file
     * @param startTime the time in which this song will begin playing at
     */
    public Song(String fileDirectory, Duration startTime) {
        this.fileDirectory = fileDirectory;
        this.startTime = startTime;
        // just derive the File and Media objects from the fileDirectory
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    /**
     * the Song class stores all the information used for playing the song, and it's playtime behavior
     * this super constructor is used for adding a song into queue with a delay beforehand, YRSC style
     * @param fileDirectory the complete filepath of the song file
     * @param startTime the time in which the DELAY will begin playing at
     * @param delayTime the length of the delay
     */
    public Song(String fileDirectory, Duration startTime, Duration delayTime) {
        this.fileDirectory = fileDirectory;
        this.startTime = startTime;
        this.delayTime = delayTime;
        // make sure this Song instance is now designated as a delay
        this.isDelay = true;
        // just derive the File and Media objects from the fileDirectory
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    /**
     * gets the name of the song file
     * @return name of the song file
     */
    public String getSongTitle() {
        return name;
    }

    /**
     * gets the Media object
     * @return Media
     */
    public Media getMedia() {
        return media;
    }

    /**
     * gets whether this Song is a delay or not
     * @return whether this Song is a delay or not
     */
    public boolean isDelay() {
        return isDelay;
    }

    /**
     * gets the length of the delay
     * @return delay duration
     */
    public Duration getDelayTime() {
        return this.delayTime;
    }

    /**
     * gets the starting time of the song
     * @return start time
     */
    public Duration getStartTime() {
        return this.startTime;
    }

    /**
     * gets the directory of the song file for this song object
     * @return complete directory of the .mp3, .wav or .aiff
     */
    public String getDirectory() {
        return this.fileDirectory;
    }
}
