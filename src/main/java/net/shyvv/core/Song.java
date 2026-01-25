package net.shyvv.core;

import javafx.scene.media.Media;
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

    public String getSongTitle() {
        return name;
    }

    public Media getMedia() {
        return media;
    }

    public String getDirectory() {
        return this.fileDirectory;
    }

    public String getDuration() {
        return format(media.getDuration());
    }
}
