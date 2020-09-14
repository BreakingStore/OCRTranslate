package me.niotgg;

import javafx.application.Application;
import me.niotgg.systemcapture.Capture;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {


    public static boolean inCaptureFrame = false;
    public static AppManager appManager;


    public static void start() {
        Application.launch(App.class, new String[0]);
    }


    public static void main(String[] args) {



        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err
                    .println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        GlobalScreen.addNativeKeyListener((NativeKeyListener) new Capture());

        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();







    }
}
