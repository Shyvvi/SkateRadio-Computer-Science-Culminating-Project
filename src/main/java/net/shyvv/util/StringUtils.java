package net.shyvv.util;

import javafx.util.Duration;

public interface StringUtils {
    /**
     * returns a string with the formatted time of a duration
     * @param duration the duration to be formatted
     * @return a string with formatted time (minutes:seconds)
     */
    default String format(Duration duration) {
        // turn the duration into seconds
        int seconds = (int) Math.floor(duration.toSeconds());
        // get the amount of minutes there are
        int mins = seconds / 60;
        // put the amount of seconds into a string
        String secs = String.valueOf(seconds - (mins*60));
        // check for if the length of a string is 1
        // this is to prevent 3:5 while it should be 3:05
        if(secs.length() == 1) {
            secs = "0"+secs;
        }
        // return the final formatted string
        return mins+":"+secs;
    }
}
