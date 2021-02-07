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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MouseTrackClick implements MouseListener {

    private CaptureFrame captureFrame;

    public MouseTrackClick(CaptureFrame frame) {
        this.captureFrame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        captureFrame.dispatchEvent(new WindowEvent(captureFrame, WindowEvent.WINDOW_CLOSING));
        Main.inCaptureFrame = false;

        Config config = Main.appManager.getConfig();

        java.awt.Image image = captureFrame.firstScreen;
        java.awt.image.FilteredImageSource fis = new java.awt.image.FilteredImageSource(
                image.getSource(), new java.awt.image.CropImageFilter(Math.round(captureFrame.captureRect.x), Math.round(captureFrame.captureRect.y), Math.round(captureFrame.captureRect.width), Math.round(captureFrame.captureRect.height))
        );
        image = null;
        image = java.awt.Toolkit.getDefaultToolkit().createImage(fis);


        BufferedImage bi = new BufferedImage
                (image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(image, 0, 0, null);
        bg.dispose();

        if (config.getInOnlyOcr()) {
            Main.appManager.getTrayIcon().displayMessage("OCRTranslate", "Lendo, aguarde...", TrayIcon.MessageType.INFO);

            try {

                String input = config.getInput();

                if (input.isEmpty()) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Você precisa configurar a entrada primeiro.");
                        alert.show();
                    });
                    return;
                }


                Tesseract tesseract = new Tesseract();

                tesseract.setDatapath("C:\\Program Files\\OCRLenguages\\");
                tesseract.setLanguage(input.replace(".traineddata", ""));



                new OutputFrame(tesseract.doOCR(bi), true);


            } catch (TesseractException tesseractException) {
                tesseractException.printStackTrace();
            }

        } else {

            Main.appManager.getTrayIcon().displayMessage("OCRTranslate", "Traduzindo, aguarde...", TrayIcon.MessageType.INFO);



            String input = config.getInput();
            String output = config.getOutput();


            if (input.isEmpty() || output.isEmpty()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Você precisa configurar a entrada e saida primeiro.");
                    alert.show();
                });
                return;
            }


            Tesseract tesseract = new Tesseract();

            tesseract.setDatapath("C:\\Program Files\\OCRLenguages\\");
            tesseract.setLanguage(input.replace(".traineddata", ""));


            TranslateAPI translateAPI = null;
            try {
                translateAPI = new TranslateAPI(Language.AUTO_DETECT, output, tesseract.doOCR(bi));
            } catch (TesseractException ex) {
                ex.printStackTrace();
            }


            new OutputFrame(translateAPI.translate(), false);

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
