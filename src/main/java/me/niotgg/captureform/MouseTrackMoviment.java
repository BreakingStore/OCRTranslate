package me.niotgg.captureform;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseTrackMoviment extends MouseMotionAdapter {

    private CaptureFrame captureFrame;
    Point start;

    public MouseTrackMoviment(CaptureFrame frame) {
        this.captureFrame = frame;
        start = new Point();
    }



    @Override
    public void mouseMoved(MouseEvent me) {
        start = me.getPoint();
        captureFrame.repaint(captureFrame.screen, captureFrame.screenCopy);
        captureFrame.jPanel.repaint();
    }
    @Override
    public void mouseDragged(MouseEvent me) {
        Point end = me.getPoint();
        captureFrame.captureRect = new Rectangle(start, new Dimension(end.x - start.x, end.y
                - start.y));
        captureFrame.repaint(captureFrame.screen, captureFrame.screenCopy);
        captureFrame.jPanel.repaint();
    }


}
