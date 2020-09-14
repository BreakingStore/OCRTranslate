package me.niotgg.captureform;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import me.niotgg.Main;
import me.niotgg.configuration.Config;
import me.niotgg.output.OutputFrame;
import me.niotgg.translate.Language;
import me.niotgg.translate.TranslateAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class MouseTrackClick implements MouseListener {

    private CaptureFrame captureFrame;

    public MouseTrackClick(CaptureFrame frame) {
        this.captureFrame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (captureFrame.captureRect != null) {
            captureFrame.dispatchEvent(new WindowEvent(captureFrame, WindowEvent.WINDOW_CLOSING));
            Main.appManager.getTrayIcon().displayMessage("OCRTranslate", "Traduzindo, aguarde...", TrayIcon.MessageType.INFO);
            Main.inCaptureFrame = false;
            try {
                Robot robot = new Robot();
                BufferedImage capture = robot.createScreenCapture(captureFrame.captureRect);

                Config config = Main.appManager.getConfig();

                String input = config.getInput();
                String output = config.getOutput();


                if (input.isEmpty() || output.isEmpty()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Você precisa configurar a entrada e saida primeiro.");
                        alert.show();
                        return;
                    });
                }


                Tesseract tesseract = new Tesseract();

                tesseract.setDatapath("C:\\Program Files\\OCRLenguages\\");
                tesseract.setLanguage(input.replace(".traineddata", ""));


                TranslateAPI translateAPI = null;
                try {
                    translateAPI = new TranslateAPI(Language.AUTO_DETECT, output, tesseract.doOCR(capture));
                } catch (TesseractException ex) {
                    ex.printStackTrace();
                }


                new OutputFrame(translateAPI.translate());


            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
