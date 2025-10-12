package org.example.labirintothreads;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader carrega a interface a partir do arquivo FXML.
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("labirinto-view.fxml"));
        // Cria a "Cena" com o layout carregado do FXML, definindo a largura e altura.
        Scene scene = new Scene(fxmlLoader.load(), 650, 600);
        // O "Stage" é a janela principal da aplicação.
        stage.setTitle("Labirinto com Threads");
        stage.setScene(scene); // Coloca a cena na janela.
        stage.show(); //Exibe a janela
    }

    public static void main(String[] args) { // metodo que inicia a aplicacao
        launch();
        Labirinto labirintoTeste = new Labirinto(20,10);
        labirintoTeste.imprimirParaConsole();
    }
}