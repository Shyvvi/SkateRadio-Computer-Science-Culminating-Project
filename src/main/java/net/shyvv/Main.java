package net.shyvv;

import net.shyvv.ui.BaseFrame;
import net.shyvv.core.Ticking;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    ///  --------------------------------------------------------------------------------------------------
    public static String DISPLAY_NAME = "SkateRadio";
    public static Color DEFAULT_BACKGROUND_COLOR = new Color(55, 75, 74);
    public static Color DEFAULT_PANEL_COLOR_PRIMARY = new Color(82, 103, 96);
    public static Color DEFAULT_PANEL_COLOR_SECONDARY = new Color(139, 139, 174);
    public static Color DEFAULT_TEXT_COLOR_PRIMARY = new Color(136, 217, 230);
    public static Color DEFAULT_TEXT_COLOR_SECONDARY = new Color(197, 255, 253);

    private static final List<Ticking> tickingObjects = new ArrayList<>();

    public static void main(String[] args) {
        String absoluteDirectoryPath = "C:/Users/YourName/";
        System.getProperty("user.name");

        System.out.println(System.getProperty("user.name"));
        // Ensure GUI creation happens on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BaseFrame();
            }
        });
        initializeTicking();
    }

    private static void initializeTicking() {
        try {
            while (true) {
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