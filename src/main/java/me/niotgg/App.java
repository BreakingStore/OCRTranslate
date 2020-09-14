package me.niotgg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {





    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        Main.appManager = new AppManager();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "OCRTranslate iniciado com sucesso (Criado por NiotGG#6961)\n\nO programa roda em segundo plano, use CTRL+Q para selecionar uma area.\n\nNos icones ocultos você pode abrir as Configurações e o Gerenciador de Download.\n\nNas configurações você pode definir a linguagem de entrada e de saida, as linguagens de entrada devem ser instaladas pelo Gerenciador de Download.\n\nSe usa dois monitores a função de selecionar area funciona apenas no monitor principal.");
        alert.show();
    }


}
