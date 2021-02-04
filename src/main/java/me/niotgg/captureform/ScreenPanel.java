package me.niotgg.captureform;

import javax.swing.*;
import java.awt.*;


public class ScreenPanel extends JPanel {

    private CaptureFrame captureFrame;

    public ScreenPanel(CaptureFrame frame) {
        this.captureFrame = frame;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(captureFrame.screenCopy, 0, 0, null);
    }

}
