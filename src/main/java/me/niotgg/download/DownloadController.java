package me.niotgg.download;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.niotgg.Main;
import me.niotgg.translate.Language;
import me.niotgg.translate.TranslateAPI;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DownloadController implements Initializable {

    private static boolean inDownload = false;



    private HashMap<String, String> urls = new HashMap<>();
    private ObservableList<DownloadLink> dataList = FXCollections.observableArrayList();
    private ObservableList<DownloadLink> dataListFilter = FXCollections.observableArrayList();

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn tableColumn;

    @FXML
    private TableColumn tableColumn2;

    @FXML
    private Button downloadButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label label;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField searchBox;

    @FXML
    private CheckBox checkBox;


    @FXML
    void downloadButtonOnAction(ActionEvent event) {
        if (!(tableView.getSelectionModel().getSelectedItems().size() > 0)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Você deve selecionar um ou mais arquivos para fazer download.");
            alert.show();
            return;
        }

        downloadButton.setVisible(false);


        ArrayList<DownloadLink> downloadLinks = new ArrayList<>(tableView.getSelectionModel().getSelectedItems());
        ArrayList<Download> download = new ArrayList<>();


        inDownload = false;
        new Thread(new Runnable() {


            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (inDownload) {
                        System.out.println("Downloading...");
                        int in = 0;
                        float somaProgress = 0;
                        float somaSpeed = 0;
                        float somaSize = 0;
                        float somaDownloaded = 0;

                        for (Download d : download) {

                            if (d.getStatus() == Download.COMPLETE) {
                                in++;
                                somaProgress += d.getProgress();
                                somaSize += d.getSize();
                                somaDownloaded += d.getDownloaded();
                            } else {
                                somaProgress += d.getProgress();
                                somaSpeed += d.getAvgSpeed();
                                somaSize += d.getSize();
                                somaDownloaded += d.getDownloaded();
                            }
                        }

                        DecimalFormat df = new DecimalFormat("#.##");

                        final double progress =  somaProgress / download.size();
                        final String formatedSpeed = df.format(somaSpeed / 1000);
                        final String formatedDownloaded = df.format(somaDownloaded / 1048576);
                        final String formatedSize = df.format(somaSize / 1048576);


                        Platform.runLater(() -> {
                            progressBar.setProgress(progress /100);
                            progressIndicator.setProgress(progress/100);
                            label.setText("Average Speed in MB/s: " + formatedSpeed + "\nDownloaded size in MB: " + formatedDownloaded + "/" + formatedSize);
                        });



                        if (in == download.size()) {
                            inDownload = false;
                            Main.appManager.getTrayIcon().displayMessage("OCRTranslate", "Download finalizado com sucesso.", TrayIcon.MessageType.INFO);
                            Main.appManager.resetDownload();
                            Main.appManager.resetConfig();
                        }
                    }
                }
            });



            @Override
            public void run() {

                File theDir = new File("C:\\Program Files\\OCRLenguages");


                if (!theDir.exists()) {
                    try {
                        theDir.mkdir();

                    } catch (SecurityException se) {
                        se.printStackTrace();
                    }
                }
                try {
                    for (DownloadLink downloadLink : downloadLinks) {
                        download.add(new Download(new URL(urls.get(downloadLink.getName())), "C:\\Program Files\\OCRLenguages"));
                    }
                    inDownload = true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                inDownload = true;
                t2.start();
            }
        }).start();










    }


    @FXML
    void checkBoxOnAction(ActionEvent event) {
        filterList(checkBox.isSelected(), null);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableView.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        tableColumn.impl_setReorderable(false);


        tableColumn.setCellValueFactory(
                new PropertyValueFactory<DownloadLink, String>("name")
        );

        tableColumn2.setCellValueFactory(
                new PropertyValueFactory<DownloadLink, String>("languageName")
        );


        GitHub gitHub = null;
        try {
            gitHub = GitHub.connectAnonymously();

        } catch (IOException e) {
            e.printStackTrace();
        }


        GHRepository repository = null;
        try {
            repository = gitHub.getRepository("tesseract-ocr/tessdata_best");
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<DownloadLink> downloadLinks = new ArrayList<>();
        try {


            HashMap<String, String> hashMap = Main.appManager.getLanguageTessdata().lenguages;


            for (GHContent s : repository.getDirectoryContent("")) {

                String name = s.getName();

                if (name.split("\\.").length == 2) {
                    if (name.split("\\.")[1].equalsIgnoreCase("traineddata")) {
                        String languageName = "";

                        if (hashMap.containsKey(name)) {
                            languageName = hashMap.get(name);
                        }
                        downloadLinks.add(new DownloadLink(s.getDownloadUrl(), name, languageName));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (DownloadLink downloadLink : downloadLinks) {
            dataList.add(downloadLink);
            tableView.getItems().add(downloadLink);
            urls.put(downloadLink.getName(), downloadLink.getLink());
        }


        filterList(checkBox.isSelected(), null);



        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList(checkBox.isSelected(), newValue);
        });
    }

    public void filterList(boolean ocultDownloaded, String newValue) {
        if (ocultDownloaded) {
            dataListFilter = FXCollections.observableArrayList();
            for (DownloadLink downloadLink : dataList) {
                File file = new File("C:\\Program Files\\OCRLenguages\\" + downloadLink.getName());

                if (!file.exists()) {
                    dataListFilter.add(downloadLink);
                }
            }

        } else {
            dataListFilter = FXCollections.observableArrayList();
            for (DownloadLink downloadLink : dataList) {
                dataListFilter.add(downloadLink);
            }
        }



        FilteredList<DownloadLink> filteredData = new FilteredList<>(dataListFilter, b -> true);
        filteredData.setPredicate(downloadLink -> {
            if (newValue == null) {
                return true;
            } else if (newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (downloadLink.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true;
            } else if (downloadLink.getLanguageName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                return true;
            }

            return false;


        });

        SortedList<DownloadLink> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }
}
