package net.shyvv.util;

import javafx.scene.control.TreeItem;
import net.shyvv.core.Song;
import net.shyvv.ui.panels.MusicPanel;

import java.io.File;

public class FileManager {
    /**
     * Scans recursively through a directory and creates new Song instances whenever a file of any supported filetype is found (mp3, wav, etc.)
     * @param directory the starting directory where the files should start searching
     */
    public static void getDirectories(File directory) {
        // get the files in the directory provided
        File[] files = directory.listFiles();
        // if the directory provided is another directory and isn't null
        if(directory.isDirectory() && files != null) {
            // for all the files in that directory, scan with this function recursively
            for(File file : files) {
                getDirectories(file);
            }
        } else {
            // elsewise the path is either null or a file, so first get the path
            String path = directory.getPath();
            // if the path ends with .mp3, .wav or .aiff, it is a filetype supported by JavaFX's mediaPlayer
            if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".aiff")) {
                // if the addSong function returns true (meaning that the file doesn't exist in the foundSongs list already)
                if(addSong(path)) {
                    // create a tree structure for the treeView with this respective directory
                    createTreeStructure(directory);
                    System.out.println("Song at path "+path+" found and added");
                }
            }
        }
    }

    /**
     * IT WORKSSSSSSSSSSSSSSSSSSSSSSSSSS
     * this is an algorithm which creates a filepath for a TreeItem
     * @param path the starting point for the algorithm
     */
    private static void createTreeStructure(File path) {
        // get the path and first remove the music directory path from the path String,
        // leaving the directory starting from the music directory, and then split every step of the path (not sure how else to word this)
        String[] stringPaths = path.getPath().replace(getSystemMusicDirectory().getParent(), "").split("\\\\");
        // set the second index of the string array to nothing (I think this was a patchfix to prevent Music for some reason showing in the filepath)
        stringPaths[1] = "";
        // starting at the system's music directory
        String builtPath = getSystemMusicDirectory().getPath();
        // and starting at the rootItem of the treeView
        TreeItem<File> rootItem = MusicPanel.rootItem;
        // begin iterating through the path presented in stringPaths
        for(String s : stringPaths) {
            // if the string isn't empty (as shown above)
            if(!s.isEmpty()) {
                // add a path divider to the string
                builtPath += "\\" + s;
                // create a new TreeItem with this path
                TreeItem<File> newItem = new TreeItem<>(new File(builtPath));
                // IF THE rootItem CONTAINS A CHILD IDENTICAL TO THE NEW TreeItem ABOVE:
                if(containsChild(rootItem, newItem)) {
                    // iterate through the children of the rootItem
                    for(TreeItem<File> file : rootItem.getChildren()) {
                        // set the rootItem to the identical TreeItem above
                        // in the next iteration of the array this will act as a stepping stone for completing the directory
                        if(file.getValue().getAbsolutePath().equals(newItem.getValue().getAbsolutePath())) {
                            rootItem = file;
                        }
                    }
                } else {
                    // elsewise just simply create a TreeItem with this directory as it hasn't been scanned yet clearly
                    rootItem.getChildren().add(newItem);
                    rootItem = newItem;
                }
            }
        }
    }

    /**
     * function for returning whether a TreeItem contains an identical TreeItem provided within the arguements
     * @param rootItem the TreeItem who's children will be checked
     * @param targetItem the identical TreeItem which is being searched for
     * @return whether the TreeItem has an identical child
     */
    private static boolean containsChild(TreeItem<File> rootItem, TreeItem<File> targetItem) {
        // iterate through the children of the TreeItem
        for(TreeItem<File> item : rootItem.getChildren()) {
            // if an identical child is found, return true
            if(item.getValue().getAbsolutePath().equals(targetItem.getValue().getAbsolutePath())) {
                return true;
            }
        }
        // elsewise, an identical child is not found
        return false;
    }

    /**
     * returns a file object of the user's music directory
     * @return filepath to the music directory of this user
     */
    public static File getSystemMusicDirectory() {
        return new File("C:/Users/"+System.getProperty("user.name")+"/Music");
    }

    /**
     * Adds a song to the static Song array in the Main class
     * @param directory the String directory for the song
     * @return boolean determining whether the song was successfully added or not
     */
    public static boolean addSong(String directory) {
        // check for if this song is already in the storedSongs List
        for(Song song : MusicPanel.storedSongs) {
            // if it is return false
            if(song.getDirectory().equals(directory)) {
                return false;
            }
        }
        // elsewise add the song to the storedSongs array and return true
        MusicPanel.storedSongs.add(new Song(directory));
        return true;
    }
}
