package net.shyvv.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.shyvv.core.ShyvvButton;
import net.shyvv.core.ShyvvPanel;
import net.shyvv.core.Song;
import net.shyvv.core.Ticking;
import net.shyvv.ui.PrimaryStage;
import net.shyvv.util.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPanel extends ShyvvPanel implements Ticking {
    // --------- Variables ---------
    // these need to be static as we only want the same instance of these variables across all instances of this object
    // the stored songs after searching the Music directory
    public static List<Song> storedSongs = new ArrayList<>();
    // the root TreeItem of the music directory
    public static TreeItem<File> rootItem = new TreeItem<>(FileManager.getSystemMusicDirectory());
    // the TreeView which will display everything from the rootItem
    private static final TreeView<File> treeView = new TreeView<>(rootItem);

    // ticking tracker variable
    int tick;

    // --------- Constants ---------
    // ticking threshold (once tick > TICKING_INTERVAL, code is ran)
    int TICKING_INTERVAL = 100;

    /**
     * the panel which controls the music file explorer display on the left
     * @param stage primaryStage
     */
    public MusicPanel(PrimaryStage stage) {
        super(stage);
        initialize();
        initTicking();
    }

    // --------- Buttons ---------
    // a button for simply refreshing the Music directory in case of error, kind of redundant
    ShyvvButton refreshDirectoryButton = new ShyvvButton("Refresh Directory", e -> {
        // clear the currently stored songs and the rootItem's children
        storedSongs.clear();
        rootItem.getChildren().clear();
        // rescan the Music directory using the FileManager class
        FileManager.getDirectories(FileManager.getSystemMusicDirectory());
    });

    // init function
    private void initialize() {
        // expand the rootItem so all of its contents are visible by default
        rootItem.setExpanded(true);

        // cell factory which sets the string values for everything shown in the TreeView
        // a lot of help from StackOverflow
        treeView.setCellFactory(tv -> new javafx.scene.control.TreeCell<>() {
            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                // make sure the file isn't null or empty
                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // set the name of the TreeCell to the name of the file
                    setText(file.getName());
                }
            }
        });

        // the sidebar which contains the refreshDirectoryButton and the treeView
        VBox musicSidebar = new VBox(refreshDirectoryButton, new Label("Songs"), treeView);
        // set the padding and preferred height for the treeView
        treeView.setPadding(new Insets(10));
        treeView.setPrefHeight(800);

        // add the musicSidebar to the musicPanel
        this.panel.getChildren().add(musicSidebar);

        // scan the music directory
        FileManager.getDirectories(FileManager.getSystemMusicDirectory());
    }

    /**
     * gets the currently selected song on the treeView
     * @return String of the filepath of the selected item on the treeView
     */
    public static String getSelectedSong() {
        return treeView.getSelectionModel().getSelectedItem().getValue().getAbsolutePath();
    }

    /**
     * using this ticking function as only background work is being performed
     * refer to the core/Ticking for more info
     */
    @Override
    public void tick() {
        // increment the ticking value
        tick++;
        // if the ticking value has passed the ticking interval, reset the ticking value and rescan the music directory
        if(tick >= TICKING_INTERVAL) {
            FileManager.getDirectories(FileManager.getSystemMusicDirectory());
            tick = 0;
        }
    }

    @Override
    public void tickThreadUI() {

    }
}
