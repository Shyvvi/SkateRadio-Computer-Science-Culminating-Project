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
        File[] files = directory.listFiles();
        if(directory.isDirectory() && files != null) {
            for(File file : files) {
                getDirectories(file);
            }
        } else {
            String path = directory.getPath();
            if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".aiff")) {
                if(addSong(path)) {
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
        String[] stringPaths = path.getPath().replace(getSystemMusicDirectory().getParent(), "").split("\\\\");
        stringPaths[1] = "";
        String builtPath = getSystemMusicDirectory().getPath();
        TreeItem<File> rootItem = MusicPanel.rootItem;
        for(String s : stringPaths) {
            if(!s.isEmpty()) {
                builtPath += "\\" + s;
                TreeItem<File> newItem = new TreeItem<>(new File(builtPath));
                if(containsChild(rootItem, newItem)) {
                    for(TreeItem<File> file : rootItem.getChildren()) {
                        if(file.getValue().getAbsolutePath().equals(newItem.getValue().getAbsolutePath())) {
                            rootItem = file;
                        }
                    }
                } else {
                    rootItem.getChildren().add(newItem);
                    rootItem = newItem;
                }
            }
        }
    }

    private static boolean containsChild(TreeItem<File> rootItem, TreeItem<File> targetItem) {
        for(TreeItem<File> item : rootItem.getChildren()) {
            if(item.getValue().getAbsolutePath().equals(targetItem.getValue().getAbsolutePath())) {
                return true;
            }
        }
        return false;
    }

    public static File getSystemMusicDirectory() {
        return new File("C:/Users/"+System.getProperty("user.name")+"/Music");
    }

    /**
     * Adds a song to the static Song array in the Main class
     * @param directory the String directory for the song
     * @return boolean determining whether the song was successfully added or not
     */
    public static boolean addSong(String directory) {
        for(Song song : MusicPanel.storedSongs) {
            if(song.getDirectory().equals(directory)) {
                return false;
            }
        }
        MusicPanel.storedSongs.add(new Song(directory));
        return true;
    }
}
