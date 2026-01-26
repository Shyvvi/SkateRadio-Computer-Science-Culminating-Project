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
    public Song(String fileDirectory) {
        this.fileDirectory = fileDirectory;
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    public Song(String fileDirectory, double startTime) {
        this.fileDirectory = fileDirectory;
        this.name = new File(fileDirectory).getName();
        this.media = new Media(new File(fileDirectory).toURI().toString());
    }

    public String getSongTitle() {
        return name;
    }

    public Media getMedia() {
        return media;
    }

    public String getDirectory() {
        return this.fileDirectory;
    }
}
