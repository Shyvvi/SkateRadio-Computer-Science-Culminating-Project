package net.shyvv.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import net.shyvv.core.ShyvvButton;
import net.shyvv.core.Song;
import net.shyvv.core.Ticking;
import net.shyvv.util.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPanel implements Ticking {
    Pane panel;
    int tick;
    int TICKING_INTERVAL = 100;
    public static List<Song> storedSongs = new ArrayList<>();
    public static TreeItem<File> rootItem = new TreeItem<>(FileManager.getSystemMusicDirectory());
    private static final TreeView<File> treeView = new TreeView<>(rootItem);

    public MusicPanel(Pane sidePanel) {
        this.panel = sidePanel;
        initialize();
        initTicking();
    }

    ShyvvButton refreshDirectoryButton = new ShyvvButton("Refresh Directory", e -> {
        storedSongs.clear();
        rootItem.getChildren().clear();
        FileManager.getDirectories(FileManager.getSystemMusicDirectory());
    });

    private void initialize() {
        rootItem.setExpanded(true);
        // some stackoverflow witchery which just makes it so only the file/folder names are displayed in the tree view
        // not really sure how it works, but it does :shrug:
        treeView.setCellFactory(tv -> new javafx.scene.control.TreeCell<>() {
            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                if (empty || file == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(file.getName());
                }
            }
        });

        VBox musicSidebar = new VBox(refreshDirectoryButton, new Label("Songs"), treeView);
        treeView.setPadding(new Insets(10));

        this.panel.getChildren().add(musicSidebar);
        FileManager.getDirectories(FileManager.getSystemMusicDirectory());
    }

    public static Media getSelectedSong() {
        return new Media(treeView.getSelectionModel().getSelectedItem().getValue().toURI().toString());
    }

    @Override
    public void tick() {
        tick++;
        if(tick >= TICKING_INTERVAL) {
            FileManager.getDirectories(FileManager.getSystemMusicDirectory());
            tick = 0;
        }
    }
}
