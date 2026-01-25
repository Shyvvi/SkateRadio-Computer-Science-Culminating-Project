package net.shyvv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.shyvv.core.Song;
import net.shyvv.core.Ticking;
import net.shyvv.core.ShyvvButton;
import net.shyvv.ui.PrimaryStage;
import net.shyvv.util.FileManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    ///  --------------------------------------------------------------------------------------------------
    ///  Constants
    ///  --------------------------------------------------------------------------------------------------
    public static String DISPLAY_NAME = "SkateRadio";

    ///  --------------------------------------------------------------------------------------------------
    ///  Variables
    ///  --------------------------------------------------------------------------------------------------
    private static final List<Ticking> tickingObjects = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        // have all the stage functions in a separate class for the sake of organization and not cramming everything into the same file
        new PrimaryStage(stage);

        ExecutorService executor = Executors.newFixedThreadPool(1, runnable -> {
            return new Thread(runnable);
        });
        executor.execute(new Task<>() {
            @Override
            protected Object call() throws Exception {
                initializeTicking();
                return null;
            }
        });
    }


    private static void initializeTicking() {
        try {
            while(true) {
                for(Ticking t : tickingObjects) {
                    try {
                        t.tick();
                    } catch(Exception e) {
                        System.err.println("Exception found in ticking from ticking object of: "+t.getClass().getName());
                        System.err.println("Stopping instance of "+t.getClass().getName()+" from ticking furthermore");
                        System.err.println(e.getLocalizedMessage());
                        t.tickingError();
                    }
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getCause().toString());
        }
    }

    public static void beginTickingObject(Ticking tickingObject) {
        tickingObjects.add(tickingObject);
    }
    public static void stopTickingObject(Ticking tickingObject) {
        tickingObjects.remove(tickingObject);
    }
}
