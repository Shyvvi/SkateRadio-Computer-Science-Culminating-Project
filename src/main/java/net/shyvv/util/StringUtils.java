package net.shyvv.util;

import javafx.util.Duration;

public interface StringUtils {
    default String format(Duration d) {
        int seconds = (int) Math.floor(d.toSeconds());
        int mins = seconds / 60;
        int secs = seconds % 60;
        return mins+":"+secs;
    }
}
