package me.niotgg.systemcapture;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import me.niotgg.Main;
import me.niotgg.captureform.CaptureFrame;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Capture implements NativeKeyListener {




    private Boolean CTRL = false;

    private Boolean I = false;

    private Boolean Q = false;

    private Boolean L = false;

    private Boolean B = false;

    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {}

    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            if (nativeKeyEvent.getKeyCode() == 29) {
                CTRL = true;
            }
            if (nativeKeyEvent.getKeyCode() == 16) {
                Q = true;
            }

            if (CTRL && Q) {

                if (Main.inCaptureFrame) {
                    return;
                }

                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                BufferedImage screen = robot.createScreenCapture(new Rectangle(screenSize));
                new CaptureFrame(screen);
                Main.inCaptureFrame = true;
            }
    }

    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        if (nativeKeyEvent.getKeyCode() == 29) {
            CTRL = false;
        } else if (nativeKeyEvent.getKeyCode() == 16) {
            Q = false;
        }
    }
}
