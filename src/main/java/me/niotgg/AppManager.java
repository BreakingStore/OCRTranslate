package me.niotgg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.niotgg.configuration.Config;
import me.niotgg.configuration.JsonFileManager;
import me.niotgg.download.LanguageTessdata;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AppManager {

    private Stage configStage;
    private Stage downloadStage;
    private JsonFileManager jsonFileManager;
    private Config config;
    private LanguageTessdata languageTessdata = new LanguageTessdata();
    private TrayIcon trayIcon;
    private boolean inOnlyOcrMode = false;


    public AppManager() {

        jsonFileManager = new JsonFileManager();

        config = jsonFileManager.getConfig();

        Platform.runLater(() -> {
            configStage = new Stage();
            configStage.setTitle("Configuração");
            configStage.setResizable(false);
            configStage.getIcons().add(new javafx.scene.image.Image("check.png"));
        });


        Platform.runLater(() -> {
            downloadStage = new Stage();
            downloadStage.setTitle("Gerenciador de Downloads");
            downloadStage.setResizable(false);
            downloadStage.getIcons().add(new javafx.scene.image.Image("check.png"));
        });

        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/check.png"));
        PopupMenu trayPopupMenu = new PopupMenu();


        MenuItem config = new MenuItem("Open config");
        config.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.appManager.showConfig();
            }
        });
        trayPopupMenu.add(config);

        MenuItem download_manager = new MenuItem("Open download manager");
        download_manager.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.appManager.showDownload();
            }
        });
        trayPopupMenu.add(download_manager);



        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        trayIcon = new TrayIcon(image, "OCRTranslate", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }


    }


    public void showConfig() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Config.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            configStage.setScene(new Scene(root));
            configStage.show();
        });
    }

    public void resetConfig() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Config.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            configStage.setScene(new Scene(root));
        });
    }

    public void resetDownload() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Download.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadStage.setScene(new Scene(root));
        });
    }

    public void showDownload() {
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/Download.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadStage.setScene(new Scene(root));
            downloadStage.show();
        });
    }

    public Config getConfig() {
        return this.config;
    }
    public JsonFileManager getJsonFileManager() {
        return this.jsonFileManager;
    }

    public LanguageTessdata getLanguageTessdata() {
        return languageTessdata;
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public boolean getIsInOnlyOcrMode() {
        return inOnlyOcrMode;
    }

    public void setOnlyOcrMode(boolean mode) {
        inOnlyOcrMode = mode;
    }
}
