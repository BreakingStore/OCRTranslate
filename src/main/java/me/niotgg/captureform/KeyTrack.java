package me.niotgg.captureform;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import me.niotgg.Main;
import me.niotgg.configuration.Config;
import me.niotgg.output.OutputFrame;
import me.niotgg.translate.Language;
import me.niotgg.translate.TranslateAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class KeyTrack extends KeyAdapter {

    private CaptureFrame captureFrame;

    public KeyTrack(CaptureFrame frame) {
        this.captureFrame = frame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

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
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Voc� precisa configurar a entrada e saida primeiro.");
                        alert.show();
                    });
                    return;
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

}
