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
    public Song(String fileDirectory) {
        this.fileDirectory = fileDirectory;
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    public Song(String fileDirectory, Duration startTime) {
        this.fileDirectory = fileDirectory;
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
        this.startTime = startTime;
    }

    public Song(String fileDirectory, Duration startTime, Duration delayTime, boolean isDelay) {
        this.fileDirectory = fileDirectory;
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
        this.startTime = startTime;
        this.isDelay = isDelay;
        this.delayTime = delayTime;
    }

    public String getSongTitle() {
        return name;
    }

    public Media getMedia() {
        return media;
    }

    public boolean isDelay() {
        return isDelay;
    }

    public Duration getDelayTime() {
        return this.delayTime;
    }

    public Duration getStartTime() {
        return this.startTime;
    }

    public String getDirectory() {
        return this.fileDirectory;
    }
}
