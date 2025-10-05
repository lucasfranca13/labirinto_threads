package org.example.labirintothreads;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class LabirintoController {

    private static final int TAMANHO_CELULA = 30; // Células de 25x25 pixels

    // a anotação @FXML conecta esta variável com o componente que tem o fx:id="gradeLabirinto" no FXML
    @FXML
    private GridPane gradeLabirinto;

    @FXML
    private Button btnIniciar;

    // chamado automaticamente quando o FXML é carregado
    @FXML
    public void initialize() {
        System.out.println("Controller inicializado! A interface está pronta.");
    }

    @FXML
    protected void iniciarSimulacao() {
        System.out.println("Botão 'Iniciar Simulação' foi clicado!");

        // INSTÂNCIA da classe Labirinto
        Labirinto labirinto = new Labirinto(20, 15); // Um labirinto de 20 de largura por 15 de altura

       desenharLabirinto(labirinto);
    }



    private void desenharLabirinto(Labirinto labirinto){
        // limpa qualquer desenho anterior que estava na grade
        gradeLabirinto.getChildren().clear();

        int largura = labirinto.getLargura();
        int altura = labirinto.getAltura();

        // percorre cada celula do modelo de dados do labirinto
        for (int i = 0; i < altura; i++){
            for (int j = 0; j < largura; j++){

                // Cria um novo retangulo para representar a celula
                Rectangle retangulo = new Rectangle(TAMANHO_CELULA, TAMANHO_CELULA);

                // Define a cor para cada tipo
                TipoCelula tipo = labirinto.getTipoNaPosicao(j,i);
                switch (tipo){
                    case RATO, INICIO_RATO -> retangulo.setFill(Color.BLUE);
                    case PAREDE -> retangulo.setFill(Color.BLACK);
                    case CAMINHO -> retangulo.setFill(Color.WHITE);
                    case QUEIJO -> retangulo.setFill(Color.YELLOW);
                }

                // adiciona retangulo na grade visual coluna = j, linha = i
                gradeLabirinto.add(retangulo, j, i);
            }
        }
    }
}