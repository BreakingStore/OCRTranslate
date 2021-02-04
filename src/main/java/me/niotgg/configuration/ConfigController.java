package me.niotgg.configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.niotgg.Main;
import me.niotgg.translate.Language;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigController implements Initializable {



    @FXML
    private AnchorPane archorPane;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox choiceBox2;

    @FXML
    private Button downloadManagerButton;

    @FXML
    private CheckBox onlyOcrCheckBox;

    @FXML
    void downloadManagerButtonOnAction(ActionEvent event) {
        Main.appManager.showDownload();
    }



    @FXML
    void saveButtonOnAction(ActionEvent event) {

        if (choiceBox2.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Você deve selecionar uma entrada.");
            alert.show();
            return;
        }

        String[] strings = choiceBox2.getSelectionModel().getSelectedItem().toString().split("/");

        if (!strings[strings.length - 1].replace(".", " ").endsWith("traineddata")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O arquivo de entrada deve ser .traineddata");
            alert.show();
            return;
        }

        File file = new File("C:\\Program Files\\OCRLenguages\\" + choiceBox2.getSelectionModel().getSelectedItem().toString());

        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "O arquivo de linguagem informado não existe.");
            alert.show();
            return;
        }


        String language = null;
        for (Field f : Language.class.getDeclaredFields()) {

            if (Modifier.isPublic(f.getModifiers())) {
                if (f.getName().equalsIgnoreCase(choiceBox.getSelectionModel().getSelectedItem().toString())) {
                    try {
                        language = (String) f.get(0);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Config config = Main.appManager.getConfig();

        if (onlyOcrCheckBox.isSelected()) {
            config.setInOnlyOcr(true);
        } else {
            config.setInOnlyOcr(false);
        }

        config.setInput(choiceBox2.getSelectionModel().getSelectedItem().toString());
        config.setOutput(language);

        Main.appManager.getJsonFileManager().setConfig(config);


        Main.appManager.resetConfig();
        Main.appManager.getTrayIcon().displayMessage("OCRTranslate", "Salvo com sucesso.", TrayIcon.MessageType.INFO);
    }

    @FXML
    void onOnlyOcrCheckBoxAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Config config = Main.appManager.getConfig();

        File theDir = new File("C:\\Program Files\\OCRLenguages");


        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        for (String fileName : listFilesUsingJavaIO("C:\\Program Files\\OCRLenguages\\")) {
            if (fileName.split("\\.").length == 2) {
                if (fileName.split("\\.")[1].equalsIgnoreCase("traineddata")) {
                    choiceBox2.getItems().add(fileName);
                    if (fileName.equalsIgnoreCase(config.getInput())) {
                        choiceBox2.getSelectionModel().select(fileName);
                    }
                }
            }
        }

        boolean selected = false;
        for (Field f : Language.class.getFields()) {

            if (Modifier.isPublic(f.getModifiers())) {
                if (f.getName().equalsIgnoreCase("AUTO_DETECT")) {
                    continue;
                } else if (f.getName().equalsIgnoreCase(config.getOutput())) {
                    choiceBox.getItems().add(f.getName());
                    choiceBox.getSelectionModel().select(f.getName());
                    selected = true;
                } else if (f.getName().equalsIgnoreCase("PORTUGUESE") && !selected) {
                    choiceBox.getItems().add(f.getName());
                    choiceBox.getSelectionModel().select(f.getName());
                } else {
                    choiceBox.getItems().add(f.getName());
                }
            }
        }

        if (config.getInOnlyOcr()) {
            onlyOcrCheckBox.setSelected(true);
        }
    }

    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }


}
