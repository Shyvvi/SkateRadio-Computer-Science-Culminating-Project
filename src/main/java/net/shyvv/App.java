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

    // --------- Constants ---------
    public static String DISPLAY_NAME = "SkateRadio";

    /// --------- Variables ---------
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


    /**
     * iterates through the tickingObjects created calling both threads
     * (again, refer to the core/Ticking interface for more info)
     */
    private static void tick() {
        // have a try and catch clause to prevent the program from annihilating itself
        try {
            // iterate through the tickingObjects
            for(Ticking t : tickingObjects) {
                // try to tick the object
                try {
                    // tick the first ticking function which runs on the background thread
                    t.tick();
                    // tick the second ticking function which runs on the JavaFX or frontend thread
                    Platform.runLater(t::tickThreadUI);
                } catch (Exception e) {
                    // if it doesn't, show an error, show the stacktrace and prevent it from ticking furthermore
                    System.err.println("Exception found in ticking from ticking object of: " + t.getClass().getName());
                    System.err.println("Stopping instance of " + t.getClass().getName() + " from ticking furthermore");
                    e.printStackTrace();
                    t.tickingError();
                }
            }
        } catch (Exception e) {
            // justtt to be safe
            System.err.println(e.getCause().toString());
        }
    }

    /**
     * begins ticking an object
     * @param tickingObject the object to be exempted from ticking
     */
    public static void beginTickingObject(Ticking tickingObject) {
        tickingObjects.add(tickingObject);
    }

    /**
     * exempts an object from ticking
     * @param tickingObject the object to be exempted from ticking
     */
    public static void stopTickingObject(Ticking tickingObject) {
        tickingObjects.remove(tickingObject);
    }
}
