package net.shyvv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import net.shyvv.core.Ticking;
import net.shyvv.ui.PrimaryStage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

        // I wish I've found this sooner
        // this allows me to have a "ticking" function without stopping the rest of the thread
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            tick();
        }, 0, 10, TimeUnit.MILLISECONDS);

        // important code to ensure that the program actually stops when the window is closed
        stage.setOnCloseRequest(e -> {
            executor.shutdownNow();
            Platform.exit();
        });
    }


    private static void tick() {
        try {
            for(Ticking t : tickingObjects) {
                try {
                    t.tick();
                    Platform.runLater(t::tickThreadUI);
                } catch (Exception e) {
                    System.err.println("Exception found in ticking from ticking object of: " + t.getClass().getName());
                    System.err.println("Stopping instance of " + t.getClass().getName() + " from ticking furthermore");
                    e.printStackTrace();
                    t.tickingError();
                }
            }
        } catch (Exception e) {
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
