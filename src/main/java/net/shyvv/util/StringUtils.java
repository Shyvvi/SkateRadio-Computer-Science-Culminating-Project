package net.shyvv.util;

import javafx.util.Duration;

public interface StringUtils {
    default String format(Duration d) {
        int seconds = (int) Math.floor(d.toSeconds());
        int mins = seconds / 60;
        String secs = String.valueOf(seconds - (mins*60));
        if(secs.length() == 1) {
            secs = "0"+secs;
        }
        return mins+":"+secs;
    }
}
